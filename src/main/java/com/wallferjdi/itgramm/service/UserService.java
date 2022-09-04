package com.wallferjdi.itgramm.service;

import com.wallferjdi.itgramm.dto.UserDTO;
import com.wallferjdi.itgramm.entity.User;
import com.wallferjdi.itgramm.entity.enums.ERole;
import com.wallferjdi.itgramm.exception.UserExistException;
import com.wallferjdi.itgramm.repository.UserRepository;
import com.wallferjdi.itgramm.payload.request.SignupRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class UserService {

    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(SignupRequest userIn){
        User user = new User();
        user.setEmail(userIn.getEmail());
        user.setName(userIn.getFirstname());
        user.setLastname(userIn.getLastname());
        user.setUsername(userIn.getUsername());
        user.setPassword(passwordEncoder.encode(userIn.getPassword()));
        user.getRoles().add(ERole.ROLE_USER);

        try {
            System.out.println("saving user "+ user.getEmail());
            LOG.info("saving user "+ user.getEmail());
            return userRepository.save(user);

        }catch (Exception e) {
            LOG.error("Error during registration "+ e.getMessage());
         throw new UserExistException(" User"+userIn.getUsername()+" already exist");
        }
    }

    public User updateUSer(UserDTO userDTO, Principal principal){
        User user = getUserFromPrincipal(principal);
        user.setName(userDTO.getName());
        user.setLastname(userDTO.getLastname());
        user.setBio(userDTO.getBio());
        return userRepository.save(user);
    }
    public User getCurrentUser(Principal principal){
        return getUserFromPrincipal(principal);
    }

    public User getUserFromPrincipal(Principal principal){
        String username = principal.getName();
        return userRepository.findUserByUsername(username).orElseThrow(()-> new UsernameNotFoundException(" User with" +
                "this username not exist "+ username));
    }

    public User getUserById(Long userId) {
        return userRepository.findUserById(userId).orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }
}
