package com.veterinaria.vet.Security.Services;

import java.text.ParseException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.veterinaria.vet.Security.DTO.JwtDTO;
import com.veterinaria.vet.Security.DTO.LoginUser;
import com.veterinaria.vet.Security.DTO.NewUser;
import com.veterinaria.vet.Security.Models.Rol;
import com.veterinaria.vet.Security.Models.User;
import com.veterinaria.vet.Security.Repositories.UserRepository;
import com.veterinaria.vet.Security.jwt.JwtProvider;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {
  
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RolService rolService;

    @Autowired
    JwtProvider jwtProvider;

    public Optional<User> getByEmail(String email){
        return userRepository.findByEmail(email);
    }

    // public Optional<User> getByUsernameOrEmail(String nombreOrEmail){
    //     return userRepository.findByUsernameOrEmail(nombreOrEmail, nombreOrEmail);
    // }

    public Optional<User> getByUserToken(String userToken){
        return userRepository.findByUserToken(userToken);
    }

    // public boolean existsByUsername(String nombreUser){
    //     return userRepository.existsByUsername(nombreUser);
    // }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public JwtDTO login(LoginUser loginUser){
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getEmail(), loginUser.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        return new JwtDTO(jwt);
    }

    public JwtDTO refresh(JwtDTO JwtDTO) throws ParseException {
        String token = jwtProvider.refreshToken(JwtDTO);
        return new JwtDTO(token);
    }

    public ResponseEntity<?> save(NewUser nuevoUser){
        if(userRepository.existsByEmail(nuevoUser.getEmail()))
            return ResponseEntity.badRequest().body("Email de usuario ya existe");
        User User =
                new User(nuevoUser.getEmail(), 
                        passwordEncoder.encode(nuevoUser.getPassword()));
        Rol rol = new Rol();
        rol = (rolService.getByRolNombre("USER").get());
        if(nuevoUser.getRol()=="ADMIN")
            rol = (rolService.getByRolNombre("ADMIN").get());
        User.setRol(rol);
        userRepository.save(User);
        return ResponseEntity.ok(User);
    }
}