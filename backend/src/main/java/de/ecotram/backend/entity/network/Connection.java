package de.ecotram.backend.entity.network;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import de.ecotram.backend.entity.EntityBase;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
@NoArgsConstructor
public final class Connection extends EntityBase {
    public static final int DEFAULT_LENGTH = 500; // m

    @Setter
    private int length = DEFAULT_LENGTH;

    @ManyToOne
    @JsonManagedReference
    private Station sourceStation;

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

    @Data
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static final class Pair {
        private Connection sourceDestination;
        private Connection destinationSource;
    }
}