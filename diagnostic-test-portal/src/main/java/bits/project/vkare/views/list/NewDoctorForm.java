package bits.project.vkare.views.list;

import bits.project.vkare.data.Doctor;
import bits.project.vkare.data.Status;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class NewDoctorForm extends FormLayout {
    TextField name = new TextField("Doctor Name");
    TextField speciality = new TextField("Speciality");
    EmailField email = new EmailField("Email");

    ComboBox<Status> status = new ComboBox<>("Status");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    // Other fields omitted
    Binder<Doctor> binder = new BeanValidationBinder<>(Doctor.class);

    public NewDoctorForm(List<Doctor> doctors, List<Status> statuses) {
        addClassName("new-doctor-form");
        binder.bindInstanceFields(this);

        status.setItems(statuses);
        status.setItemLabelGenerator(Status::getName);

        add(name,
                speciality,
                email,
                status,
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


    public void setDoctor(Doctor doctor) {
        binder.setBean(doctor); // <1>
    }

    // Events
    public static abstract class DoctorFormEvent extends ComponentEvent<NewDoctorForm> {
        private final Doctor doctor;

        protected DoctorFormEvent(NewDoctorForm source, Doctor doctor) {
            super(source, false);
            this.doctor = doctor;
        }

        public Doctor getDoctor() {
            return doctor;
        }
    }

    public static class SaveEvent extends DoctorFormEvent {
        SaveEvent(NewDoctorForm source, Doctor doctor) {
            super(source, doctor);
        }
    }

    public static class DeleteEvent extends DoctorFormEvent {
        DeleteEvent(NewDoctorForm source, Doctor doctor) {
            super(source, doctor);
        }

    }

    public static class CloseEvent extends DoctorFormEvent {
        CloseEvent(NewDoctorForm source) {
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

