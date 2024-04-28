package bits.project.vkare.data;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatus extends AbstractEntity {
    private String name;

    @Override
    public String toString() {
        return this.name;
    }
}
