package bits.project.vkare.services;

import bits.project.vkare.data.DiagnosticTestRepository;
import bits.project.vkare.data.TestPackage;
import bits.project.vkare.data.TestPackageRepository;
import bits.project.vkare.data.TestOrPackageModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestPackageService {

    private final TestPackageRepository testPackageRepository;
    private final DiagnosticTestRepository diagnosticTestRepository;

    public List<TestPackage> findAllTestPackages(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return testPackageRepository.findAll();
        } else {
            return testPackageRepository.search(stringFilter);
        }
    }

    public List<TestOrPackageModel> getAllPackageAndTests() {
        List<TestOrPackageModel> packagesAndTests = new ArrayList<>();
        var allPackages = testPackageRepository.findAll();
        var allTests = diagnosticTestRepository.findAll();
        if (!CollectionUtils.isEmpty(allPackages))
            packagesAndTests.addAll(allPackages.stream().map(testPackage -> new TestOrPackageModel( true, null, testPackage)).toList());
        if (!CollectionUtils.isEmpty(allPackages))
            packagesAndTests.addAll(allTests.stream().map(t -> new TestOrPackageModel(false, t, null)).toList());
        return packagesAndTests;
    }

    public long countTestPackages() {
        return testPackageRepository.count();
    }

    public void deleteTestPackage(TestPackage testPackage) {
        testPackageRepository.delete(testPackage);
    }

    public void saveTestPackage(TestPackage testPackage) {
        if (testPackage == null) {
            System.err.println("TestPackage is null. Are you sure you have connected your form to the application?");
            return;
        }
        testPackageRepository.save(testPackage);
    }
}
