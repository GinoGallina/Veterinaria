package com.veterinaria.vet.Security.Services;

import java.text.ParseException;
import java.util.Optional;

import javax.security.auth.login.LoginException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.veterinaria.vet.Security.DTO.JwtDTO;
import com.veterinaria.vet.Security.DTO.LoginUser;
import com.veterinaria.vet.Security.DTO.NewUser;
import com.veterinaria.vet.Security.Models.Rol;
import com.veterinaria.vet.Security.Models.User;
import com.veterinaria.vet.Security.Repositories.UserRepository;
import com.veterinaria.vet.Security.jwt.JwtProvider;

import jakarta.servlet.http.HttpSession;
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

    public boolean login(LoginUser loginUser, HttpSession session) throws LoginException{
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getEmail(), loginUser.getPassword()));
            System.out.println(authentication.isAuthenticated());
            if (!authentication.isAuthenticated()) {
                return false;
            }        
            session.setAttribute("user_role", authentication.getAuthorities().toString());
            session.setAttribute("user_email", authentication.getName());
            session.setAttribute("user_id", userRepository.findByEmail(authentication.getName()).get().getID());
            return true;
        }catch(Exception e){
            throw new LoginException("No tienes permisos para acceder a esta página");
        }
    }

    public void logout(HttpSession session){
        session.invalidate();
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