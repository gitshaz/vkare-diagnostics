package bits.project.vkare.data;

import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestOrderSummary extends AbstractEntity {

    @NotNull
    @ManyToOne
    private Patient patient;

    @NotNull
    private LocalDateTime orderDate;

    @NotNull
    @ManyToOne
    private OrderStatus orderStatus;

    private LocalDateTime lastUpdatedDate;

    @NotNull
    @ManyToOne
    private Doctor primaryDoctor;

    private String primaryDoctorComments;

    @NotNull
    @ManyToOne
    private Doctor secDoctor;

    private String secDoctorComments;

    @OneToMany(mappedBy = "testOrderSummary", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Nullable
    private List<TestOrderDetail> orderDetails;
}
