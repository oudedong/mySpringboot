package oudedong.project.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import oudedong.project.dto.RegisterRequest;
import oudedong.project.dto.UserResponse;
import oudedong.project.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class UserApiController {
    
    private final UserService userService;

    @PostMapping("/user")
    public ResponseEntity<UserResponse> register(HttpServletRequest httpRequest){   

        RegisterRequest request = new RegisterRequest(
            httpRequest.getParameter("username"),
            httpRequest.getParameter("password")
        );
        UserResponse response = userService.register(request);

        return ResponseEntity
            .status(HttpStatus.FOUND)
            .header(HttpHeaders.LOCATION, "/main")
            .body(response);
    }
}
