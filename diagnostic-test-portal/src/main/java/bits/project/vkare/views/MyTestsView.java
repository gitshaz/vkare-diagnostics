package bits.project.vkare.views;

import bits.project.vkare.services.PatientService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed("ROLE_PATIENT")
@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Login | VKare Diagnostics")
public class MyTestsView extends VerticalLayout {
    private final PatientService service;

    public MyTestsView(PatientService service) {
        this.service = service;
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        add(getPatientStats());
    }

    private Component getPatientStats() {
        Span stats = new Span(service.countPatients() + " patients");
        stats.addClassNames(
            LumoUtility.FontSize.XLARGE,
            LumoUtility.Margin.Top.MEDIUM);
        return stats;
    }

}
