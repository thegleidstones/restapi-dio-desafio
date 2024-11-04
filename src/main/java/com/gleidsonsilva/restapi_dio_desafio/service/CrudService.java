package com.gleidsonsilva.restapi_dio_desafio.service;

import java.util.List;

public interface CrudService<T, ID> {
    List<T> findAll();
    T findById(ID id);
    T create(T entity);
    T update(T entity, ID id);
    void delete(ID id);
}
