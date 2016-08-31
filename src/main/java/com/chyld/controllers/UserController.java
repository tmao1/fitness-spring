package com.chyld.controllers;

import com.chyld.dtos.AuthDto;
import com.chyld.entities.Profile;
import com.chyld.entities.Role;
import com.chyld.entities.User;
import com.chyld.enums.RoleEnum;
import com.chyld.security.JwtToken;
import com.chyld.utilities.JwtUtil;
import com.chyld.services.RoleService;
import com.chyld.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;

@RestController

public class UserController {

    private static final String EMAIL_EXISTS_MESSAGE = "This email is in use";

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(final UserService userService, final PasswordEncoder encoder, final JwtUtil jwtUtil, final RoleService roleService) {
        this.userService = userService;
        this.passwordEncoder = encoder;
        this.jwtUtil = jwtUtil;
        this.roleService = roleService;
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@Valid @RequestBody AuthDto auth) throws JsonProcessingException {
        final String requestedEmail = auth.getUsername();

        UserDetails ud =  userService.loadUserByUsername(requestedEmail);
        if (ud != null) {
            return ResponseEntity.badRequest().body(EMAIL_EXISTS_MESSAGE);
        }

        User user = new User();
        user.setEnabled(true);
        user.setUsername(auth.getUsername());
        user.setPassword(passwordEncoder.encode(auth.getPassword()));

        user.setRoles(new ArrayList<>());
        Role r = roleService.findByRole(RoleEnum.USER);
        user.getRoles().add(r);

        User savedUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(null);
    }

    @RequestMapping(value = "/users/profile", method = RequestMethod.POST)
    public ResponseEntity<?> createProfile(@Valid @RequestBody Profile profile, Principal user) throws JsonProcessingException {


            int uid = ((JwtToken)user).getUserId();
            User u = userService.findUserById(uid);

        if(u.getProfile() != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
                    u.setProfile(profile);
            profile.setUser(u);
            userService.saveUser(u);
return ResponseEntity.status(HttpStatus.CREATED).body(null);


    }

}
