package ca.pledgetovote.controllers;

import ca.pledgetovote.model.EmailDetails;
import ca.pledgetovote.controllers.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


// Annotation
@RestController
// Class
public class EmailController {

    @Autowired private EmailService emailService;

    // Sending a simple Email
    @RequestMapping("/sendMail")
    public String sendEmailCall()
    {
        EmailDetails details = new EmailDetails();
        details.setRecipient("avibarca10@gmail.com");
        details.setSubject("Sending automated email");
        details.setMsgBody("Hola! - Spring");
        return(sendMail(details));
    }


    public String
    sendMail(@RequestBody EmailDetails details)
    {

//        ("Hitting here");

        String status
                = emailService.sendSimpleMail(details);

        return status;
    }

    // Sending email with attachment
    @RequestMapping("/sendMailWithAttachment")
    public String sendMailWithAttachmentCall() throws MessagingException {
        EmailDetails details = new EmailDetails();
        details.setRecipient("avibarca10@gmail.com");
        details.setSubject("Report Generated");
        details.setMsgBody("Please find the attached report");
        details.setAttachment("D:\\IntellijProjects\\SpringBootIntro-master\\response.json");
        return(sendMailWithAttachment(details));
    }

    public String sendMailWithAttachment(
            @RequestBody EmailDetails details) throws MessagingException {
        String status
                = emailService.sendMailWithAttachment(details);

        return status;
    }


    //Junk
    @GetMapping("/greeting")
    public String greetingForm(Model model) {
        model.addAttribute("emailDetails", new EmailDetails());
        return "emailDetails";
    }
}