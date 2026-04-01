package pt.upskill.smart_city_co2.configuration;

import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityWebConfig {

    @Autowired
    UserAuthenticationProvider userAuthenticationProvider;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.authorizeHttpRequests(auth -> {

            auth.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll();

            // Recursos públicos — sem login
            auth.requestMatchers(
                    "/auth/**",
                    "/assets/**",        // ← ESTA É A LINHA QUE FALTAVA
                    "/styles/**",
                    "/scripts/**",
                    "/images/**",
                    "/WEB-INF/**"
            ).permitAll();

            // Rotas por role
            auth.requestMatchers("/auth/autenticado").authenticated();
            auth.requestMatchers("/auth/homeCidadao").hasRole("CIDADAO");
            auth.requestMatchers("/auth/homeMunicipio").hasRole("MUNICIPIO");
            auth.requestMatchers("/cidadao/**").hasRole("CIDADAO");
            auth.requestMatchers("/municipio/**").hasRole("MUNICIPIO");

            auth.anyRequest().denyAll();
        });

        httpSecurity.formLogin(login -> {
            login.loginPage("/auth/login");
            login.loginProcessingUrl("/login");
            login.defaultSuccessUrl("/auth/autenticado", true);
            login.permitAll();
        });

        httpSecurity.logout(logout -> {
            logout.logoutUrl("/logout");
            logout.logoutSuccessUrl("/auth/login");
            logout.permitAll();
        });

        httpSecurity.authenticationProvider(userAuthenticationProvider);
        return httpSecurity.build();
    }
}