package pt.upskill.smart_city_co2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pt.upskill.smart_city_co2.entities.Cidadao;
import pt.upskill.smart_city_co2.entities.Municipio;
import pt.upskill.smart_city_co2.entities.User;
import pt.upskill.smart_city_co2.models.SignUpModel;
import pt.upskill.smart_city_co2.repositories.CidadaoRepository;
import pt.upskill.smart_city_co2.repositories.MunicipioRepository;
import pt.upskill.smart_city_co2.repositories.UserRepository;

import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CidadaoRepository cidadaoRepository;

    @Autowired
    MunicipioRepository municipioRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User register(SignUpModel signUpModel) {

        // VERIFICAR EMAIL
        if (userRepository.findByEmail(signUpModel.getEmail()).isPresent()) {
            throw new RuntimeException("Email já está em uso");
        }

        // verificar username
        if (userRepository.findByUsername(signUpModel.getUsername()).isPresent()) {
            throw new RuntimeException("Username já está em uso");
        }


        String encodedPassword = passwordEncoder.encode(signUpModel.getPassword());

        if ("cidadao".equals(signUpModel.getTipo())) {
            Cidadao cidadao = new Cidadao(
                    signUpModel.getFirstName(),
                    signUpModel.getLastName(),
                    signUpModel.getUsername(),
                    LocalDateTime.now(),
                    signUpModel.getEmail(),
                    encodedPassword,
                    signUpModel.getNif(),
                    signUpModel.getTipo(),
                    true
            );
            return cidadaoRepository.save(cidadao);
        } else if ("municipio".equals(signUpModel.getTipo())) {
            Municipio municipio = new Municipio(
                    null, // nome do municipio - definido posteriormente
                    0.0, // objetivo_co2_mes_hab - definido posteriormente
                    signUpModel.getUsername(),
                    LocalDateTime.now(),
                    signUpModel.getEmail(),
                    encodedPassword,
                    signUpModel.getNif(),
                    signUpModel.getTipo(),
                    true
            );
            return municipioRepository.save(municipio);
        }

        return null;
    }

    public User getUser(String username) {
        return userRepository.getUserByUsername(username);
    }

    public User validateLogin(String username, String password) {
        User user = getUser(username);
        if (user == null || user.getId() == null) {
            return null;
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }

        return user;
    }

    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return null;
        }
        String username = auth.getPrincipal().toString();
        return getUser(username);
    }

    public boolean verificarDadosRecuperacao(String username, String email) {
        return userRepository.findByUsernameAndEmail(username, email).isPresent();
    }

    public void atualizarPassword(String username, String novaPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));

        user.setPassword(passwordEncoder.encode(novaPassword));
        userRepository.save(user);
    }
}