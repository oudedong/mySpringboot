package oudedong.project.dto;

import java.util.List;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import oudedong.project.domain.User;
import oudedong.project.vo.AuthorityType;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    
    private Long id;
    private String username;
    private List<String> authorities;

    public UserResponse(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.authorities = user.getAuthorities()
            .stream()
            .map((auth)->toString())
            .toList();
    }
}
