package de.ecotram.backend.entity;

import lombok.Getter;

import javax.persistence.*;

@MappedSuperclass
public abstract class EntityBase {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
}