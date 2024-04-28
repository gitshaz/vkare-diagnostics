package bits.project.vkare.services;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class SendMailService {

    @Value("${vkare.sendgrid.apiKey}")
    private String apiKey;

    @Value("${vkare.sendgrid.user}")
    private String apiSenderEmail;

    @Async
    public void sendUserLoginEmail(String user, String paswd, String mailTo) {
        try {
            String body = "<b>Dear " + user + "</b>, <br> You can login to VKare Diagnostics by using your emailId as username and password as: " + paswd;
            sendMail("VKare Diagnostics: Account Details", mailTo, body);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    public void sendChangePasswdEmail(String paswd, String mailTo) {
        try {
            String body = "<b>Dear User</b>, <br> Your password for VKare Diagnostics is changed to: " + paswd;
            sendMail("VKare Diagnostics: Updated password", mailTo, body);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMail(String subject, String mailTo, String htmlBody) throws IOException {
        if (StringUtils.isNotBlank(mailTo)) {
            Email from = new Email(apiSenderEmail);
            Email to = new Email(mailTo);
            Content content = new Content("text/html", htmlBody);
            Mail mail = new Mail(from, subject, to, content);

            SendGrid sg = new SendGrid(apiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            var resp = sg.api(request);
            log.info("SendGrid Response - Status: "+resp.getStatusCode()+". Body: "+resp.getBody());
        }
    }
}
