package com.wallferjdi.itgramm.service;

import com.wallferjdi.itgramm.entity.User;
import com.wallferjdi.itgramm.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username)  {
        User user = userRepository.findUserByEmail(username).orElseThrow(()->
                new UsernameNotFoundException("Username bot found :"+username));
        return build(user);

    }
    public User loadUserById(Long id){
        return userRepository.findUserById(id).orElse(null);
    }

    public static User build(User user){
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role-> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
        return new User(user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getAuthorities());
    }
}
