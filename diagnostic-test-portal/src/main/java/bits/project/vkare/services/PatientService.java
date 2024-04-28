package bits.project.vkare.services;

import bits.project.vkare.data.Patient;
import bits.project.vkare.data.PatientRepository;
import bits.project.vkare.data.Status;
import bits.project.vkare.data.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final StatusRepository statusRepository;
    private final UserActivationService userActivationService;
    private final UserLoginService userLoginService;

    public List<Patient> findAllPatients(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return patientRepository.findAll();
        } else {
            return patientRepository.search(stringFilter);
        }
    }

    public long countPatients() {
        return patientRepository.count();
    }

    public void deletePatient(Patient patient) {
        patientRepository.delete(patient);
        userLoginService.deleteUser(patient.getEmail());
    }

    public void savePatient(Patient patient) {


        if (patient == null) {
            System.err.println("Patient is null. Are you sure you have connected your form to the application?");
            return;
        }
        patientRepository.save(patient);
        userActivationService.checkForUserActivation(patient);
    }


    public List<Status> findAllStatuses(){
        return statusRepository.findAll();
    }
}
