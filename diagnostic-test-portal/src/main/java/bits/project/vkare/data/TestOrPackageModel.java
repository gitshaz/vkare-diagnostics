package bits.project.vkare.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestOrPackageModel {
    private boolean isPackage;
    private DiagnosticTest test;
    private TestPackage testPackage;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestOrPackageModel obj)) return false;
        if(this.isPackage && obj.isPackage) {
            return this.getTestPackage().getId().longValue() == obj.getTestPackage().getId().longValue();
        }
        else {
            return this.getTest().getId().longValue() == obj.getTest().getId().longValue();
        }
    }
}
