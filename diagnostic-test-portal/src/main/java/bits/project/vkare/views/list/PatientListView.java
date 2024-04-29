package bits.project.vkare.views.list;

import bits.project.vkare.data.Patient;
import bits.project.vkare.services.PatientService;
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
import jakarta.annotation.security.RolesAllowed;
import org.springframework.context.annotation.Scope;

import java.util.List;

@SpringComponent
@Scope("prototype")
@RolesAllowed({"ROLE_ADMIN","ROLE_DESK_STAFF"})
@Route(value = "/patients", layout = MainLayout.class)
@PageTitle("Patients | VKare")
public class PatientListView extends VerticalLayout {
    Grid<Patient> grid = new Grid<>(Patient.class);
    TextField filterText = new TextField();
    NewPatientForm form;
    PatientService service;

    public PatientListView(PatientService service) {
        this.service = service;
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
        form = new NewPatientForm(service.findAllPatients(null), service.findAllStatuses(), List.of("M", "F"));
        form.setWidth("25em");
        form.addSaveListener(this::saveContact); // <1>
        form.addDeleteListener(this::deleteContact); // <2>
        form.addCloseListener(e -> closeEditor()); // <3>
    }

    private void saveContact(NewPatientForm.SaveEvent event) {
        service.savePatient(event.getContact());
        updateList();
        closeEditor();
    }

    private void deleteContact(NewPatientForm.DeleteEvent event) {
        service.deletePatient(event.getContact());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName", "email");
        //grid.setColumns("Test Name", "Test Brief", "Lower Value", "Upper Value");

        grid.addColumn(contact -> contact.getStatus().getName()).setHeader("Status");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editContact(event.getValue()));
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("New Patient");
        addContactButton.addClickListener(click -> addContact());

        var toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editContact(Patient patient) {
        if (patient == null) {
            closeEditor();
        } else {
            form.setContact(patient);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setContact(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addContact() {
        grid.asSingleSelect().clear();
        editContact(new Patient());
    }


    private void updateList() {
        grid.setItems(service.findAllPatients(filterText.getValue()));
    }
}
