package de.ecotram.backend.entity;

import lombok.Getter;

import javax.persistence.*;

@MappedSuperclass
public abstract class EntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
}
