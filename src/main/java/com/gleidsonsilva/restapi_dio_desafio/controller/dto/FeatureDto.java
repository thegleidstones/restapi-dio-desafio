package com.gleidsonsilva.restapi_dio_desafio.controller.dto;

import com.gleidsonsilva.restapi_dio_desafio.domain.model.Feature;

public record FeatureDto(Long id, String icon, String description) {

    public FeatureDto(Feature model) {
        this(model.getId(), model.getIcon(), model.getDescription());
    }

    public Feature toModel() {
        Feature model = new Feature();

        model.setId(this.id);
        model.setIcon(this.icon);
        model.setDescription(this.description);

        return model;
    }

}
