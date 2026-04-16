package pt.upskill.smart_city_co2.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import pt.upskill.smart_city_co2.entities.User;
import pt.upskill.smart_city_co2.services.AuthService;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    AuthService authService; //responsável pela validação de credenciais

    //metodo da Classe Authentication (o framework de autenticação do Spring)
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //username e password do objeto authentication
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        // Valida as credenciais e retorna o user se forem corretas
        User user = authService.validarLogin(username, password);

        if (user != null) {
            // Lista de roles de user - permissões
            List<GrantedAuthority> roleList = new ArrayList<>();

            // Adiciona role específico baseado no tipo de user
            if ("cidadao".equals(user.getTipo())) {
                roleList.add(new SimpleGrantedAuthority("ROLE_CIDADAO"));
            } else if ("municipio".equals(user.getTipo())) {
                roleList.add(new SimpleGrantedAuthority("ROLE_MUNICIPIO"));
            }

            // Por enquanto, atribui role USER para todos os users autenticados
            roleList.add(new SimpleGrantedAuthority("ROLE_USER"));

            // Retorna token de autenticação com user, password e roles
            return new UsernamePasswordAuthenticationToken(user, password, roleList);
        }

        // Retorna null se autenticação falhar
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // Este provider só processa autenticações do tipo UsernamePasswordAuthenticationToken
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}