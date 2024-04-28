package bits.project.vkare.views.list;

import bits.project.vkare.data.Doctor;
import bits.project.vkare.services.DoctorService;
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
@RolesAllowed("ROLE_ADMIN")
@Route(value = "/doctors", layout = MainLayout.class)
@PageTitle("Doctors | VKare")
public class DoctorListView extends VerticalLayout {
    Grid<Doctor> grid = new Grid<>(Doctor.class);
    TextField filterText = new TextField();
    NewDoctorForm form;
    DoctorService service;

    public DoctorListView(DoctorService service) {
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
        form = new NewDoctorForm(service.findAllDoctors(null), service.findAllStatuses());
        form.setWidth("25em");
        form.addSaveListener(this::saveDoctor); // <1>
        form.addDeleteListener(this::deleteDoctor); // <2>
        form.addCloseListener(e -> closeEditor()); // <3>
    }

    private void saveDoctor(NewDoctorForm.SaveEvent event) {
        service.saveDoctor(event.getDoctor());
        updateList();
        closeEditor();
    }

    private void deleteDoctor(NewDoctorForm.DeleteEvent event) {
        service.deleteDoctor(event.getDoctor());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns("name", "speciality");

        grid.addColumn(contact -> contact.getStatus().getName()).setHeader("Status");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editDoctor(event.getValue()));
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("New Doctor");
        addContactButton.addClickListener(click -> addDoctor());

        var toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editDoctor(Doctor doctor) {
        if (doctor == null) {
            closeEditor();
        } else {
            form.setDoctor(doctor);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setDoctor(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addDoctor() {
        grid.asSingleSelect().clear();
        editDoctor(new Doctor());
    }


    private void updateList() {
        grid.setItems(service.findAllDoctors(filterText.getValue()));
    }
}
