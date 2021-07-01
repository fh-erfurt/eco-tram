package de.ecotram.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import de.ecotram.backend.entity.network.Traversable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class LineEntry {

    @EmbeddedId
    Key id = new Key();

    @Getter
    @Setter
    int orderValue = 0;

    @Getter
    @Setter
    @ManyToOne
    @JsonBackReference
    @MapsId("lineId")
    @JoinColumn
    Line line;

    @Getter
    @Setter
    @ManyToOne
    @JsonManagedReference
    @MapsId("traversableId")
    @JoinColumn
    Traversable traversable;

    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    static class Key implements Serializable {

        @Getter
        @Setter
        @Column
        Long lineId;

        @Getter
        @Setter
        @Column
        Long traversableId;
    }
}
