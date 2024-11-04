package com.gleidsonsilva.restapi_dio_desafio.service;

import com.gleidsonsilva.restapi_dio_desafio.domain.model.User;

public interface UserService {
    User findById(Long id);

    User create(User user);
}
