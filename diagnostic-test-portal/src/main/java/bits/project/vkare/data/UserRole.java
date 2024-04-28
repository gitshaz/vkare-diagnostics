package bits.project.vkare.data;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRole  extends AbstractEntity {
    private String roleName;

    @OneToMany(mappedBy = "userRole", fetch = FetchType.EAGER)
    @Nullable
    private List<RolePermission> rolePermissions;
}
