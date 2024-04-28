package bits.project.vkare.services;

import bits.project.vkare.data.Doctor;
import bits.project.vkare.data.DoctorRepository;
import bits.project.vkare.data.Status;
import bits.project.vkare.data.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final StatusRepository statusRepository;
    private final UserActivationService userActivationService;
    private final UserLoginService userLoginService;
    public List<Doctor> findAllDoctors(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return doctorRepository.findAll();
        } else {
            return doctorRepository.search(stringFilter);
        }
    }

    public long countDoctors() {
        return doctorRepository.count();
    }

    public void deleteDoctor(Doctor doctor) {
        doctorRepository.delete(doctor);
        userLoginService.deleteUser(doctor.getEmail());
    }

    public void saveDoctor(Doctor doctor) {
        if (doctor == null) {
            System.err.println("Dotor is null. Are you sure you have connected your form to the application?");
            return;
        }
        doctorRepository.save(doctor);
        userActivationService.checkForUserActivation(doctor);
    }


    public List<Status> findAllStatuses(){
        return statusRepository.findAll();
    }
}
