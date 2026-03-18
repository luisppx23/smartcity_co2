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
    AuthService authService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = authService.validateLogin(username, password);

        if (user != null) {
            List<GrantedAuthority> roleList = new ArrayList<>();
            /*if(utilizador.isAdmin()) {
                roleList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }*/
            //ALternativa:
            //roleList.add(new SimpleGrantedAuthority("ROLE_" + utilizador.getRole()));

            roleList.add(new SimpleGrantedAuthority("ROLE_USER"));

            return new UsernamePasswordAuthenticationToken(username, password, roleList);
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
