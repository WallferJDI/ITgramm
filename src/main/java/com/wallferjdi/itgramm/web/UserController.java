package com.wallferjdi.itgramm.web;

import com.wallferjdi.itgramm.dto.UserDTO;
import com.wallferjdi.itgramm.entity.User;
import com.wallferjdi.itgramm.facade.UserFacade;
import com.wallferjdi.itgramm.service.ResponseErrorValidator;
import com.wallferjdi.itgramm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("api/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserFacade userFacade;
    @Autowired
    private ResponseErrorValidator responseErrorValidator;

    @GetMapping("/")
     public ResponseEntity<UserDTO> getCurrentUser(Principal principal){
         UserDTO userDTO = userFacade.userToUserDTO(userService.getCurrentUser(principal));
         return new ResponseEntity<>(userDTO, HttpStatus.OK);

     }
    @GetMapping("/{userId}")
     public ResponseEntity<UserDTO> getUserProfile(@PathVariable("userId") String userId){
        User user = userService.getUserById(Long.parseLong(userId));
        UserDTO userDTO = userFacade.userToUserDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult,Principal principal){
        ResponseEntity<Object> errors = responseErrorValidator.mapValidationService(bindingResult);

        if(!ObjectUtils.isEmpty(errors)) return errors;
        User user = userService.updateUSer(userDTO,principal);
        UserDTO userUpdated = userFacade.userToUserDTO(user);
        return new ResponseEntity<>(userUpdated,HttpStatus.OK);

    }
}
