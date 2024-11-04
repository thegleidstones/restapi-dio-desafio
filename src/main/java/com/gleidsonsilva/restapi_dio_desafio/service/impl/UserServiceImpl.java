package com.gleidsonsilva.restapi_dio_desafio.service.impl;

import com.gleidsonsilva.restapi_dio_desafio.domain.model.User;
import com.gleidsonsilva.restapi_dio_desafio.domain.repository.UserRepository;
import com.gleidsonsilva.restapi_dio_desafio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public User create(User user) {
        if (userRepository.existsByAccountNumber(user.getAccount().getNumber())) {
            throw new IllegalArgumentException("This Account number already exists.");
        }

        return userRepository.save(user);
    }
}
