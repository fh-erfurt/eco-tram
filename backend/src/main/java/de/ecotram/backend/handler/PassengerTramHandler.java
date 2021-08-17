package de.ecotram.backend.handler;

import de.ecotram.backend.entity.Tram;
import de.ecotram.backend.repository.PassengerTramRepository;
import de.ecotram.backend.utilities.ErrorResponseException;
import de.ecotram.backend.utilities.ValidationUtilities;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component("passengerTramHandler")
public final class PassengerTramHandler {
    @Autowired
    private PassengerTramRepository passengerTramRepository;

    public void validatePassengerTramBody(PassengerTramBody passengerTramBody) throws ErrorResponseException {
        if (!ValidationUtilities.isIntegerValid(passengerTramBody.weight, 0))
            throw new ErrorResponseException("invalid-max-weight", "weight is invalid");

        if (!ValidationUtilities.isFloatValid(passengerTramBody.speed, 0))
            throw new ErrorResponseException("invalid-traffic-factor", "speed is invalid");

        if (!ValidationUtilities.isIntegerValid(passengerTramBody.maxPassengers, 0))
            throw new ErrorResponseException("invalid-max-passengers", "maxPassengers is invalid");

        if (!ValidationUtilities.isIntegerValid(passengerTramBody.currentPassengers, 0))
            throw new ErrorResponseException("invalid-current-passengers", "currentPassengers is invalid");
    }

    public Tram createPassengerTramFromRequest(PassengerTramBody passengerTramBody) throws ErrorResponseException {
        validatePassengerTramBody(passengerTramBody);
        Tram tram = passengerTramBody.applyToPassengerTram();
        passengerTramRepository.save(tram);

        return tram;
    }

    public Tram updatePassengerTramFromRequest(Tram tram, PassengerTramBody passengerTramBody) throws ErrorResponseException {
        validatePassengerTramBody(passengerTramBody);
        Tram updatedTram = passengerTramBody.applyToPassengerTram(tram);
        passengerTramRepository.save(updatedTram);

        return updatedTram;
    }

    public static class PassengerTramBody {
        @Getter
        private int speed;

        @Getter
        private int weight;

        @Nullable
        @Getter
        private int maxPassengers;

        @Nullable
        @Getter
        private int currentPassengers;

        public Tram applyToPassengerTram(Tram tram) {
            tram.setSpeed(speed);
            tram.setWeight(weight);
            tram.setMaxPassengers(maxPassengers);
            tram.setCurrentPassengers(currentPassengers);

            return tram;
        }

        public Tram applyToPassengerTram() {
            return applyToPassengerTram(new Tram());
        }
    }
}