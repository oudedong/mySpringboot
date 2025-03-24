package oudedong.project.configure;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import oudedong.project.service.UserDetailService;
import oudedong.project.vo.AuthorityType;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public WebSecurityCustomizer securityConfig(){
        return (web) -> {
            web.ignoring().requestMatchers(
                PathRequest.toH2Console(),
                new AntPathRequestMatcher("/static/**")
            );
        };
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((request) -> {
            request.requestMatchers(
                new AntPathRequestMatcher("/login"),
                new AntPathRequestMatcher("/register"),
                new AntPathRequestMatcher("/main/**"),
                new AntPathRequestMatcher("/api/user/**")
            )
            .permitAll()
            .requestMatchers(
                new AntPathRequestMatcher("/console/**")
            )
            .hasAnyAuthority(AuthorityType.ROLE_ADMIN.name())
            .anyRequest()
            .authenticated();
        })
        .formLogin((login) -> {
            login
                .loginPage("/login")
                .defaultSuccessUrl("/main");
        })
        .logout((logout) -> {
            logout
                .logoutSuccessUrl("/main")
                .invalidateHttpSession(true);
        })
        .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(BCryptPasswordEncoder encoder, UserDetailService service){
        
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        CheckAdminProvider adminProvider = new CheckAdminProvider();

        provider.setUserDetailsService(service);
        provider.setPasswordEncoder(encoder);

        return new ProviderManager(provider, adminProvider);
    }
    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
}

@Component
class CheckAdminProvider implements AuthenticationProvider{

    @Value("${admin.name}")
    private String adminName;
    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public Authentication authenticate(Authentication authentication) throws BadCredentialsException{

        String username = authentication.getName();
        String password = (String)authentication.getCredentials();

        if(!username.equals(adminName) || !password.equals(adminPassword)){
            throw new BadCredentialsException("not a admin");
        }
        return new UsernamePasswordAuthenticationToken(username, null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
