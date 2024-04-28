package bits.project.vkare.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoginRepository extends JpaRepository<UserLogin, Long> {
    UserLogin findFirstByUserName(String userName);
}
