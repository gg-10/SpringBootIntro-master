
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
import org.springframework.web.util.UriComponentsBuilder;


import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
//import java.net.http.HttpClient;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.io.FileWriter;

import static java.net.http.HttpClient.newHttpClient;

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
//        }

        // Catch block to handle MessagingException
//        catch (jakarta.mail.MessagingException e) {
//            throw new RuntimeException(e);
//        }
    }

//    public void callPostCSV()
//    {
//        RestTemplate restTemplate = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        headers.add("Header", "header1");
//
//
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://foo/api/v3/projects/1/labels")
//                .queryParam("param1", param1)
//                .queryParam("param2", param2);
//
//        HttpEntity<?> entity = new HttpEntity<>(headers);
//
//        HttpEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.GET,
//                entity,
//                String.class);
//    }

//    public void postCallCsv() throws IOException {
//        HttpClient client = HttpClientBuilder.create().build();
//        HttpPost request = new HttpPost("");
//        StringEntity payload = new StringEntity("");
//        request.setEntity(payload);
//        HttpResponse response = client.execute(request);
//        int statusCode = response.getStatusLine().getStatusCode();
//        String responseBody = EntityUtils.toString(response.getEntity());
//        System.out.println("Status Code: " + statusCode);
//        System.out.println("Response Body: " + responseBody);
//    }
//
//    public void csvPostCall() throws IOException{
//        HttpClient client = ;
//
//        String credentials = "elastic" + ":" + "";
//        String auth = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
//
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("http://localhost:5601/api/reporting/generate/csv_searchsource?jobParams=%28browserTimezone%3AAsia%2FCalcutta%2Ccolumns%3A%21%28timestamp%2Cbytes_counter%2Cagent%2Cclientip%29%2CobjectType%3Asearch%2CsearchSource%3A%28fields%3A%21%28%28field%3Atimestamp%2Cinclude_unmapped%3Atrue%29%2C%28field%3Abytes_counter%2Cinclude_unmapped%3Atrue%29%2C%28field%3Aagent%2Cinclude_unmapped%3Atrue%29%2C%28field%3Aclientip%2Cinclude_unmapped%3Atrue%29%29%2Cfilter%3A%21%28%28meta%3A%28field%3Atimestamp%2Cindex%3A%2790943e30-9a47-11e8-b64d-95841ca0b247%27%2Cparams%3A%28%29%29%2Cquery%3A%28range%3A%28timestamp%3A%28format%3Astrict_date_optional_time%2Cgte%3Anow-7d%2Fd%2Clte%3Anow%29%29%29%29%29%2Cindex%3A%2790943e30-9a47-11e8-b64d-95841ca0b247%27%2Cquery%3A%28language%3Akuery%2Cquery%3A%27%27%29%2Csort%3A%21%28%28timestamp%3A%28format%3Astrict_date_optional_time%2Corder%3Adesc%29%29%29%29%2Ctitle%3AReportGen%2Cversion%3A%278.13.0%27%29"))
//                .POST(HttpRequest.BodyPublishers.noBody())
//                .setHeader("kbn-xsrf", "true")
//                .setHeader("authorization", auth)
//                .build();
//
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//    }

//    public void callCSVPost() throws IOException {
//        URL url = new URL("http://localhost:5601/api/reporting/generate/csv_searchsource?jobParams=%28browserTimezone%3AAsia%2FCalcutta%2Ccolumns%3A%21%28timestamp%2Cbytes_counter%2Cagent%2Cclientip%29%2CobjectType%3Asearch%2CsearchSource%3A%28fields%3A%21%28%28field%3Atimestamp%2Cinclude_unmapped%3Atrue%29%2C%28field%3Abytes_counter%2Cinclude_unmapped%3Atrue%29%2C%28field%3Aagent%2Cinclude_unmapped%3Atrue%29%2C%28field%3Aclientip%2Cinclude_unmapped%3Atrue%29%29%2Cfilter%3A%21%28%28meta%3A%28field%3Atimestamp%2Cindex%3A%2790943e30-9a47-11e8-b64d-95841ca0b247%27%2Cparams%3A%28%29%29%2Cquery%3A%28range%3A%28timestamp%3A%28format%3Astrict_date_optional_time%2Cgte%3Anow-7d%2Fd%2Clte%3Anow%29%29%29%29%29%2Cindex%3A%2790943e30-9a47-11e8-b64d-95841ca0b247%27%2Cquery%3A%28language%3Akuery%2Cquery%3A%27%27%29%2Csort%3A%21%28%28timestamp%3A%28format%3Astrict_date_optional_time%2Corder%3Adesc%29%29%29%29%2Ctitle%3AReportGen%2Cversion%3A%278.13.0%27%29");
//        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
//        httpConn.setRequestMethod("POST");
//
//        httpConn.setRequestProperty("kbn-xsrf", "true");
//
//        byte[] message = ("elastic:").getBytes("UTF-8");
//        String basicAuth = DatatypeConverter.printBase64Binary(message);
//        httpConn.setRequestProperty("Authorization", "Basic " + basicAuth);
//
//        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
//                ? httpConn.getInputStream()
//                : httpConn.getErrorStream();
//        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
//        String response = s.hasNext() ? s.next() : "";
//        System.out.println(response);
//    }
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
