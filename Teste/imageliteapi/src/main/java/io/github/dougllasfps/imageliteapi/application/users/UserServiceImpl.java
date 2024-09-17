package io.github.dougllasfps.imageliteapi.application.users;

import io.github.dougllasfps.imageliteapi.application.jwt.JwtService;
import io.github.dougllasfps.imageliteapi.domain.AccessToken;
import io.github.dougllasfps.imageliteapi.domain.entity.User;
import io.github.dougllasfps.imageliteapi.domain.exception.DuplicatedTupleException;
import io.github.dougllasfps.imageliteapi.domain.service.UserService;
import io.github.dougllasfps.imageliteapi.infra.repository.specs.UserRepositoey;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
//@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepositoey userRepositoey;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserServiceImpl(UserRepositoey userRepositoey, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepositoey = userRepositoey;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public User getByEmail(String email) {
        return userRepositoey.findByEmail(email);
    }

    @Override
    @Transactional
    public User save(User user) {
        var possibleUser = getByEmail(user.getEmail());
        if(possibleUser != null){
            throw new DuplicatedTupleException("User already exists!");
        }
        encodePassword(user);
        return userRepositoey.save(user);
    }

    @Override
    public AccessToken autheticate(String email, String password){
        var user = getByEmail(email);
        if(user == null){
            return null;
        }

        boolean matches = passwordEncoder.matches(password, user.getPassword());

        if (matches){
            return jwtService.generateToken(user);
        }

        return null;
    }

    private void encodePassword(User user){
        String rawPassword = user.getPassword();
        String encodePassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodePassword);
    }
}
