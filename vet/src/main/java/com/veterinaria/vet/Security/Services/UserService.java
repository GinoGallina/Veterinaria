package com.veterinaria.vet.Security.Services;

import java.util.Optional;

import javax.security.auth.login.LoginException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.veterinaria.vet.Models.Response;
import com.veterinaria.vet.Security.DTO.LoginUser;
import com.veterinaria.vet.Security.DTO.NewUser;
import com.veterinaria.vet.Security.Models.Rol;
import com.veterinaria.vet.Security.Models.User;
import com.veterinaria.vet.Security.Repositories.UserRepository;

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

    public Optional<User> getByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public Optional<User> getById(long id){
        return userRepository.findById(id);
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public boolean login(LoginUser loginUser, HttpSession session) throws LoginException{
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getEmail(),loginUser.getPassword()));
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

    public ResponseEntity<Object> save(NewUser nuevoUser) throws JsonProcessingException{
        Response json = new Response();
        if(userRepository.existsByEmail(nuevoUser.getEmail())){
            json.setMessage("Ya existe un usuario con dicho mail");
            return new ResponseEntity<Object>(json.toJson(), HttpStatus.BAD_REQUEST);
        }
        User User =
                new User(nuevoUser.getEmail(), 
                        passwordEncoder.encode(nuevoUser.getPassword()));
        Rol rol = new Rol();
        rol = (rolService.getByRolNombre("USER").get());
        if(nuevoUser.getRol()=="ADMIN"){
            rol = (rolService.getByRolNombre("ADMIN").get());
        } else if (nuevoUser.getRol()=="VET"){
            rol = (rolService.getByRolNombre("VET").get());
        } 
        User.setRol(rol);
        userRepository.save(User);
        return ResponseEntity.ok(User) ;
    }
    
    public void eliminarLogico(Long id) {
        userRepository.eliminarLogico(id);
    }

    public void saveLogico(Long id) {
        userRepository.saveLogico(id);
    }
}