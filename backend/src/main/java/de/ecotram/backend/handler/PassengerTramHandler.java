package de.ecotram.backend.handler;

import de.ecotram.backend.entity.PassengerTram;
import de.ecotram.backend.repository.PassengerTramRepository;
import de.ecotram.backend.repository.StationRepository;
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

    public PassengerTram createPassengerTramFromRequest(PassengerTramBody passengerTramBody) throws ErrorResponseException {
        validatePassengerTramBody(passengerTramBody);
        PassengerTram passengerTram = passengerTramBody.applyToPassengerTram();
        passengerTramRepository.save(passengerTram);

        return passengerTram;
    }

    public PassengerTram updatePassengerTramFromRequest(PassengerTram passengerTram, PassengerTramBody passengerTramBody) throws ErrorResponseException {
        validatePassengerTramBody(passengerTramBody);
        PassengerTram updatedPassengerTram = passengerTramBody.applyToPassengerTram(passengerTram);
        passengerTramRepository.save(updatedPassengerTram);

        return updatedPassengerTram;
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

        public PassengerTram applyToPassengerTram(PassengerTram passengerTram) {
            passengerTram.setSpeed(speed);
            passengerTram.setWeight(weight);
            passengerTram.setMaxPassengers(maxPassengers);
            passengerTram.setCurrentPassengers(currentPassengers);

            return passengerTram;
        }

        public PassengerTram applyToPassengerTram() {
            return applyToPassengerTram(new PassengerTram());
        }
    }
}