package bits.project.vkare.views.list;

import bits.project.vkare.data.DiagnosticTest;
import bits.project.vkare.data.PackageTestsMapping;
import bits.project.vkare.data.TestPackage;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class NewTestPackageForm extends FormLayout {
    TextField name = new TextField("Package Name");
    TextField description = new TextField("Package Description");
    MultiSelectComboBox<DiagnosticTest> diagnosticTests = new MultiSelectComboBox<>("Diagnostic Tests");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    // Other fields omitted
    Binder<TestPackage> binder = new BeanValidationBinder<>(TestPackage.class);

    public NewTestPackageForm(List<TestPackage> testPackages, List<DiagnosticTest> tests) {
        addClassName("new-test-package-form");
        binder.bindInstanceFields(this);

        diagnosticTests.setItems(tests);
        diagnosticTests.setItemLabelGenerator(DiagnosticTest::getTestName);

        add(name, description, diagnosticTests, createButtonsLayout());
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave()); // <1>
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean()))); // <2>
        close.addClickListener(event -> fireEvent(new CloseEvent(this))); // <3>

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid())); // <4>
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if(binder.isValid()) {
            TestPackage testPackage = binder.getBean();
            if(!CollectionUtils.isEmpty(diagnosticTests.getSelectedItems())) {
                List<PackageTestsMapping> packageTestsMappings = diagnosticTests.getSelectedItems().stream().map(item ->
                        PackageTestsMapping.builder().testPackage(testPackage).diagnosticTest(item).build()).toList();
                testPackage.setPackageTestsMappings(packageTestsMappings);
            }
            fireEvent(new SaveEvent(this, testPackage)); // <6>
        }
    }


    public void setPackage(TestPackage testPackage) {
        diagnosticTests.deselectAll();
        if(testPackage != null) {
            if (!CollectionUtils.isEmpty(testPackage.getPackageTestsMappings()))
                diagnosticTests.select(testPackage.getPackageTestsMappings().stream().map(PackageTestsMapping::getDiagnosticTest).toList());
            binder.setBean(testPackage);
        }
    }

    // Events
    public static abstract class TestPackageFormEvent extends ComponentEvent<NewTestPackageForm> {
        private final TestPackage testPackage;

        protected TestPackageFormEvent(NewTestPackageForm source, TestPackage testPackage) {
            super(source, false);
            this.testPackage = testPackage;
        }

        public TestPackage getTestPackage() {
            return testPackage;
        }
    }

    public static class SaveEvent extends TestPackageFormEvent {
        SaveEvent(NewTestPackageForm source, TestPackage testPackage) {
            super(source, testPackage);
        }
    }

    public static class DeleteEvent extends TestPackageFormEvent {
        DeleteEvent(NewTestPackageForm source, TestPackage testPackage) {
            super(source, testPackage);
        }

    }

    public static class CloseEvent extends TestPackageFormEvent {
        CloseEvent(NewTestPackageForm source) {
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

