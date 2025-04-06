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

        System.out.println("===========================================================");
        System.out.println("loadUserByUsername 호출됨: " + username);
        System.out.println("비밀번호: " + user.getPassword());
        System.out.println("권한: " + user.getAuthorities());

        return new UserDetail(user);
    }
    
}
