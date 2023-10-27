package edu.ualberta.med.biobank.auth;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.ualberta.med.biobank.domain.Status;
import edu.ualberta.med.biobank.services.UserService;

@Service
public class BiobankUserDetailsService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(BiobankUserDetailsService.class);

    private UserService userService;

    public BiobankUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    // see https://javapointers.com/spring/spring-security/spring-custom-userdetailsservice-example/
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var userInfoMaybe = userService.findByLogin(username);

        if (userInfoMaybe.isLeft()) {
            throw new UsernameNotFoundException("User not found for this username: " + username);
        }

        var userInfo = userInfoMaybe.getRight().get();
        List<SimpleGrantedAuthority> authList = new ArrayList<>();
        authList.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (userInfo.isGlobalAdmin()) {
            authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return User.withUsername(username)
            .password(userInfo.password())
            .disabled(!userInfo.isActive())
            .authorities(authList)
            .build();
    }
}
