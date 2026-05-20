package se.iths.paveena.springmessenger.messaging;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.iths.paveena.springmessenger.model.Email;
import se.iths.paveena.springmessenger.model.Message;

import java.util.Properties;

@Component("email")
public class EmailSender implements Messenger {
    @Value("${spring.mail.username}")
    private String from;

    @Value("${spring.mail.password}")
    private String appPassword;

    @Override
    public void send(Message message) {
        if (!(message instanceof Email email)) {
            throw new IllegalArgumentException("Fel typ av meddelande");
        }
        //Här har vi tillgång till email variabel
        //Implementation av den abstrakta metoden send i interfacet Messenger
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, appPassword);
            }
        });

        try {
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(from,"Mey"));
            mimeMessage.setReplyTo(new InternetAddress[]{
                new InternetAddress(from)});
            mimeMessage.setRecipient(jakarta.mail.Message.RecipientType.TO,
                    new InternetAddress(email.getRecipient()));
            mimeMessage.setSubject(email.getSubject());
            mimeMessage.setText(email.getMessage());
            Transport.send(mimeMessage);

            System.out.println("Email sent ");
        }
        catch (Exception e) {
            System.out.println( ("Error sending message")+ e.getMessage());;
        }
    }
}

