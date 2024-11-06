package com.gleidsonsilva.restapi_dio_desafio.domain.model;

import jakarta.persistence.Entity;

@Entity(name = "tb_features")
public class Feature extends  BaseItem{
    public Feature () {
        super();
    }

    public Feature(Long id, String icon, String description) {
        super(id, icon, description);
    }
}
