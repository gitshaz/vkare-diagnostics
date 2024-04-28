package bits.project.vkare.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    UserRole findFirstByRoleName(String roleName);
}
