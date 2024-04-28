package bits.project.vkare.services;

import bits.project.vkare.data.DiagnosticTest;
import bits.project.vkare.data.DiagnosticTestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiagnosticTestService {

    private final DiagnosticTestRepository diagnosticTestRepository;
    public List<DiagnosticTest> findAllDiagnosticTests(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return diagnosticTestRepository.findAll();
        } else {
            return diagnosticTestRepository.search(stringFilter);
        }
    }

    public long countDiagnosticTests() {
        return diagnosticTestRepository.count();
    }

    public void deleteDiagnosticTest(DiagnosticTest test) {
        diagnosticTestRepository.delete(test);
    }

    public void saveDiagosticTest(DiagnosticTest test) {
        if (test == null) {
            System.err.println("DiagnosticTest is null. Are you sure you have connected your form to the application?");
            return;
        }
        diagnosticTestRepository.save(test);
    }
}
