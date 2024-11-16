package org.testTask.Services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testTask.databases.entity.User;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsersServiceTest {

    private UsersService usersService;

    @Mock
    private EntityManagerFactory firstDbEntityManagerFactory;

    @Mock
    private EntityManagerFactory secondDbEntityManagerFactory;

    @Mock
    private EntityManager firstDbEntityManager;

    @Mock
    private EntityManager secondDbEntityManager;

    @Mock
    private CriteriaBuilder firstDbCriteriaBuilder;

    @Mock
    private CriteriaBuilder secondDbCriteriaBuilder;

    @Mock
    private CriteriaQuery<User> firstDbCriteriaQuery;

    @Mock
    private CriteriaQuery<User> secondDbCriteriaQuery;

    @Mock
    private Root<User> firstDbUserRoot;

    @Mock
    private Root<User> secondDbUserRoot;

    @Mock
    private TypedQuery<User> firstDbQuery;

    @Mock
    private TypedQuery<User> secondDbQuery;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        List<User> usersFromFirstDb = Arrays.asList(
                new User("name1", "username1", "surname1"),
                new User("name2", "username2", "surname2")
        );

        List<User> usersFromSecondDb = Arrays.asList(
                new User("name3", "username3", "surname3"),
                new User("name4", "username4", "surname4")
        );

        when(firstDbEntityManagerFactory.createEntityManager()).thenReturn(firstDbEntityManager);
        when(secondDbEntityManagerFactory.createEntityManager()).thenReturn(secondDbEntityManager);

        when(firstDbEntityManager.createQuery(anyString())).thenReturn(firstDbQuery);
        when(secondDbEntityManager.createQuery(anyString())).thenReturn(secondDbQuery);

        when(firstDbQuery.getResultList()).thenReturn(usersFromFirstDb);
        when(secondDbQuery.getResultList()).thenReturn(usersFromSecondDb);

        when(firstDbEntityManager.getCriteriaBuilder()).thenReturn(firstDbCriteriaBuilder);
        when(secondDbEntityManager.getCriteriaBuilder()).thenReturn(secondDbCriteriaBuilder);

        when(firstDbCriteriaBuilder.createQuery(User.class)).thenReturn(firstDbCriteriaQuery);
        when(secondDbCriteriaBuilder.createQuery(User.class)).thenReturn(secondDbCriteriaQuery);

        when(firstDbCriteriaQuery.from(User.class)).thenReturn(firstDbUserRoot);
        when(secondDbCriteriaQuery.from(User.class)).thenReturn(secondDbUserRoot);

        when(firstDbEntityManager.createQuery(firstDbCriteriaQuery)).thenReturn(firstDbQuery);
        when(secondDbEntityManager.createQuery(secondDbCriteriaQuery)).thenReturn(secondDbQuery);

        usersService = new UsersService(firstDbEntityManagerFactory, secondDbEntityManagerFactory);

    }

    @Test
    void findAllUsers_shouldReturnUsersFromBothDatabases() {
        List<User> allUsers = usersService.findAllUsers();

        assertNotNull(allUsers);
        assertEquals(4, allUsers.size());  // 2 users from first DB + 2 users from second DB

        List<String> userNames = allUsers.stream().map(User::getName).toList();
        assertTrue(userNames.containsAll(Arrays.asList("name1", "name2", "name3", "name4")));

        verify(firstDbEntityManagerFactory).createEntityManager();
        verify(secondDbEntityManagerFactory).createEntityManager();
        verify(firstDbEntityManager).getCriteriaBuilder();
        verify(secondDbEntityManager).getCriteriaBuilder();
        verify(firstDbCriteriaBuilder).createQuery(User.class);
        verify(secondDbCriteriaBuilder).createQuery(User.class);
        verify(firstDbCriteriaQuery).from(User.class);
        verify(secondDbCriteriaQuery).from(User.class);
        verify(firstDbQuery).getResultList();
        verify(secondDbQuery).getResultList();
    }
}

