package edu.ualberta.med.biobank.auth;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.ualberta.med.biobank.domain.Status;
import edu.ualberta.med.biobank.exception.UserNotActiveException;
import edu.ualberta.med.biobank.repositories.UserRepository;

@Service
public class BiobankUserDetailsService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(BiobankUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    // see https://javapointers.com/spring/spring-security/spring-custom-userdetailsservice-example/
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var dbUsers = userRepository.findByLogin(username);

        if (dbUsers.size() <= 0) {
            throw new UsernameNotFoundException("User details not found for this username: " + username);
        }

        var dbUser = dbUsers.get(0);
        if (!dbUser.getActivityStatus().equals(Status.ACTIVE)) {
            throw new UserNotActiveException("Invalid credentials");
        }

        List<SimpleGrantedAuthority> authList = new ArrayList<>();
        authList.add(new SimpleGrantedAuthority("ROLE_USER"));

        for (var group : dbUser.getGroups()) {
            if (group.getName().equals("Global Administrators")) {
                authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }
        }

        User user = new User(username, dbUser.getCsmUser().getPassword(), authList);
        return user;
    }
}
