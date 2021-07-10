package de.ecotram.backend.entity;

import lombok.Getter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class EntityBase {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;
}