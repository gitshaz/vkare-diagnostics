package bits.project.vkare.views.list;

import bits.project.vkare.data.Doctor;
import bits.project.vkare.data.OrderStatus;
import bits.project.vkare.data.PackageTestsMapping;
import bits.project.vkare.data.Patient;
import bits.project.vkare.data.TestOrderDetail;
import bits.project.vkare.data.TestOrderSummary;
import bits.project.vkare.data.TestOrPackageModel;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class NewTestOrderForm extends FormLayout {
    ComboBox<Patient> patient = new ComboBox<>("Patient");
    MultiSelectComboBox<TestOrPackageModel> testOrPackgSelector = new MultiSelectComboBox<>("Tests / Packages");
    DateTimePicker orderDate = new DateTimePicker("Order Date");
    ComboBox<OrderStatus> orderStatus = new ComboBox<>("Order Status");
    DateTimePicker lastUpdated = new DateTimePicker("Last Updated On");
    ComboBox<Doctor> primaryDoctor = new ComboBox<>("Primary Doctor");
    TextField primaryDoctorComments = new TextField("Primary Doctor Notes");
    ComboBox<Doctor> secDoctor = new ComboBox<>("Secondary Doctor");
    TextField secDoctorComments = new TextField("Secondary Doctor Notes");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    // Other fields omitted
    Binder<TestOrderSummary> binder = new BeanValidationBinder<>(TestOrderSummary.class);

    public NewTestOrderForm(List<TestOrderSummary> testOrders,
                            List<Patient> patients,
                            List<Doctor> doctors,
                            List<OrderStatus> orderStatuses,
                            List<TestOrPackageModel> testOrPackages) {
        addClassName("new-test-order-form");
        binder.bindInstanceFields(this);

        if (patients != null) {
            patient.setItems(patients);
            patient.setItemLabelGenerator((patient) -> patient.getFirstName() + " " + patient.getLastName());
        }

        if (doctors != null) {
            primaryDoctor.setItems(doctors);
            primaryDoctor.setItemLabelGenerator(Doctor::getName);

            secDoctor.setItems(doctors);
            secDoctor.setItemLabelGenerator(Doctor::getName);
        }

        orderStatus.setItems(orderStatuses);
        orderStatus.setItemLabelGenerator(OrderStatus::getName);

        if (testOrPackages != null) {
            testOrPackgSelector.setItems(testOrPackages);
            testOrPackgSelector.setItemLabelGenerator((p) -> p.isPackage() ? "PKG:"+p.getTestPackage().getName() : p.getTest().getTestName());
        }

        add(patient, orderDate, orderStatus, lastUpdated, primaryDoctor,
                primaryDoctorComments, secDoctor, secDoctorComments, testOrPackgSelector, createButtonsLayout());
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if(binder.isValid()) {
            TestOrderSummary testOrderSummary = binder.getBean();
            testOrderSummary.setLastUpdatedDate(LocalDateTime.now());
            if(!CollectionUtils.isEmpty(testOrPackgSelector.getSelectedItems())) {
                List<TestOrderDetail> testOrderDetails = testOrPackgSelector.getSelectedItems().stream().map(
                        item -> {
                            if (item.isPackage()) {
                                if (!CollectionUtils.isEmpty(item.getTestPackage().getPackageTestsMappings())) {
                                    return item.getTestPackage().getPackageTestsMappings().stream().map(PackageTestsMapping::getDiagnosticTest)
                                            .map(diagTest -> TestOrderDetail.builder().testOrderSummary(testOrderSummary).diagnosticTest(diagTest).isCompleted(false).build())
                                            .toList();
                                }
                                return Collections.<TestOrderDetail>emptyList();
                            } else {
                                return List.of(TestOrderDetail.builder().testOrderSummary(testOrderSummary).diagnosticTest(item.getTest()).isCompleted(false).build());
                            }
                        }
                ).flatMap(List::stream).toList();

                testOrderSummary.setOrderDetails(testOrderDetails);
            }
            fireEvent(new SaveEvent(this, testOrderSummary));
        }
    }


    public void setTestOrderSummary(TestOrderSummary testOrderSummary) {
        testOrPackgSelector.deselectAll();
        if(testOrderSummary != null) {
            if (!CollectionUtils.isEmpty(testOrderSummary.getOrderDetails()))
                testOrPackgSelector.select(testOrderSummary.getOrderDetails().stream().map(od -> TestOrPackageModel.builder().isPackage(false).test(od.getDiagnosticTest()).build()).toList());
            binder.setBean(testOrderSummary);
        }
    }

    // Events
    public static abstract class TestOrderFormEvent extends ComponentEvent<NewTestOrderForm> {
        private final TestOrderSummary testOrderSummary;

        protected TestOrderFormEvent(NewTestOrderForm source,  TestOrderSummary testOrderSummary) {
            super(source, false);
            this.testOrderSummary = testOrderSummary;
        }

        public TestOrderSummary getTestOrderSummary() {
            return testOrderSummary;
        }
    }

    public static class SaveEvent extends TestOrderFormEvent {
        SaveEvent(NewTestOrderForm source, TestOrderSummary testOrderSummary) {
            super(source, testOrderSummary);
        }
    }

    public static class DeleteEvent extends TestOrderFormEvent {
        DeleteEvent(NewTestOrderForm source, TestOrderSummary testOrderSummary) {
            super(source, testOrderSummary);
        }

    }

    public static class CloseEvent extends TestOrderFormEvent {
        CloseEvent(NewTestOrderForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }


}

