package bits.project.vkare.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiagnosticTestRepository extends JpaRepository<DiagnosticTest, Long> {


    @Query("select c from DiagnosticTest c " +
            "where lower(c.testName) like lower(concat('%', :searchTerm, '%')) ")
    List<DiagnosticTest> search(@Param("searchTerm") String searchTerm);
}
