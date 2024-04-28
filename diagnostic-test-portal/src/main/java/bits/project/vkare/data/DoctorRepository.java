package bits.project.vkare.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Query("select c from Doctor c " +
            "where lower(c.name) like lower(concat('%', :searchTerm, '%')) ")
    List<Doctor> search(@Param("searchTerm") String searchTerm);
}
