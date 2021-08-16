package de.ecotram.backend.entity.network;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import de.ecotram.backend.entity.EntityBase;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * A directed connection between 2 stations.
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
public final class Connection extends EntityBase {
    public static final int DEFAULT_LENGTH = 500; // in meters

    /**
     * The length of this connection in meters.
     */
    @Setter
    private int length = DEFAULT_LENGTH;

    /**
     * The station this connection leads off from.
     */
    @ManyToOne
    @JsonManagedReference
    private Station sourceStation;

    /**
     * The station this connection leads to.
     */
    @ManyToOne
    @JsonManagedReference
    private Station destinationStation;

    private Connection(Builder builder) {
        this.length = builder.length;
        this.sourceStation = builder.sourceStation;
        this.destinationStation = builder.destinationStation;
    }

    public static Builder builder() {
        return new Builder();
    }

    // a non-lombok builder was consciously chosen here to avoid some ctor problems
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static final class Builder {
        private int length = DEFAULT_LENGTH;
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

        public Connection build() {
            return new Connection(this);
        }
    }

    /**
     * A pair of connections for stations that are connected in both directions.
     */
    @Data
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static final class Pair {
        private Connection sourceDestination;
        private Connection destinationSource;
    }
}