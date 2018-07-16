/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csrportal.helpers;


import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author shady
 */

public class Mailer extends Thread  {

 
    private static String USER_NAME = "postmaster@sandbox724a2b1a33f547798d87fbb228859a98.mailgun.org";  // GMail user name (just the part before "@gmail.com")
    private static String PASSWORD = "ed03d08a7acf210ddc17869f05aedb88"; // GMail password
    private static String RECIPIENT = "wynton.franklin@gmail.com";
    private static String FROM = "shadywf@hotmail.com";
    private static String HOST = "smtp.mailgun.org";
    public String message;
    public String [] recipient;
    public String subject;
    
    public Mailer(){
        
    }

    public static void main(String[] args) {
        String[] to = { RECIPIENT }; // list of recipient email addresses
        String subject = "Java send mail example";
        String body = "Welcome to JavaMail!";

        sendFromGMail(to, subject, body);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String[] getRecipient() {
        return recipient;
    }

    public void setRecipient(String[] recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String sub) {
        this.subject = sub;
    }

    @Override
    public void run() {
        String [] email_to = this.getRecipient();
        String email_subject = this.getSubject();
        String email_body = this.getMessage();
        Mailer.sendFromGMail(email_to, email_subject, email_body);
    }
    
    

    public static void sendFromGMail( String[] to, String subject, String body) {
        Properties props = System.getProperties();
        String host = "smtp.mailgun.org";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", Mailer.HOST);
        props.put("mail.smtp.user", Mailer.USER_NAME);
        props.put("mail.smtp.password", Mailer.PASSWORD );
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(Mailer.USER_NAME));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(Mailer.HOST, Mailer.USER_NAME, Mailer.PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }
}
