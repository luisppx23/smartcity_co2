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
    UserAuthenticationProvider userAuthenticationProvider; // Provedor de autenticação customizado

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // Desabilita CSRF para simplificar em produção
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        // Configuração de regras de autorização
        httpSecurity.authorizeHttpRequests(auth -> {
            // Requisições de forward (redirecionamentos internos)
            auth.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll();

            // Acesso público a rotas de autenticação e recursos estáticos
            auth.requestMatchers("/auth/**", "/styles/**", "/WEB-INF/**", "/images/**").permitAll();
            auth.requestMatchers("/styles/**", "/scripts/**", "/images/**").permitAll();
            auth.requestMatchers("/login", "/logout").permitAll();
            auth.requestMatchers("/error").permitAll();

            // Acesso mediante Role do User
            auth.requestMatchers("/auth/autenticado").authenticated();
            auth.requestMatchers("/auth/autenticadoCidadao").hasRole("CIDADAO");
            auth.requestMatchers("/auth/autenticadoMunicipio").hasRole("MUNICIPIO");

            // Bloqueia qualquer outra requisição não especificada
            auth.anyRequest().authenticated();
        });

        // Formulário de Login sob httpSecurity
        httpSecurity.formLogin(login -> {
            login.loginPage("/auth/login");           // Página personalizada de login
            login.loginProcessingUrl("/login");       // Endpoint que processa o login
            login.defaultSuccessUrl("/auth/autenticado", true); // Redireciona para autenticado após login bem-sucedido
            login.permitAll();                        // acesso geral à página de login
        });

        // Configuração de logout
        httpSecurity.logout(logout -> {
            logout.logoutUrl("/logout");
            logout.logoutSuccessUrl("/auth/login");
            logout.permitAll();
        });

        // Define o provedor de autenticação
        httpSecurity.authenticationProvider(userAuthenticationProvider);

        return httpSecurity.build();
    }
}