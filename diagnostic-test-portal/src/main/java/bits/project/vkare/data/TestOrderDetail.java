package bits.project.vkare.data;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestOrderDetail extends AbstractEntity {

    @NotNull
    @ManyToOne
    private TestOrderSummary testOrderSummary;

    @NotNull
    @ManyToOne
    private DiagnosticTest diagnosticTest;

    @Nullable
    private BigDecimal resultValue;

    @NotNull
    private Boolean isCompleted;
}
