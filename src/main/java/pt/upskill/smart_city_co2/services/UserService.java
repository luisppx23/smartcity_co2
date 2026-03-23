package pt.upskill.smart_city_co2.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pt.upskill.smart_city_co2.entities.User;
import pt.upskill.smart_city_co2.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (userRepository.count() > 0) return;
        {
            userRepository.save(new User("João", "Castor", "joaocastor", LocalDateTime.of(2026, 3, 20, 10, 15, 30), "joao.pastar@gmail.com", passwordEncoder.encode("joao123"), "cidadao", true));
            userRepository.save(new User("Maria", "Silva", "mariasilva", LocalDateTime.of(2026, 3, 20, 11, 0, 0), "maria.silva@gmail.com", passwordEncoder.encode("maria123"), "cidadao", true));
            userRepository.save(new User("Carlos", "Ferreira", "carlosf", LocalDateTime.of(2026, 3, 20, 11, 30, 0), "carlos.ferreira@gmail.com", passwordEncoder.encode("carlos123"), "cidadao", true));
            userRepository.save(new User("Ana", "Costa", "anacosta", LocalDateTime.of(2026, 3, 20, 12, 0, 0), "ana.costa@gmail.com", passwordEncoder.encode("ana123"), "cidadao",true));
        }
    }

    public List<User> getUser() {
        return userRepository.findAll();
    }

    public User getUser(Long id) {
        return userRepository.getReferenceById(id);
    }

}
