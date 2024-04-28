package bits.project.vkare.views.list;

import bits.project.vkare.views.MainLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope("prototype")
@RolesAllowed({"ROLE_ADMIN", "ROLE_DOCTOR", "ROLE_DESK_STAFF", "ROLE_LAB_TESTER", "ROLE_PATIENT"})
@Route(value = "", layout = MainLayout.class)
@PageTitle("Home | VKare")
public class HomeView extends VerticalLayout {

    private Image image;

    public HomeView() {
        addClassName("home-view");
        setSizeFull();
        add(getContent());
    }

    private HorizontalLayout getContent() {
        image = new Image("images/vkare.png", "Welcome");
        HorizontalLayout content = new HorizontalLayout(image);
        content.setFlexGrow(1, image);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }
}
