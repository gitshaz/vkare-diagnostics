package bits.project.vkare.data;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RolePermission extends AbstractEntity {

    @NotNull
    @ManyToOne
    private UserRole userRole;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Permission permission;
}
