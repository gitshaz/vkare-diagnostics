package bits.project.vkare.views.list;

import bits.project.vkare.data.Patient;
import bits.project.vkare.data.Status;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class NewPatientForm extends FormLayout {
  TextField firstName = new TextField("First Name");
  TextField lastName = new TextField("Last Name");
  TextField email = new TextField("Email");

  ComboBox<String> gender = new ComboBox<String>("Gender");

  DatePicker dob = new DatePicker("BirthDate");

  ComboBox<Status> status = new ComboBox<>("Status");

  Button save = new Button("Save");
  Button delete = new Button("Delete");
  Button close = new Button("Cancel");
  // Other fields omitted
  Binder<Patient> binder = new BeanValidationBinder<>(Patient.class);

  public NewPatientForm(List<Patient> patients, List<Status> statuses, List<String> genders) {
    addClassName("new-patient-form");
    binder.bindInstanceFields(this);

    status.setItems(statuses);
    status.setItemLabelGenerator(Status::getName);

    gender.setItems(genders);

    add(firstName,
            lastName,
            email,
            status,
        gender,
        dob,
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


  public void setContact(Patient patient) {
    binder.setBean(patient); // <1>
  }

  // Events
  public static abstract class ContactFormEvent extends ComponentEvent<NewPatientForm> {
    private Patient patient;

    protected ContactFormEvent(NewPatientForm source, Patient patient) {
      super(source, false);
      this.patient = patient;
    }

    public Patient getContact() {
      return patient;
    }
  }

  public static class SaveEvent extends ContactFormEvent {
    SaveEvent(NewPatientForm source, Patient patient) {
      super(source, patient);
    }
  }

  public static class DeleteEvent extends ContactFormEvent {
    DeleteEvent(NewPatientForm source, Patient patient) {
      super(source, patient);
    }

  }

  public static class CloseEvent extends ContactFormEvent {
    CloseEvent(NewPatientForm source) {
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

