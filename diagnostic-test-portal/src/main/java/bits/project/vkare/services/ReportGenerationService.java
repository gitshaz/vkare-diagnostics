package bits.project.vkare.services;

import bits.project.vkare.data.OrderStatus;
import bits.project.vkare.data.TestOrderSummary;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

@SpringBootApplication
@RestController
public class ReportGenerationService {

    public StreamResource showPdf(byte[] pdfBytes, String filename) {
        StreamResource resource = new StreamResource(filename, () -> new ByteArrayInputStream(pdfBytes));
        resource.setContentType("content/octet-stream");
        resource.setCacheTime(0);
        return resource;
    }

    public ResponseEntity<byte[]> generatePdf(TestOrderSummary summary) {
        try {
            // Load HTML content
            Resource resource = new ClassPathResource("Report.html");
            String reportTemplateHtml = new String(resource.getInputStream().readAllBytes());
            reportTemplateHtml = StringUtils.replace(reportTemplateHtml, "{{Patient}}", summary.getPatient().getFirstName()+" "+summary.getPatient().getLastName());
            reportTemplateHtml = StringUtils.replace(reportTemplateHtml, "{{Age}}", String.valueOf(Period.between(summary.getPatient().getDob(), LocalDate.now()).getYears()));
            reportTemplateHtml = StringUtils.replace(reportTemplateHtml, "{{Report-Date}}",summary.getLastUpdatedDate().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")));
            reportTemplateHtml = StringUtils.replace(reportTemplateHtml, "{{ReportId}}","V"+StringUtils.leftPad(summary.getId().toString(), 6, '0'));
            reportTemplateHtml = StringUtils.replace(reportTemplateHtml, "{{Status}}", Optional.ofNullable(summary.getOrderStatus()).map(OrderStatus::getName).orElse("N/A"));
            reportTemplateHtml = StringUtils.replace(reportTemplateHtml, "{{Doctor1}}",summary.getPrimaryDoctor().getName());
            reportTemplateHtml = StringUtils.replace(reportTemplateHtml, "{{Doctor2}}", summary.getSecDoctor().getName());
            reportTemplateHtml = StringUtils.replace(reportTemplateHtml, "{{Gender}}", summary.getPatient().getGender());
            reportTemplateHtml = StringUtils.replace(reportTemplateHtml, "{{DoctorRemarks}}", (StringUtils.isNotBlank(summary.getPrimaryDoctorComments()) ? summary.getPrimaryDoctorComments() + "." + summary.getSecDoctorComments() : summary.getSecDoctorComments()));


            StringBuilder trThBuilder = new StringBuilder();
            for(int i = 0; i< Objects.requireNonNull(summary.getOrderDetails()).size(); i++) {

                var ordDetail = summary.getOrderDetails().get(i);
                boolean isMale = "M".equalsIgnoreCase(summary.getPatient().getGender());

                String testName = ordDetail.getDiagnosticTest().getTestName();
                BigDecimal minValue = isMale ? ordDetail.getDiagnosticTest().getLowerValueMale() : ordDetail.getDiagnosticTest().getLowerValueFemale();
                BigDecimal maxValue = isMale ?ordDetail.getDiagnosticTest().getUpperValueMale() : ordDetail.getDiagnosticTest().getUpperValueFemale();
                BigDecimal recValue = isMale ?ordDetail.getDiagnosticTest().getPreferredValueMale() : ordDetail.getDiagnosticTest().getPreferredValueFemale();
                BigDecimal actValue = BigDecimal.valueOf(85.5);

                String trth = " <tr>\n" +
                        "        <th>" + testName + "</th>\n" +
                        "        <th>" + minValue + "</th>\n" +
                        "        <th>" + maxValue + "</th>\n" +
                        "        <th>" + recValue + "</th>\n" +
                        "        <th>" + actValue + "</th>\n" +
                        "    </tr>";

                trThBuilder.append(trth).append("\n");
            }

            reportTemplateHtml = StringUtils.replace(reportTemplateHtml, "{{TRTH_TESTRESULTS}}", trThBuilder.toString());
            reportTemplateHtml = StringUtils.replace(reportTemplateHtml, "{{TEST_COST}}", summary.getOrderDetails().stream().map(o -> o.getDiagnosticTest().getTestCost()).toList().stream().reduce(BigDecimal.ZERO, BigDecimal::add).toString());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.writeBytes(reportTemplateHtml.getBytes());

            // Set response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_HTML);
            headers.setContentDispositionFormData("filename", "lab_test_report.html");

            // Return PDF as byte array
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}