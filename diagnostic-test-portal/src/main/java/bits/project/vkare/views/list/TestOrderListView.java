package bits.project.vkare.views.list;

import bits.project.vkare.data.Patient;
import bits.project.vkare.data.TestOrderSummary;
import bits.project.vkare.services.DoctorService;
import bits.project.vkare.services.PatientService;
import bits.project.vkare.services.TestOrderSummaryService;
import bits.project.vkare.services.TestPackageService;
import bits.project.vkare.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope("prototype")
@RolesAllowed({"ROLE_ADMIN", "ROLE_DOCTOR", "ROLE_DESK_STAFF", "ROLE_LAB_TESTER"})
@Route(value = "/test-order", layout = MainLayout.class)
@PageTitle("Test Order | VKare")
public class TestOrderListView extends VerticalLayout {
    Grid<TestOrderSummary> grid = new Grid<>(TestOrderSummary.class);
    TextField filterText = new TextField();
    NewTestOrderForm form;
    TestOrderSummaryService service;
    PatientService patientService;
    DoctorService doctorService;

    TestPackageService testPackageService;

    public TestOrderListView(TestOrderSummaryService service, PatientService patientService, DoctorService doctorService, TestPackageService testPackageService) {
        this.service = service;
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.testPackageService = testPackageService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new NewTestOrderForm(service.findAllTestOrders(null), patientService.findAllPatients(null),
                doctorService.findAllDoctors(null), service.findAllStatuses(), testPackageService.getAllPackageAndTests());
        form.setWidth("25em");
        form.addSaveListener(this::saveTestOrder); // <1>
        form.addDeleteListener(this::deleteTestOrder); // <2>
        form.addCloseListener(e -> closeEditor()); // <3>
    }

    private void saveTestOrder(NewTestOrderForm.SaveEvent event) {
        service.saveTestOrderSummary(event.getTestOrderSummary());
        updateList();
        closeEditor();
    }

    private void deleteTestOrder(NewTestOrderForm.DeleteEvent event) {
        service.deleteTestOrder(event.getTestOrderSummary());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns("patient", "orderDate", "orderStatus", "lastUpdatedDate");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editTestOrder(event.getValue()));
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("New Test-Order");
        addContactButton.addClickListener(click -> addTestOrder());

        var toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editTestOrder(TestOrderSummary tos) {
        if (tos == null) {
            closeEditor();
        } else {
            form.setTestOrderSummary(tos);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setTestOrderSummary(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addTestOrder() {
        grid.asSingleSelect().clear();
        editTestOrder(new TestOrderSummary());
    }


    private void updateList() {
        grid.setItems(service.findAllTestOrders(filterText.getValue()));
    }
}
