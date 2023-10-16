package edu.ualberta.med.biobank.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ualberta.med.biobank.domain.User;
import edu.ualberta.med.biobank.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public List<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public User findOneWithMemberships(String login) {
        return userRepository.findOneWithMemberships(login);
    }
}
