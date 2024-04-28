package bits.project.vkare.services;

import bits.project.vkare.data.StatusRepository;
import bits.project.vkare.data.UserLogin;
import bits.project.vkare.data.UserLoginRepository;
import bits.project.vkare.data.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLoginService {

    private final UserLoginRepository userLoginRepository;
    private final UserRoleRepository userRoleRepository;
    private final StatusRepository statusRepository;
    private static final String ACTIVE_STATUS = "ACTIVE";

    public void deleteUser(String userName) {
        var user = userLoginRepository.findFirstByUserName(userName);
        if(user != null) {
            userLoginRepository.delete(user);
        }
    }

    public void changePassword(String userName, String newPassword) {
        UserLogin userLogin = userLoginRepository.findFirstByUserName(userName);
        if (userLogin != null) {
            userLogin.setPassword(newPassword);
            userLoginRepository.save(userLogin);
        }
    }

    public UserLogin getUser(String userName) {
        return userLoginRepository.findFirstByUserName(userName);
    }

    public void createNewUser(String role, String passwd, String userName) {
        final var status = statusRepository.findAll().stream().filter(s -> ACTIVE_STATUS.equalsIgnoreCase(s.getName())).findFirst().get();
        final var uRole = userRoleRepository.findAll().stream().filter(r -> r.getRoleName().equalsIgnoreCase(role)).findFirst().get();
        UserLogin userLogin = new UserLogin();
        userLogin.setUserRole(uRole);
        userLogin.setUserName(userName);
        userLogin.setPassword(StringUtils.isBlank(passwd) ? RandomStringUtils.randomAlphanumeric(6) : passwd);
        userLogin.setStatus(status);
        userLoginRepository.save(userLogin);
    }

    public void createNewUser(String role, String userName) {
        createNewUser(role, null, userName);
    }

    public void changeStatus(String userName, boolean active) {
        UserLogin userLogin = userLoginRepository.findFirstByUserName(userName);
        if (userLogin != null) {
            final var status = statusRepository.findAll().stream().filter(s -> active ? ACTIVE_STATUS.equalsIgnoreCase(s.getName()) : !ACTIVE_STATUS.equalsIgnoreCase(s.getName()))
                    .findFirst().get();
            userLogin.setStatus(status);
            userLoginRepository.save(userLogin);
        }
    }
}
