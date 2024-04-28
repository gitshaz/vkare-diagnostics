package bits.project.vkare;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@Theme(value = "flowcrmtutorial")
@PWA(
        name = "VKare Diagnostic Test Portal",
        shortName = "VKare Health",
        offlinePath="offline.html",
        offlineResources = { "images/offline.png" }
)
@EnableAsync
public class DiagnosticTestApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(DiagnosticTestApplication.class, args);
    }

}
