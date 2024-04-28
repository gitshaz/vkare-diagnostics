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
public class Doctor extends AbstractEntity {

    @NotNull
    private String name;
    @NotNull
    private String speciality;
    @NotNull
    private String email;

    @NotNull
    @ManyToOne
    private Status status;

    @Override
    public String toString() {
        return this.name;
    }
}
