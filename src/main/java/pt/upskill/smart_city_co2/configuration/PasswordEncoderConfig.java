package pt.upskill.smart_city_co2.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//Bean de codificação de senhas usando BCrypt (fator 12)
//utilizado pelo Spring Security para hash e validação de passwords

@Configuration
public class PasswordEncoderConfig {
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(12);
    }
}
