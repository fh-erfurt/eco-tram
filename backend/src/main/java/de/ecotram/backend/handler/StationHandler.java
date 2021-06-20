package de.ecotram.backend.handler;

import de.ecotram.backend.entity.network.Station;
import de.ecotram.backend.repository.StationRepository;
import de.ecotram.backend.utilities.ErrorResponseException;
import de.ecotram.backend.utilities.Utilities;
import lombok.Getter;
import org.springframework.lang.Nullable;

public final class StationHandler {
    public void validateStationBody(StationBody stationBody) throws ErrorResponseException {
        if (!Utilities.isFloatValid(stationBody.length, 0))
            throw new ErrorResponseException("invalid-length", "length is invalid");

        if (!Utilities.isIntegerValid(stationBody.maxWeight, 0))
            throw new ErrorResponseException("invalid-max-weight", "maxWeight is invalid");

        if (!Utilities.isFloatValid(stationBody.trafficFactor, 0))
            throw new ErrorResponseException("invalid-traffic-factor", "trafficFactor is invalid");

        if (!Utilities.isStringValid(stationBody.name, 1, 100))
            throw new ErrorResponseException("invalid-name", "name is invalid");

        if (!Utilities.isIntegerValid(stationBody.maxPassengers, 0))
            throw new ErrorResponseException("invalid-max-passengers", "maxPassengers is invalid");

        if (!Utilities.isIntegerValid(stationBody.currentPassengers, 0))
            throw new ErrorResponseException("invalid-current-passengers", "currentPassengers is invalid");
    }

    public Station createStationFromRequest(StationRepository stationRepository, StationBody stationBody) throws ErrorResponseException {
        validateStationBody(stationBody);
        Station station = stationBody.applyToStation();
        stationRepository.save(station);

        return station;
    }

    public Station updateStationFromRequest(Station station, StationRepository stationRepository, StationBody stationBody) throws ErrorResponseException {
        validateStationBody(stationBody);
        Station updatedStation = stationBody.applyToStation(station);
        stationRepository.save(updatedStation);

        return updatedStation;
    }

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

        @Nullable
        @Getter
        private int currentPassengers;

        public Station applyToStation(Station station) {
            station.setLength(length);
            station.setMaxWeight(maxWeight);
            station.setTrafficFactor(trafficFactor);
            station.setName(name);
            station.setMaxPassengers(maxPassengers);
            station.setCurrentPassengers(currentPassengers);

            return station;
        }

        public Station applyToStation() {
            return applyToStation(new Station());
        }
    }
}