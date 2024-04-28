package bits.project.vkare.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestPackageRepository extends JpaRepository<TestPackage, Long> {

    @Query("select c from TestPackage c " +
            "where lower(c.name) like lower(concat('%', :searchTerm, '%')) ")
    List<TestPackage> search(@Param("searchTerm") String searchTerm);
}
