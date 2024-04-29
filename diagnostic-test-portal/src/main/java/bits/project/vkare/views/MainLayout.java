package bits.project.vkare.views;

import bits.project.vkare.security.SecurityService;
import bits.project.vkare.views.list.ChangePasswordView;
import bits.project.vkare.views.list.DiagnosticTestListView;
import bits.project.vkare.views.list.DoctorListView;
import bits.project.vkare.views.list.NewUserView;
import bits.project.vkare.views.list.PatientListView;
import bits.project.vkare.views.list.TestOrderListView;
import bits.project.vkare.views.list.TestPackageListView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainLayout extends AppLayout {
    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("VKare Diagnostics");
        logo.addClassNames(
            LumoUtility.FontSize.LARGE,
            LumoUtility.Margin.MEDIUM);

        String u = securityService.getAuthenticatedUser().getUsername();
        Button logout = new Button("Log out " + u, e -> securityService.logout());

        var header = new HorizontalLayout(new DrawerToggle(), logo, logout);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames(
            LumoUtility.Padding.Vertical.NONE,
            LumoUtility.Padding.Horizontal.MEDIUM);

        addToNavbar(header);
    }

    private void createDrawer() {
        switch (securityService.getUserRole()) {
            case ROLE_ADMIN -> {
                VerticalLayout verticalLayout = new VerticalLayout();
                verticalLayout.add(new RouterLink("Test Packages", TestPackageListView.class));
                verticalLayout.add(new RouterLink("Diagnostic Tests", DiagnosticTestListView.class));
                verticalLayout.add(new RouterLink("Doctors", DoctorListView.class));
                verticalLayout.add(new RouterLink("Test Orders", TestOrderListView.class));
                verticalLayout.add(new RouterLink("Patients", PatientListView.class));
                verticalLayout.add(new RouterLink("New User", NewUserView.class));
                verticalLayout.add(new RouterLink("Change Password", ChangePasswordView.class));
                addToDrawer(verticalLayout);
            }
            case ROLE_DESK_STAFF -> {
                VerticalLayout verticalLayout = new VerticalLayout();
                verticalLayout.add(new RouterLink("Patients", PatientListView.class));
                verticalLayout.add(new RouterLink("Test Orders", TestOrderListView.class));
                verticalLayout.add(new RouterLink("Change Password", ChangePasswordView.class));
                addToDrawer(verticalLayout);
            }
            case ROLE_DOCTOR, ROLE_LAB_TESTER -> {
                VerticalLayout verticalLayout = new VerticalLayout();
                verticalLayout.add(new RouterLink("Test Orders", TestOrderListView.class));
                verticalLayout.add(new RouterLink("Change Password", ChangePasswordView.class));
                addToDrawer(verticalLayout);
            }
            default ->  {
                VerticalLayout verticalLayout = new VerticalLayout();
                verticalLayout.add(new RouterLink("My Tests", MyTestsView.class));
                verticalLayout.add(new RouterLink("Change Password", ChangePasswordView.class));
                addToDrawer(verticalLayout);
            }
        }
    }
}