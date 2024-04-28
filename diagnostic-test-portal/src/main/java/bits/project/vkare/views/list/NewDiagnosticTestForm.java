package bits.project.vkare.views.list;

import bits.project.vkare.data.DiagnosticTest;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class NewDiagnosticTestForm extends FormLayout {
    TextField testName = new TextField("Test Name");
    BigDecimalField upperValueMale = new BigDecimalField("Upper Value (Male)");
    BigDecimalField lowerValueMale = new BigDecimalField("Lower Value (Male)");
    BigDecimalField preferredValueMale = new BigDecimalField("Pref Value (Male)");

    BigDecimalField upperValueFemale = new BigDecimalField("Upper Value (Female)");
    BigDecimalField lowerValueFemale = new BigDecimalField("Lower Value (Female)");
    BigDecimalField preferredValueFemale = new BigDecimalField("Pref Value (Female)");

    Checkbox forMale = new Checkbox("For Male");
    Checkbox forFemale = new Checkbox("For Female");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    // Other fields omitted
    Binder<DiagnosticTest> binder = new BeanValidationBinder<>(DiagnosticTest.class);

    public NewDiagnosticTestForm(List<DiagnosticTest> diagnosticTests) {
        addClassName("new-diag-test-form");
        binder.bindInstanceFields(this);

        add(testName,
                upperValueMale,
                lowerValueMale,
                preferredValueMale,
                upperValueFemale,
                lowerValueFemale,
                preferredValueFemale,
                forMale,
                forFemale,
                createButtonsLayout());
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
            fireEvent(new SaveEvent(this, binder.getBean())); // <6>
        }
    }


    public void setDiagnosticTest(DiagnosticTest test) {
        binder.setBean(test); // <1>
    }

    // Events
    public static abstract class DiagnosticTestFormEvent extends ComponentEvent<NewDiagnosticTestForm> {
        private final DiagnosticTest test;

        protected DiagnosticTestFormEvent(NewDiagnosticTestForm source, DiagnosticTest test) {
            super(source, false);
            this.test = test;
        }

        public DiagnosticTest getDiagnosticTest() {
            return test;
        }
    }

    public static class SaveEvent extends DiagnosticTestFormEvent {
        SaveEvent(NewDiagnosticTestForm source, DiagnosticTest test) {
            super(source, test);
        }
    }

    public static class DeleteEvent extends DiagnosticTestFormEvent {
        DeleteEvent(NewDiagnosticTestForm source, DiagnosticTest test) {
            super(source, test);
        }

    }

    public static class CloseEvent extends DiagnosticTestFormEvent {
        CloseEvent(NewDiagnosticTestForm source) {
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

