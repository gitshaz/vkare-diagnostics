package bits.project.vkare.security;

import bits.project.vkare.data.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPermissionService {

    private final UserRoleRepository userRoleRepository;

    public List<String> getPermissions(String role) {
        return userRoleRepository.findFirstByRoleName(role).getRolePermissions().stream().map(rp -> rp.getPermission().getPermission()).toList();
    }
}
