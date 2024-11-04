package com.gleidsonsilva.restapi_dio_desafio.service.impl;

import com.gleidsonsilva.restapi_dio_desafio.domain.model.User;
import com.gleidsonsilva.restapi_dio_desafio.domain.repository.UserRepository;
import com.gleidsonsilva.restapi_dio_desafio.service.UserService;
import com.gleidsonsilva.restapi_dio_desafio.service.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static java.util.Optional.ofNullable;

@Service
public class UserServiceImpl implements UserService {

    private static final Long UNCHANGEALBE_USER_ID = 1L;

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Transactional
    public User findById(Long id) {
        return this.userRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public User create(User user) {
        ofNullable(user).orElseThrow(() -> new BusinessException("User to create must not be null."));
        ofNullable(user.getAccount()).orElseThrow(() -> new BusinessException("User account must not be null"));
        ofNullable(user.getCard()).orElseThrow(() -> new BusinessException("User card must not be null"));

        this.validateChangeableId(user.getId(), "created");

        if (this.userRepository.existsByAccountNumber(user.getAccount().getNumber())) {
            throw new IllegalArgumentException("This Account number already exists.");
        }

        if (this.userRepository.existsByCardNumber(user.getCard().getNumber())) {
            throw new BusinessException("This card number already exists");
        }

        return this.userRepository.save(user);
    }

    @Transactional
    public User update(User userToUpdate, Long id) {
        this.validateChangeableId(id, "updated");

        User existingUser = this.findById(id);

        if (!existingUser.getId().equals(userToUpdate.getId())) {
            throw new BusinessException("Update IDs must be the same");
        }

        existingUser.setName(userToUpdate.getName());
        existingUser.setAccount(userToUpdate.getAccount());
        existingUser.setCard(userToUpdate.getCard());
        existingUser.setFeatures(userToUpdate.getFeatures());
        existingUser.setNews(userToUpdate.getNews());

        return this.userRepository.save(existingUser);
    }

    @Transactional
    public void delete(Long id) {
        this.validateChangeableId(id, "deleted");

        User existingUser = this.findById(id);

        this.userRepository.delete(existingUser);
    }

    private void validateChangeableId(Long id, String operation) {
        if (UNCHANGEALBE_USER_ID.equals(id)) {
            throw new BusinessException("User with ID %d can not be %s.".formatted(UNCHANGEALBE_USER_ID, operation));
        }
    }
}
