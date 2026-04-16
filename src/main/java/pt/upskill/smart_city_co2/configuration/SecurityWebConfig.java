package pt.upskill.smart_city_co2.configuration;

import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
public class SecurityWebConfig {

    @Autowired
    UserAuthenticationProvider userAuthenticationProvider;

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        firewall.setAllowSemicolon(true);
        return firewall;
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // Desabilita CSRF para simplificar em produção
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        // Configuração de regras de autorização
        httpSecurity.authorizeHttpRequests(auth -> {
            // Requisições de forward (redirecionamentos internos)
            auth.dispatcherTypeMatchers(DispatcherType.FORWARD);

            // Homepage pública
            auth.requestMatchers("/").permitAll();

            // Acesso público a rotas de autenticação e recursos estáticos
            auth.requestMatchers(
                    "/auth/**",
                    "/styles/**",
                    "/WEB-INF/**",
                    "/scripts",
                    "/images/**",
                    "/api/**",
            "/favicon.ico").permitAll();

            // Acesso mediante Role do User - endpoints reais dos controllers
            auth.requestMatchers("/auth/autenticado").authenticated();
            auth.requestMatchers("/auth/homeCidadao").hasRole("CIDADAO");
            auth.requestMatchers("/auth/homeMunicipio").hasRole("MUNICIPIO");

            // Rotas do cidadão
            auth.requestMatchers("/cidadao/**").hasRole("CIDADAO");

            // Rotas do município
            auth.requestMatchers("/municipio/**").hasRole("MUNICIPIO");

            auth.requestMatchers("/**").denyAll();
        });

        // Formulário de Login sob httpSecurity
        httpSecurity.formLogin(login -> {
            login.loginPage("/auth/login");
            login.loginProcessingUrl("/login");
            login.defaultSuccessUrl("/auth/autenticado", true);
            login.permitAll();
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