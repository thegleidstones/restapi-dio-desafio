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

    public static final int INDEX = 0;
    public static final long ID1 = 1L;
    public static final long ID2 = 2L;
    public static final BigDecimal LIMIT = BigDecimal.valueOf(3000);
    public static final BigDecimal BALANCE = BigDecimal.valueOf(2000);
    public static final String CARD_NUMBER1 = "4525.1265,4578.9988";
    public static final String CARD_NUMBER2 = "1235.7852;9974.1365";
    public static final String ACCOUNT_NUMBER1 = "4525CC";
    public static final String ACCOUNT_NUMBER2 = "4613CC";
    public static final String AGENCY = "001";
    public static final String PIX_ICON = "pix.icon";
    public static final String EXTRACT_ICON = "extract.icon";
    public static final String LIGHT_ICON = "light.icon";
    public static final String INVESTMENT_ICON = "investment.icon";
    public static final String NAME1 = "Gleidson Morais Silva";
    public static final String NAME2 = "Mariana Freitas Lima";
    public static final String PIX_TRANSACTIONS = "Pix Transactions";
    public static final String ACCOUNT_EXTRACT = "Account extract";
    public static final String SE_PROCURA_SOLUCOES_CLIQUE_AQUI = "Se procura soluções, clique aqui!";
    public static final String OS_MELHORES_INVESTIMENTOS = "Os melhores investimentos!";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_WITH_ID_1_CAN_NOT_BE_CREATED = "User with ID 1 can not be created";
    public static final String USER_WITH_ID_1_CAN_NOT_BE_UPDATED = "User with ID 1 can not be updated";
    public static final String USER_TO_CREATE_MUST_NOT_BE_NULL = "User to create must not be null";
    public static final String USER_ACCOUNT_MUST_NOT_BE_NULL = "User account must not be null";
    public static final String USER_CARD_MUST_NOT_BE_NULL = "User card must not be null";
    public static final String UPDATE_IDS_MUST_BE_THE_SAME = "Update IDs must be the same";
    public static final String THIS_CARD_NUMBER_ALREADY_EXISTS = "This card number already exists";
    public static final String THIS_ACCOUNT_NUMBER_ALREADY_EXISTS = "This Account number already exists";
    public static final String USER_NAME_MUST_NOT_BE_NULL = "User name must not be null";

    @InjectMocks
    private UserServiceImpl service;

    @Mock
    private UserRepository repository;

    private User user1;
    private User user2;
    private Account account;
    private Card card;

    @BeforeEach
    void setUp() {
        openMocks(this);
        startUser();
    }

    @Test
    void whenFindAllThenReturnAListOfUsers() {
        // Arrange
        when(repository.findAll()).thenReturn(List.of(user1));

        // Act
        List<User> response = service.findAll();

        // Assert
        assertNotNull(response, "The response should not be null");
        assertEquals(1, response.size(), "The response should contain exactly one user");

        User returnedUser = response.get(INDEX);
        assertNotNull(returnedUser, "The returned user should not be null");

        assertEquals(ID1, returnedUser.getId(), "User ID1 should match");
        assertEquals(NAME1, returnedUser.getName(), "User name should match");

        assertNotNull(returnedUser.getAccount(), "Account should not be null");
        assertEquals(AGENCY, returnedUser.getAccount().getAgency(), "User account agency should match");
        assertEquals(ACCOUNT_NUMBER1, returnedUser.getAccount().getNumber(), "User account number should match");

        assertNotNull(returnedUser.getCard(), "Card should not be null");
        assertEquals(CARD_NUMBER1, returnedUser.getCard().getNumber(), "User card number should match");
        assertEquals(LIMIT, returnedUser.getCard().getLimit(), "User card limit should match");

        assertInstanceOf(User.class, returnedUser, "Returned user should be an instance of User.class");
    }

    @Test
    void whenFindByIdThenReturnAnUserInstance() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(user1));

        User response = service.findById(ID1);

        assertNotNull(response, "The response should not be null");

        assertEquals(ID1, response.getId());
        assertEquals(NAME1, response.getName());

        assertNotNull(response.getAccount());
        assertEquals(ACCOUNT_NUMBER1, response.getAccount().getNumber());
        assertEquals(AGENCY, response.getAccount().getAgency());
        assertEquals(BALANCE, response.getAccount().getBalance());
        assertEquals(LIMIT, response.getAccount().getLimit());

        assertNotNull(response.getCard());
        assertEquals(CARD_NUMBER1, response.getCard().getNumber());
        assertEquals(LIMIT, response.getCard().getLimit());

        assertNotNull(response.getFeatures());
        assertEquals(INVESTMENT_ICON, response.getNews().get(INDEX).getIcon());
        assertEquals(OS_MELHORES_INVESTIMENTOS, response.getNews().get(INDEX).getDescription());

        assertNotNull(response.getNews());
        assertEquals(PIX_ICON, response.getFeatures().get(INDEX).getIcon());
        assertEquals(PIX_TRANSACTIONS, response.getFeatures().get(INDEX).getDescription());

        assertInstanceOf(User.class, response);
    }

    @Test
    void whenFindByIdThenReturnNotFoundException() {
        Exception exception = assertThrows(NoSuchElementException.class, () -> service.findById(anyLong()));
        assertNull(exception.getMessage(), "Exception message should be null");
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void whenCreateThenReturnSuccess() {
        when(repository.save(any())).thenReturn(user2);

        User response = service.create(user2);

        assertNotNull(response);
        verify(repository, times(1)).save(user2);
    }

    @Test
    void whenCreateThenReturnBusinessExceptionOnNullUserName() {
        user2.setName(null);
        Exception exception = assertThrows(BusinessException.class, () -> service.create(user2));
        assertEquals(USER_NAME_MUST_NOT_BE_NULL, exception.getMessage());
        verify(repository, never()).save(user2);
    }

    @Test
    void whenCreateThenReturnBusinessExceptionOnCreateUserWithId1() {
        Exception exception = assertThrows(BusinessException.class, () -> service.create(user1));
        assertEquals(USER_WITH_ID_1_CAN_NOT_BE_CREATED, exception.getMessage());
        verify(repository, never()).save(user1);
    }

    @Test
    void whenCreateThenThrowsBusinessExceptionOnNullUser() {
        Exception exception = assertThrows(BusinessException.class, () -> service.create(null));
        assertEquals(USER_TO_CREATE_MUST_NOT_BE_NULL, exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void whenCreateThenThrowsBusinessExceptionOnNullAccount() {
        user2.setAccount(null);

        Exception exception = assertThrows(BusinessException.class, () -> service.create(user2));
        assertEquals(USER_ACCOUNT_MUST_NOT_BE_NULL, exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void whenCreateThenThrowsBusinessExceptionOnAccountNumberAlreadyExists() {
        when(repository.findByAccountNumber(anyString())).thenReturn(Optional.of(user2));

        try {
            user2.setId(0L);
            service.create(user2);
        } catch (Exception exception) {
            assertEquals(THIS_ACCOUNT_NUMBER_ALREADY_EXISTS, exception.getMessage());
            assertInstanceOf(BusinessException.class, exception);
            verify(repository, never()).save(user2);
        }

    }

    @Test
    void whenCreateThenThrowsBusinessExceptionOnCardNumberAlreadyExists() {
        when(repository.findByCardNumber(anyString())).thenReturn(Optional.of(user2));

        try {
            user2.setId(0L);
            service.create(user2);
        } catch (Exception exception) {
            assertEquals(THIS_CARD_NUMBER_ALREADY_EXISTS, exception.getMessage());
            assertInstanceOf(BusinessException.class, exception);
            verify(repository, never()).save(any());
        }
    }

    @Test
    void whenCreateThenThrowsBusinessExceptionOnNullCard() {
        user2.setCard(null);
        Exception exception = assertThrows(BusinessException.class, () -> service.create(user2));
        assertEquals(USER_CARD_MUST_NOT_BE_NULL, exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void whenUpdateThenReturnSuccess() {
        when(repository.findById(ID2)).thenReturn(Optional.of(user2));
        when(repository.save(user2)).thenReturn(user2);

        User toUpdate = new User();
        toUpdate.setId(ID2);
        toUpdate.setName(NAME2);
        toUpdate.setAccount(new Account(ID1, ACCOUNT_NUMBER1, AGENCY, BALANCE, LIMIT));
        toUpdate.setCard(new Card(ID2, CARD_NUMBER2, LIMIT));

        User response = service.update(toUpdate, ID2);

        assertNotNull(response);
        verify(repository, times(1)).save(user2);
    }

    @Test
    void whenUpdateReturnBusinessExceptionOnUserWithID1() {
        Exception exception = assertThrows(BusinessException.class, () -> service.update(user1, ID1));
        assertEquals(USER_WITH_ID_1_CAN_NOT_BE_UPDATED, exception.getMessage());
        verify(repository, never()).save(user1);
    }

    @Test
    void whenUpdateThenReturnBusinessExceptionOnUserWithIDDifferentFromParameter() {
        when(repository.findById(ID2)).thenReturn(Optional.of(user2));
        Exception exception = assertThrows(BusinessException.class, () -> service.update(user1, ID2));
        assertEquals(UPDATE_IDS_MUST_BE_THE_SAME, exception.getMessage());
        verify(repository, never()).save(user2);
    }

    @Test
    void whenUpdateThenReturnBusinessExceptionOnUserWithNullName() {
        when(repository.findById(ID2)).thenReturn(Optional.of(user2));
        user2.setName(null);
        Exception exception = assertThrows(BusinessException.class, () -> service.update(user2, ID2));
        assertEquals(USER_NAME_MUST_NOT_BE_NULL, exception.getMessage());
        verify(repository, never()).save(user2);
    }

    @Test
    void whenUpdateThenReturnBusinessExceptionOnNullAccount() {
        when(repository.findById(ID2)).thenReturn(Optional.of(user2));
        user2.setAccount(null);
        Exception exception = assertThrows(BusinessException.class, () -> service.update(user2, ID2));
        assertEquals(USER_ACCOUNT_MUST_NOT_BE_NULL, exception.getMessage());
        verify(repository, never()).save(user2);
    }

    @Test
    void whenUpdateThenReturnBusinessExceptionOnNullCard() {
        when(repository.findById(ID2)).thenReturn(Optional.of(user2));
        user2.setCard(null);
        Exception exception = assertThrows(BusinessException.class, () -> service.update(user2, ID2));
        assertEquals(USER_CARD_MUST_NOT_BE_NULL, exception.getMessage());
        verify(repository, never()).save(user2);
    }

    @Test
    void whenDeleteThenReturnSuccess() {
        when(repository.findById(ID2)).thenReturn(Optional.of(user2));

        service.delete(ID2);
        verify(repository, times(1)).delete(user2);
    }

    @Test
    void whenDeleteThrowsNoSuchElementException() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        Exception exception = assertThrows(NoSuchElementException.class, () -> service.delete(ID2));
        assertNull(exception.getMessage());
        verify(repository, never()).deleteById(anyLong());
    }

    private void startUser() {
        Account account1 = new Account(ID1, ACCOUNT_NUMBER1, AGENCY, BALANCE, LIMIT);
        Account account2 = new Account(ID2, ACCOUNT_NUMBER2, AGENCY, BALANCE, LIMIT);

        Card card1 = new Card(ID1, CARD_NUMBER1, LIMIT);
        Card card2 = new Card(ID2, CARD_NUMBER2, LIMIT);

        Feature f1 = new Feature(ID1, PIX_ICON, PIX_TRANSACTIONS);
        Feature f2 = new Feature(ID2, EXTRACT_ICON, ACCOUNT_EXTRACT);
        List<Feature> features = List.of(f1, f2);

        News n1 = new News(ID1, INVESTMENT_ICON, OS_MELHORES_INVESTIMENTOS);
        News n2 = new News(ID2, LIGHT_ICON, SE_PROCURA_SOLUCOES_CLIQUE_AQUI);
        List<News> news = List.of(n1, n2);

        user1 = new User(ID1, NAME1, account1, card1, features, news);
        user2 = new User(ID2, NAME2, account2, card2, features, news);
    }
}