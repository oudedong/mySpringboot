package oudedong.project.configure;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import oudedong.project.dto.UserDetail;
import oudedong.project.service.UserDetailService;
import oudedong.project.vo.AuthorityType;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public WebSecurityCustomizer securityConfig(){
        return (web) -> {
            web.ignoring().requestMatchers(
                PathRequest.toH2Console()
            );
        };
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, ProviderManager providerManager) throws Exception {
        http.authorizeHttpRequests((request) -> {
            request.requestMatchers(
                new AntPathRequestMatcher("/login"),
                new AntPathRequestMatcher("/register"),
                new AntPathRequestMatcher("/main/**"),
                new AntPathRequestMatcher("/api/user/**"),
                new AntPathRequestMatcher("/css/**"),
                new AntPathRequestMatcher("/js/**")
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
                .defaultSuccessUrl("/main")
                .failureHandler(new CustomFailureHandler());
        })
        .authenticationManager(providerManager) //기본적으로 dao만 쓰므로 명시적으로 등록해줘야됨!!!!!!
        .logout((logout) -> {
            logout
                .logoutSuccessUrl("/main")
                .invalidateHttpSession(true);
        })
        .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
    @Bean
    public ProviderManager providerManager(DaoAuthenticationProvider provider, CheckAdminProvider adminProvider){
        return new ProviderManager(adminProvider, provider);
    }
    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailService service, BCryptPasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(service);
        provider.setPasswordEncoder(encoder);
        return provider;
    }
    @Bean
    public CheckAdminProvider checkAdminProvider(){
        return new CheckAdminProvider("test", "test123");
    }
}

class CheckAdminProvider implements AuthenticationProvider{

    private String adminName;
    private String adminPassword;

    public CheckAdminProvider(String adminName, String adminPassword){
        this.adminName = adminName;
        this.adminPassword = adminPassword;
    }

    @Override
    public Authentication authenticate(Authentication authentication){

        String username = authentication.getName();
        String password = (String)authentication.getCredentials();

        System.out.println("로그인 이름,비번:" + username + "/" + password);
        System.out.println("어드민 이름,비번:" + adminName + "/" + adminPassword);

        if(!username.equals(adminName) || !password.equals(adminPassword)){
            throw new BadCredentialsException("not admin!"); //실패시 예외를 던저야야 다른 provider에서 인증을 재시도함!!!!!!!
        }
        return new UsernamePasswordAuthenticationToken(new UserDetail(0L, username, "", List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))), null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

@Component
class CustomFailureHandler extends SimpleUrlAuthenticationFailureHandler  {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String errorMessage;
        if (exception instanceof BadCredentialsException) {
            errorMessage = "아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해 주세요.";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            errorMessage = "내부적으로 발생한 시스템 문제로 인해 요청을 처리할 수 없습니다. 관리자에게 문의하세요.";
        } else if (exception instanceof UsernameNotFoundException) {
            errorMessage = "계정이 존재하지 않습니다. 회원가입 진행 후 로그인 해주세요.";
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            errorMessage = "인증 요청이 거부되었습니다. 관리자에게 문의하세요.";
        } else {
            errorMessage = "알 수 없는 이유로 로그인에 실패하였습니다 관리자에게 문의하세요.";
        }

        errorMessage = URLEncoder.encode(errorMessage, "UTF-8");
        setDefaultFailureUrl("/login?error=true&exception=" + errorMessage);

        super.onAuthenticationFailure(request, response, exception);
    }
}