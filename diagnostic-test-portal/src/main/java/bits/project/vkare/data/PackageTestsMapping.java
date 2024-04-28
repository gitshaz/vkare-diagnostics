package bits.project.vkare.data;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageTestsMapping extends AbstractEntity {

    @NotNull
    @ManyToOne
    private TestPackage testPackage;

    @NotNull
    @ManyToOne
    private DiagnosticTest diagnosticTest;
}
