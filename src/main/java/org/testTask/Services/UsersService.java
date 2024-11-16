package org.testTask.Services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.testTask.Services.Interfaces.UsersServiceInterface;
import org.testTask.databases.entity.User;

import java.util.List;
import java.util.stream.Stream;

@Service
public class UsersService implements UsersServiceInterface {

    private static final Logger logger = LogManager.getLogger(UsersService.class);

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
        logger.info("Fetching all users from both databases.");
        List<User> usersFromFirstDb = getAllUsersFromDb(firstDbEntityManagerFactory);
        List<User> usersFromSecondDb = getAllUsersFromDb(secondDbEntityManagerFactory);

        logger.info("Retrieved {} users from the first database", usersFromFirstDb.size());
        logger.info("Retrieved {} users from the second database", usersFromSecondDb.size());

        return Stream.concat(usersFromFirstDb.stream(), usersFromSecondDb.stream()).toList();
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
