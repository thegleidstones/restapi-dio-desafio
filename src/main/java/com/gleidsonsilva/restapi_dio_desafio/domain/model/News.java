package com.gleidsonsilva.restapi_dio_desafio.domain.model;

import jakarta.persistence.Entity;

@Entity(name = "tb_news")
public class News extends BaseItem {
    public News() {
        super();
    }

    public News(Long id, String icon, String description) {
        super(id, icon, description);
    }

}
