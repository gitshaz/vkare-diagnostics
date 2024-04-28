package bits.project.vkare.data;

import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
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
public class TestPackage extends AbstractEntity {
    @NotBlank
    private String name;

    @Nullable
    private String description;

    @OneToMany(mappedBy = "testPackage", fetch = FetchType.EAGER, cascade= CascadeType.ALL, orphanRemoval = true)
    @Nullable
    private List<PackageTestsMapping> packageTestsMappings;
}
