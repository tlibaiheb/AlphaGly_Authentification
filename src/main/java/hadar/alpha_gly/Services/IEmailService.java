package hadar.alpha_gly.Services;


import hadar.alpha_gly.payload.mailing.EmailDetails;

public interface IEmailService {

    // Method
    // To send a simple email
    String sendSimpleMail(EmailDetails details);

    // Method
    // To send an email with attachment
    String sendMailWithAttachment(EmailDetails details);
}
