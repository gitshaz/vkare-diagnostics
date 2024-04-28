package bits.project.vkare.views.list;

import bits.project.vkare.data.DiagnosticTest;
import bits.project.vkare.services.DiagnosticTestService;
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

@SpringComponent
@Scope("prototype")
@RolesAllowed("ROLE_ADMIN")
@Route(value = "/diag-tests", layout = MainLayout.class)
@PageTitle("Diagnostic Tests | VKare")
public class DiagnosticTestListView extends VerticalLayout {
    Grid<DiagnosticTest> grid = new Grid<>(DiagnosticTest.class);
    TextField filterText = new TextField();
    NewDiagnosticTestForm form;
    DiagnosticTestService service;

    public DiagnosticTestListView(DiagnosticTestService service) {
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
        form = new NewDiagnosticTestForm(service.findAllDiagnosticTests(null));
        form.setWidth("25em");
        form.addSaveListener(this::saveDiagnosticTest); // <1>
        form.addDeleteListener(this::deleteDiagnosticTest); // <2>
        form.addCloseListener(e -> closeEditor()); // <3>
    }

    private void saveDiagnosticTest(NewDiagnosticTestForm.SaveEvent event) {
        service.saveDiagosticTest(event.getDiagnosticTest());
        updateList();
        closeEditor();
    }

    private void deleteDiagnosticTest(NewDiagnosticTestForm.DeleteEvent event) {
        service.deleteDiagnosticTest(event.getDiagnosticTest());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns("testName", "upperValueMale", "lowerValueMale", "preferredValueMale", "upperValueFemale", "lowerValueFemale", "preferredValueFemale", "forMale","forFemale");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editDiagnosticTest(event.getValue()));
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("New Test");
        addContactButton.addClickListener(click -> addDiagTest());

        var toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editDiagnosticTest(DiagnosticTest test) {
        if (test == null) {
            closeEditor();
        } else {
            form.setDiagnosticTest(test);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setDiagnosticTest(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addDiagTest() {
        grid.asSingleSelect().clear();
        editDiagnosticTest(new DiagnosticTest());
    }


    private void updateList() {
        grid.setItems(service.findAllDiagnosticTests(filterText.getValue()));
    }
}
