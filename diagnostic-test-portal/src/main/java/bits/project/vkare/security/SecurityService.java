package bits.project.vkare.security;

import bits.project.vkare.constants.Role;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {

    private final AuthenticationContext authenticationContext;

    public SecurityService(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
    }

    public UserDetails getAuthenticatedUser() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class).get();
    }

    public Role getUserRole() {
        return Role.valueOf(getAuthenticatedUser().getAuthorities().stream().findFirst().get().getAuthority());
    }

    public void logout() {
        authenticationContext.logout();
    }
}