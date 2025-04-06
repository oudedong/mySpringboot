package oudedong.project.dto;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import oudedong.project.domain.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetail implements UserDetails{

    private Long id;
    private String username;
    private String password;
    private List<SimpleGrantedAuthority> authorities;

    public UserDetail(User user){
        username = user.getUsername();
        id = user.getId();
        password = user.getPassword();
        authorities = user
            .getAuthorities()
            .stream()
            .map((role)->new SimpleGrantedAuthority(role.toString()))
            .toList();
    }
}
