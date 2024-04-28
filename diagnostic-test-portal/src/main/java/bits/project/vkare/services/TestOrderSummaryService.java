package bits.project.vkare.services;

import bits.project.vkare.data.OrderStatus;
import bits.project.vkare.data.OrderStatusRepository;
import bits.project.vkare.data.TestOrderSummary;
import bits.project.vkare.data.TestOrderSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestOrderSummaryService {

    private final TestOrderSummaryRepository testOrderSummaryRepository;
    private final OrderStatusRepository statusRepository;

    public List<TestOrderSummary> findAllTestOrders(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return testOrderSummaryRepository.findAll();
        } else {
            return testOrderSummaryRepository.search(stringFilter);
        }
    }

    public long countTestOrders() {
        return testOrderSummaryRepository.count();
    }

    public void deleteTestOrder(TestOrderSummary tos) {
        testOrderSummaryRepository.delete(tos);
    }

    public void saveTestOrderSummary(TestOrderSummary tos) {
        if (tos == null) {
            System.err.println("Test Order Summary is null. Are you sure you have connected your form to the application?");
            return;
        }
        testOrderSummaryRepository.save(tos);
    }


    public List<OrderStatus> findAllStatuses(){
        return statusRepository.findAll();
    }
}
