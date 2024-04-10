
package ca.pledgetovote.controllers;

import ca.pledgetovote.model.EmailDetails;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.mail.*;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import org.springframework.http.ResponseEntity;
import java.io.FileWriter;

// Annotation
@Service
// Class
// Implementing EmailService interface
public class EmailServiceImpl implements EmailService {

    @Autowired private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}") private String sender;


    // To send a simple email
    public String sendSimpleMail(EmailDetails details)
    {

        // Try block to check for exceptions
        try {

            // Creating a simple mail message
            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();

            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            // Sending the mail
            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            return "Error while Sending Mail";
        }
    }


    // To send an email with attachment
    public String
    sendMailWithAttachment(EmailDetails details) throws MessagingException {
        testApi();
//        sendPostRequest();
        // Creating a mime message
        MimeMessage mimeMessage
                = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

//        try {

        // Setting multipart as true for attachments to
        // be send
        mimeMessageHelper
                = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(sender);
        mimeMessageHelper.setTo(details.getRecipient());
        mimeMessageHelper.setText(details.getMsgBody());
        mimeMessageHelper.setSubject(
                details.getSubject());

        // Adding the attachment
        FileSystemResource file
                = new FileSystemResource(
                new File(details.getAttachment()));

        mimeMessageHelper.addAttachment(
                file.getFilename(), file);

        // Sending the mail
        javaMailSender.send(mimeMessage);
        System.out.println("Mail sent successfully");
        return ("Mail sent Successfully");
    }
        public void sendPostRequest() {
            String baseUrl = "http://localhost:5601/api/reporting/generate/csv_searchsource";

            String jobParams = "(browserTimezone:Asia/Calcutta,columns:!(timestamp,bytes_counter,agent,clientip),objectType:search,searchSource:(fields:!(" +
                    "(field:timestamp,include_unmapped:true)," +
                    "(field:bytes_counter,include_unmapped:true)," +
                    "(field:agent,include_unmapped:true)," +
                    "(field:clientip,include_unmapped:true)),filter:!(" +
                    "(meta:(field:timestamp,index:'79043e30-9a47-11e8-b64d-95841ca0b247',params:())),query:(range:(timestamp:(format:strict_date_optional_time,gte:now-7d/d,lte:now)))),index:'79043e30-9a47-11e8-b64d-95841ca0b247',query:(language:kuery,query:''),sort:!(timestamp:(format:strict_date_optional_time,order:desc)))";
//            ,title:ReportGen,version:'8.13.0'

            String encodedJobParams = null;
            try {
                encodedJobParams = URLEncoder.encode(jobParams, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String urlWithParams = baseUrl + "?jobParams=" + encodedJobParams;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("kbn-xsrf", "true");
            headers.setBasicAuth("elastic", "7iPQKeQMOqTSxB-7uqbT");

            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response = restTemplate.exchange(
                    urlWithParams,
                    HttpMethod.POST,
                    requestEntity,
                    String.class);

            System.out.println("Response: " + response.getBody());
        }

    public void testApi() {
        RestTemplate restTemplate = new RestTemplate();

        String apiUrl = "https://world.openpetfoodfacts.org/api/v0/product/20106836.json";

        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

        System.out.println("Response: " + response.getBody());

        saveResponseToFile(response.getBody());
    }

    private void saveResponseToFile(String responseBody) {
        String filePath = "D:\\IntellijProjects\\SpringBootIntro-master\\response.json";
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(responseBody);
            System.out.println("Response saved to: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
