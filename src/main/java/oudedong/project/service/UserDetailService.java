package oudedong.project.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import oudedong.project.domain.User;
import oudedong.project.dto.UserDetail;
import oudedong.project.repository.UserRepo;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService{

    private final UserRepo userRepo;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("cant find user: " + username));

        return new UserDetail(user);
    }
    
}
