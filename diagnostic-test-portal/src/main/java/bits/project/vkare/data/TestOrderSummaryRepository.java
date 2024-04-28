package bits.project.vkare.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestOrderSummaryRepository extends JpaRepository<TestOrderSummary, Long> {

    @Query("select c from TestOrderSummary c " +
            "where lower(c.patient.firstName) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.patient.lastName) like lower(concat('%', :searchTerm, '%'))")
    List<TestOrderSummary> search(@Param("searchTerm") String searchTerm);
}
