package de.ecotram.backend.handler;

import de.ecotram.backend.entity.network.Station;
import de.ecotram.backend.repository.StationRepository;
import de.ecotram.backend.utilities.ErrorResponseException;
import de.ecotram.backend.utilities.ValidationUtilities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component("stationHandler")
public final class StationHandler {

    @Autowired
    private StationRepository stationRepository;

    public void validateStationBody(StationBody stationBody) throws ErrorResponseException {
        if (!ValidationUtilities.isFloatValid(stationBody.length, 0))
            throw new ErrorResponseException("invalid-length", "length is invalid");

        if (!ValidationUtilities.isIntegerValid(stationBody.maxWeight, 0))
            throw new ErrorResponseException("invalid-max-weight", "maxWeight is invalid");

        if (!ValidationUtilities.isFloatValid(stationBody.trafficFactor, 0))
            throw new ErrorResponseException("invalid-traffic-factor", "trafficFactor is invalid");

        if (!ValidationUtilities.isStringValid(stationBody.name, 1, 100))
            throw new ErrorResponseException("invalid-name", "name is invalid");

        if (!ValidationUtilities.isIntegerValid(stationBody.maxPassengers, 0))
            throw new ErrorResponseException("invalid-max-passengers", "maxPassengers is invalid");

    }

    public Station createStationFromRequest(StationBody stationBody) throws ErrorResponseException {
        validateStationBody(stationBody);
        Station station = stationBody.applyToStation();
        stationRepository.save(station);

        return station;
    }

    public Station updateStationFromRequest(Station station, StationBody stationBody) throws ErrorResponseException {
        validateStationBody(stationBody);
        Station updatedStation = stationBody.applyToStation(station);
        stationRepository.save(updatedStation);

        return updatedStation;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class StationBody {
        @Getter
        private int length;

        @Getter
        private int maxWeight;

        @Getter
        private float trafficFactor;

        @Getter
        private String name;

        @Nullable
        @Getter
        private int maxPassengers;

        public Station applyToStation(Station station) {
            station.setName(name);
            station.setMaxPassengers(maxPassengers);

            return station;
        }

        public Station applyToStation() {
            return applyToStation(new Station());
        }
    }
}