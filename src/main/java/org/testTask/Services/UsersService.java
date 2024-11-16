package org.testTask.Services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.testTask.Services.Interfaces.UsersServiceInterface;
import org.testTask.databases.entity.User;

import java.util.List;
import java.util.stream.Stream;

@Service
public class UsersService implements UsersServiceInterface {

    @Autowired
    public UsersService(@Qualifier("db1EntityManagerFactory") EntityManagerFactory firstDbEntityManagerFactory,
                        @Qualifier("db2EntityManagerFactory") EntityManagerFactory secondDbEntityManagerFactory){
        this.firstDbEntityManagerFactory = firstDbEntityManagerFactory;
        this.secondDbEntityManagerFactory = secondDbEntityManagerFactory;
    }

    public EntityManagerFactory firstDbEntityManagerFactory;
    public EntityManagerFactory secondDbEntityManagerFactory;

    @Override
    public List<User> findAllUsers() {
      return Stream.concat(getAllUsersFromDb(firstDbEntityManagerFactory).stream(), getAllUsersFromDb(secondDbEntityManagerFactory).stream()).toList();
    }

    private List<User> getAllUsersFromDb(EntityManagerFactory entityManagerFactory){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);

        criteriaQuery.select(userRoot);

        Query query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
