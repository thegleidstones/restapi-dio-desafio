package com.gleidsonsilva.restapi_dio_desafio.service.impl;

import com.gleidsonsilva.restapi_dio_desafio.domain.model.*;
import com.gleidsonsilva.restapi_dio_desafio.domain.repository.UserRepository;
import com.gleidsonsilva.restapi_dio_desafio.service.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class UserServiceImplTest {

    public static final long ID = 1L;
    public static final long ID1 = 2L;
    public static final BigDecimal LIMIT = BigDecimal.valueOf(3000);
    public static final BigDecimal BALANCE = BigDecimal.valueOf(2000);
    public static final String NUMBER = "4525.1265,4578.9988";
    public static final String NUMBER1 = "4525CC";
    public static final String AGENCY = "001";
    public static final String PIX_ICON = "pix.icon";
    public static final String EXTRACT_ICON = "extract.icon";
    public static final String LIGHT_ICON = "light.icon";
    public static final String INVESTIMENT_ICON = "investiment.icon";
    public static final String NAME = "Gleidson Morais Silva";
    public static final String PIX_TRANSACTIONS = "Pix Transactions";
    public static final String ACCOUNT_EXTRACT = "Account extract";
    public static final String SE_PROCURA_SOLUCOES_CLIQUE_AQUI = "Se procura soluções, clique aqui!";
    public static final String OS_MELHORES_INVESTIMENTOS = "Os melhores investimentos!";
    public static final int INDEX = 0;
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_WITH_ID_1_CAN_NOT_BE_CREATED = "User with ID 1 can not be created.";
    public static final String USER_TO_CREATE_MUST_NOT_BE_NULL = "User to create must not be null.";
    public static final String USER_ACCOUNT_MUST_NOT_BE_NULL = "User account must not be null";
    public static final String USER_CARD_MUST_NOT_BE_NULL = "User card must not be null";

    @InjectMocks
    private UserServiceImpl service;

    @Mock
    private UserRepository repository;

    private User user;
    private Optional<User> optionalUser;
    private Account account;
    private Card card;
    private List<Feature> features;
    private List<News> news;

    @BeforeEach
    void setUp() {
        openMocks(this);
        startUser();
    }

    @Test
    void whenFindAllThenReturnAListOfUsers() {
        when(repository.findAll()).thenReturn(List.of(user));

        List<User> response = service.findAll();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(User.class, response.get(INDEX).getClass());

        assertEquals(ID, response.get(INDEX).getId());
        assertEquals(NAME, response.get(INDEX).getName());
        assertEquals(AGENCY, response.get(INDEX).getAccount().getAgency());
        assertEquals(NUMBER1, response.get(INDEX).getAccount().getNumber());
        assertEquals(NUMBER, response.get(INDEX).getCard().getNumber());
    }

    @Test
    void whenFindByIdThenReturnAnUserInstance() {
        when(repository.findById(anyLong())).thenReturn(optionalUser);

        User response = service.findById(ID);

        assertNotNull(response);
        assertEquals(User.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(NUMBER1, response.getAccount().getNumber());
        assertEquals(AGENCY, response.getAccount().getAgency());
        assertEquals(BALANCE, response.getAccount().getBalance());
        assertEquals(LIMIT, response.getAccount().getLimit());
        assertEquals(NUMBER, response.getCard().getNumber());
        assertEquals(LIMIT, response.getCard().getLimit());
        assertEquals(INVESTIMENT_ICON, response.getNews().get(INDEX).getIcon());
        assertEquals(OS_MELHORES_INVESTIMENTOS, response.getNews().get(INDEX).getDescription());
        assertEquals(PIX_ICON, response.getFeatures().get(INDEX).getIcon());
        assertEquals(PIX_TRANSACTIONS, response.getFeatures().get(INDEX).getDescription());
    }

    @Test
    void whenFindByIdThenReturnNotFoundException() {
        when(repository.findById(anyLong())).thenThrow(new NoSuchElementException(USER_NOT_FOUND));

        try {
            service.findById(ID);
        } catch (Exception e) {
            assertEquals(NoSuchElementException.class, e.getClass());
            assertEquals(USER_NOT_FOUND, e.getMessage());
        }
    }

    @Test
    void whenCreateThenReturnSuccess() {
        user.setId(2L);
        user.setName(null);
        when(repository.save(any())).thenReturn(user);

        User response = service.create(user);

        assertNotNull(response);
    }

    @Test
    void whenCreateThenReturnBusinessException() {
        when(repository.save(any())).thenThrow(new BusinessException(USER_WITH_ID_1_CAN_NOT_BE_CREATED));

        try {
            service.create(user);
        } catch (Exception e) {
            assertEquals(BusinessException.class, e.getClass());
            assertEquals(USER_WITH_ID_1_CAN_NOT_BE_CREATED, e.getMessage());
        }
    }

    @Test
    void whenCreateThenThrowsBusinessExceptionOnNullUser() {
        Exception exception = assertThrows(BusinessException.class, () -> service.create(null));
        assertEquals(USER_TO_CREATE_MUST_NOT_BE_NULL, exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void whenCreateThenThrowsBusinessExceptionOnNullAccount() {
        user.setAccount(null);

        Exception exception = assertThrows(BusinessException.class, () -> service.create(user));
        assertEquals(USER_ACCOUNT_MUST_NOT_BE_NULL, exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void whenCreateThenThrowsBusinessExceptionOnNullCard() {
        user.setCard(null);

        Exception exception = assertThrows(BusinessException.class, () -> service.create(user));
        assertEquals(USER_CARD_MUST_NOT_BE_NULL, exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    private void startAccount() {
        account = new Account(ID, NUMBER1, AGENCY, BALANCE, LIMIT);
    }

    private void startCard() {
        card =new Card(ID, NUMBER, LIMIT);
    }

    private void startFeatures() {
        Feature f1 = new Feature(ID, PIX_ICON, PIX_TRANSACTIONS);
        Feature f2 = new Feature(ID1, EXTRACT_ICON, ACCOUNT_EXTRACT);
        features = List.of(f1, f2);
    }

    private void startNews() {
        News n1 = new News(ID, INVESTIMENT_ICON, OS_MELHORES_INVESTIMENTOS);
        News n2 = new News(ID1, LIGHT_ICON, SE_PROCURA_SOLUCOES_CLIQUE_AQUI);
        news = List.of(n1, n2);
    }

    private void startUser() {
        this.startAccount();
        this.startCard();
        this.startFeatures();
        this.startNews();

        user = new User(ID, NAME, account, card, features, news);
        optionalUser = Optional.of(new User(ID, NAME, account, card, features, news));
    }
}