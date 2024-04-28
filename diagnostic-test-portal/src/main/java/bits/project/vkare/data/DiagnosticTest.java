package bits.project.vkare.data;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticTest extends AbstractEntity {

    @NotNull
    private String testName;

    @Nullable
    private BigDecimal upperValueMale;

    @Nullable
    private BigDecimal lowerValueMale;

    @NotNull
    private BigDecimal preferredValueMale = BigDecimal.ZERO;

    @Nullable
    private BigDecimal upperValueFemale;

    @Nullable
    private BigDecimal lowerValueFemale;

    @NotNull
    private BigDecimal preferredValueFemale = BigDecimal.ZERO;

    @NotNull
    private Boolean forMale;
    @NotNull
    private Boolean forFemale;

}
