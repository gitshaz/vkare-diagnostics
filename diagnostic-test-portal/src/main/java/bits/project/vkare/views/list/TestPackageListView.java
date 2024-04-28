package bits.project.vkare.views.list;

import bits.project.vkare.data.TestPackage;
import bits.project.vkare.services.DiagnosticTestService;
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
@RolesAllowed("ROLE_ADMIN")
@Route(value = "/test-packages", layout = MainLayout.class)
@PageTitle("Diagnostic Test Packages | VKare")
public class TestPackageListView extends VerticalLayout {
    Grid<TestPackage> grid = new Grid<>(TestPackage.class);
    TextField filterText = new TextField();
    NewTestPackageForm form;
    TestPackageService service;

    DiagnosticTestService diagnosticTestService;

    public TestPackageListView(TestPackageService service, DiagnosticTestService diagnosticTestService) {
        this.service = service;
        this.diagnosticTestService = diagnosticTestService;
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
        form = new NewTestPackageForm(service.findAllTestPackages(null), diagnosticTestService.findAllDiagnosticTests(null));
        form.setWidth("25em");
        form.addSaveListener(this::saveTestPackage); // <1>
        form.addDeleteListener(this::deleteTestPackage); // <2>
        form.addCloseListener(e -> closeEditor()); // <3>
    }

    private void saveTestPackage(NewTestPackageForm.SaveEvent event) {
        service.saveTestPackage(event.getTestPackage());
        updateList();
        closeEditor();
    }

    private void deleteTestPackage(NewTestPackageForm.DeleteEvent event) {
        service.deleteTestPackage(event.getTestPackage());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns("name", "description");
        //grid.setColumns("Test Name", "Test Brief", "Lower Value", "Upper Value");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editTestPackage(event.getValue()));
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("New Test Package");
        addContactButton.addClickListener(click -> addTestPackage());

        var toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editTestPackage(TestPackage patient) {
        if (patient == null) {
            closeEditor();
        } else {
            form.setPackage(patient);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setPackage(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addTestPackage() {
        grid.asSingleSelect().clear();
        editTestPackage(new TestPackage());
    }


    private void updateList() {
        grid.setItems(service.findAllTestPackages(filterText.getValue()));
    }
}
