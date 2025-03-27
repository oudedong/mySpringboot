package oudedong.project.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import oudedong.project.domain.User;
import oudedong.project.dto.RegisterRequest;
import oudedong.project.dto.UserResponse;
import oudedong.project.repository.UserRepo;
import oudedong.project.vo.AuthorityType;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder encoder;

    public UserResponse register(RegisterRequest request){
        
        User registered = new User();

        registered.setUsername(request.getUsername());
        registered.setPassword(encoder.encode(request.getPassword()));
        registered.addAuthority(AuthorityType.ROLE_USER);
        registered = userRepo.save(registered);

        return new UserResponse(registered);
    }
}
