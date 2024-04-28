package bits.project.vkare.security;

import bits.project.vkare.data.UserLogin;
import bits.project.vkare.data.UserLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUserDetailService implements UserDetailsService {

    private final UserLoginRepository userLoginRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserLogin userLogin = userLoginRepository.findFirstByUserName(username);
        if(userLogin == null)
            throw new UsernameNotFoundException("Invalid User");

        return org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password("{noop}"+userLogin.getPassword())
                .roles(userLogin.getUserRole().getRoleName())
                //.authorities(userLogin.getUserRole().getRolePermissions().stream().map(p -> p.getPermission().getPermission()).toArray(String[]::new))
                .build();
    }
}
