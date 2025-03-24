package oudedong.project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {
    
    private String username;
    private String password;
    
    public RegisterRequest(String username, String password){
        this.username = username;
        this.password = password;
    }
}
