package de.ecotram.backend.entity.network;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
@NoArgsConstructor
public final class Connection extends Traversable {
    @ManyToOne
    private Station sourceStation;

    @ManyToOne
    private Station destinationStation;

    private Connection(Builder builder) {
        this.length = builder.length;
        this.maxWeight = builder.maxWeight;
        this.trafficFactor = builder.trafficFactor;
        this.sourceStation = builder.sourceStation;
        this.destinationStation = builder.destinationStation;
    }

    public static Builder builder() {
        return new Builder();
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static final class Builder {
        private int length = Traversable.DEFAULT_LENGTH;
        private int maxWeight = Traversable.DEFAULT_MAX_WEIGHT;
        private float trafficFactor = Traversable.DEFAULT_TRAFFIC_FACTOR;
        private Station sourceStation;
        private Station destinationStation;

        public Builder sourceStation(Station sourceStation) {
            this.sourceStation = sourceStation;
            return this;
        }

        public Builder destinationStation(Station destinationStation) {
            this.destinationStation = destinationStation;
            return this;
        }

        public Builder length(int length) {
            this.length = length;
            return this;
        }

        public Builder maxWeight(int maxWeight) {
            this.maxWeight = maxWeight;
            return this;
        }

        public Builder trafficFactor(float trafficFactor) {
            this.trafficFactor = trafficFactor;
            return this;
        }

        public Connection build() {
            return new Connection(this);
        }

        @FunctionalInterface
        public interface ModifyDelegate {
            Builder Modify(Builder a);
        }
    }

    @Data
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static final class Pair {
        private Connection sourceDestination;
        private Connection destinationSource;
    }
}