package bits.project.vkare.services;

import bits.project.vkare.data.Doctor;
import bits.project.vkare.data.Patient;
import bits.project.vkare.data.Status;
import bits.project.vkare.data.UserLogin;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserActivationService {

    private final UserLoginService userLoginService;

    private final SendMailService sendMailService;

    public void checkForUserActivation(Patient patient) {
        checkForUserActivationInternal("PATIENT", patient.getFirstName() + " " + patient.getLastName(), patient.getEmail(), patient.getStatus());
    }

    public void checkForUserActivation(Doctor doctor) {
        checkForUserActivationInternal("DOCTOR", doctor.getName(), doctor.getEmail(), doctor.getStatus());
    }

    public void checkForUserActivationInternal(String role, String receipient, String email, Status profileStatus) {
        boolean sendMail = false;
        if (!StringUtils.isEmpty(email)) {
            if ("ACTIVE".equalsIgnoreCase(profileStatus.getName())) {
                //Creates a new account or updates account status as required
                UserLogin userLogin = userLoginService.getUser(email);
                if (userLogin != null) {
                    if (userLogin.getStatus() == null || !"ACTIVE".equalsIgnoreCase(userLogin.getStatus().getName())) {
                        //Activate (or Reactivate) the user
                        userLoginService.changeStatus(email, true);
                        sendMail = true;
                    }
                } else {
                    userLoginService.createNewUser(role, email);
                    sendMail = true;
                }
                if (sendMail) {
                    sendMailService.sendUserLoginEmail(receipient, userLogin.getPassword(), email);
                }
            } else {
                userLoginService.changeStatus(email, false);
            }
        }
    }
}
