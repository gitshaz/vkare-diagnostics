package bits.project.vkare.views.list;

import bits.project.vkare.data.UserLogin;
import bits.project.vkare.security.SecurityService;
import bits.project.vkare.services.SendMailService;
import bits.project.vkare.services.UserLoginService;
import bits.project.vkare.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
@Route(value = "change-password", layout = MainLayout.class)
@RolesAllowed({"ROLE_ADMIN","ROLE_DESK_STAFF", "ROLE_PATIENT", "ROLE_DOCTOR", "ROLE_LAB_TESTER"})
@PageTitle("Change Password | VKare")
public class ChangePasswordView extends Composite<VerticalLayout> {
    private final ChangePasswordPanel changePassword;
    private final Button clearButton;
    private final Button okButton;

    SecurityService securityService;
    UserLoginService userLoginService;
    SendMailService sendMailService;

    public ChangePasswordView(SecurityService securityService, UserLoginService userLoginService, SendMailService sendMailService) {
        this.userLoginService = userLoginService;
        this.securityService = securityService;
        this.sendMailService = sendMailService;

        changePassword = new ChangePasswordPanel();
        changePassword.setChangePasswordMode(ChangePassword.ChangePasswordMode.CHANGE_FORGOTTEN);
        changePassword.setPasswordRules(ChangePasswordRule.length(6,16));

        clearButton = new Button("Clear");
        clearButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        clearButton.addClickListener(event -> changePassword.reset());

        okButton = new Button("OK");
        okButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        okButton.addClickListener(event -> {
            if (changePassword.isValid()) {
                var newPasswd = changePassword.getDesiredPassword();
                userLoginService.changePassword(securityService.getAuthenticatedUser().getUsername(),newPasswd);
                sendMailService.sendChangePasswdEmail(newPasswd, securityService.getAuthenticatedUser().getUsername());
                Notification.show("Password is Updated Successfully");
                okButton.getUI().ifPresent(ui -> ui.navigate(""));
            }
        });

        var buttonBar = new HorizontalLayout();
        buttonBar.setWidthFull();
        buttonBar.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        buttonBar.add(clearButton);
        buttonBar.add(okButton);

        var content = getContent();
        content.setSizeUndefined();
        content.add(changePassword);
        content.add(buttonBar);
    }
}