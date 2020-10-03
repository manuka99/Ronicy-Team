package com.adeasy.advertise.util;

import android.os.AsyncTask;

import com.adeasy.advertise.config.Configurations;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class EmailUtility extends AsyncTask{

    private static final String MAIL_USER = Configurations.EMAIL_RONICY;
    private static final String MAIL_PASSWORD = Configurations.PASSWORD_RONICY;
    private static final String MAIL_HOST = Configurations.EMAIL_HOST;
    private static final String MAIL_PORT = Configurations.EMAIL_PORT;
    private String recipientEmail;
    private String subject;
    private String message;

    public EmailUtility(String recipientEmail, String subject, String message){
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.message = message;
    }

    public void sendEmail() throws AddressException,
            MessagingException, UnsupportedEncodingException {

        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", MAIL_HOST);
        properties.put("mail.smtp.port", MAIL_PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MAIL_USER, MAIL_PASSWORD);
            }
        };

        Session session = Session.getInstance(properties, auth);

        // creates a new e-mail message
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(MAIL_USER, MAIL_USER));
        InternetAddress[] toAddresses = {new InternetAddress(this.recipientEmail)};
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(this.subject);
        msg.setSentDate(new Date());
        msg.setContent(this.message, "text/html");

        // sends the e-mail
        Transport.send(msg);

    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            sendEmail();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
