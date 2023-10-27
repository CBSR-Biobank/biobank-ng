package edu.ualberta.med.biobank.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import edu.ualberta.med.biobank.domain.User;
import edu.ualberta.med.biobank.errors.AppError;
import edu.ualberta.med.biobank.errors.EntityNotFound;
import edu.ualberta.med.biobank.repositories.UserRepository;
import edu.ualberta.med.biobank.repositories.UserSpecifications;
import io.jbock.util.Either;

@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);


    private UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Either<AppError, User> findByLogin(String login) {
        var users = userRepository.findByLogin(login);
        if (users.size() != 1) {
            return Either.left(new EntityNotFound("user by username"));
        }
        return Either.right(users.get(0));
    }

    public Either<AppError, User> findOneWithMemberships(String username) {
        var users = userRepository.findAll(UserSpecifications.isUsername(username));
        if (users.size() != 1) {
            return Either.left(new EntityNotFound("user by username"));
        }
        return Either.right(users.get(0));
    }
}
