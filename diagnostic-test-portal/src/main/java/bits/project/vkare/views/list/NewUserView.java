package bits.project.vkare.views.list;

import bits.project.vkare.security.SecurityService;
import bits.project.vkare.services.SendMailService;
import bits.project.vkare.services.UserActivationService;
import bits.project.vkare.services.UserLoginService;
import bits.project.vkare.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.context.annotation.Scope;
import org.vaadin.addons.joelpop.changepassword.ChangePassword;
import org.vaadin.addons.joelpop.changepassword.ChangePasswordPanel;
import org.vaadin.addons.joelpop.changepassword.ChangePasswordRule;


@SpringComponent
@Scope("prototype")
@Route(value = "new-user", layout = MainLayout.class)
@RolesAllowed({"ROLE_ADMIN"})
@PageTitle("New User | VKare")
public class NewUserView extends Composite<VerticalLayout> {
    private final ChangePasswordPanel password;
    private final Button clearButton;
    private final Button okButton;

    ComboBox<String> roleBox = new ComboBox<>("Role");

    SecurityService securityService;
    UserLoginService userLoginService;
    SendMailService sendMailService;

    public NewUserView(SecurityService securityService, UserLoginService userLoginService, SendMailService sendMailService) {
        this.userLoginService = userLoginService;
        this.securityService = securityService;
        this.sendMailService = sendMailService;

        password = new ChangePasswordPanel();
        password.setChangePasswordMode(ChangePassword.ChangePasswordMode.ESTABLISH_NEW);
        password.setPasswordRules(ChangePasswordRule.length(6,16));
        password.setUseridRules(ChangePasswordRule.hasSpecifieds("@",1, "Username should be Email-Id"), ChangePasswordRule.hasSpecifieds(".",1), ChangePasswordRule.length(6, 255));

        clearButton = new Button("Clear");
        clearButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        clearButton.addClickListener(event ->  {
            password.reset();
        });

        okButton = new Button("OK");
        okButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        okButton.addClickListener(event -> {
            userLoginService.createNewUser(roleBox.getValue(), password.getDesiredPassword(), password.getUserid());
            sendMailService.sendUserLoginEmail(password.getUserid(), password.getDesiredPassword(), password.getUserid());
            Notification.show("User is Created Successfully");
            password.reset();
        });

        var buttonBar = new HorizontalLayout();
        buttonBar.setWidthFull();
        buttonBar.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        buttonBar.add(clearButton);
        buttonBar.add(okButton);

        roleBox.setItems("LAB_TESTER", "DESK_STAFF");

        var content = getContent();
        content.setSizeUndefined();
        content.add(password);
        content.add(roleBox);
        content.add(buttonBar);
    }
}