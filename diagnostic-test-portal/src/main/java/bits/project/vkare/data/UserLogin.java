package bits.project.vkare.data;

import jakarta.persistence.Entity;
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
public class UserLogin extends AbstractEntity {

    @NotNull
    private String userName;

    @NotNull
    private String password;

    @NotNull
    @ManyToOne
    private Status status;

    @NotNull
    @ManyToOne
    private UserRole userRole;
}
