package com.capstone.lifesabit.gateguard;

import java.sql.Date;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.capstone.lifesabit.gateguard.login.Member;
import com.capstone.lifesabit.gateguard.passes.Pass;

@Component
public class EmailSender {
  private static final String FROM = "automated@gate-guard.com";
  private static EmailSender instance = null;

  public EmailSender() {
    instance = this;
  }

  @Autowired
  private JavaMailSender emailSender;

  public void emailUserAboutNewPass(Pass pass) {
        Long expirationDate;
        String mes = "";
        MimeMessage message = emailSender.createMimeMessage();
        MimeMultipart mimeMultipart = new MimeMultipart();
        MimeBodyPart textMime = new MimeBodyPart();

        if (pass.getUsageBased() == true) {
          expirationDate = (long) pass.getUsesTotal();
          mes = " gives you <strong>" + expirationDate + " total uses.</strong>";
        } else {
          expirationDate = pass.getExpirationDate();
          mes = " your pass expires on <strong>" + new Date(expirationDate) + ".</strong>";

        }

        try {
          MimeMessageHelper mess = new MimeMessageHelper(message, true);
          mess.setFrom(FROM);
          mess.setTo(pass.getEmail());
          mess.setSubject("A new GateGuard pass has been created for you");
          Member member = SQLLinker.getInstance().getMemberByUUID(pass.getUserID().toString());
 
                textMime.setText("<html><head>"

                + "<title>The title is not usually displayed</title>"
                + "</head>"
                + "<center><img src=https://www.gate-guard.com/static/media/gate_guard.png width=175 height=175></center>"
                + "<h2><div><center><strong>  Hello " + pass.getName() + "!</strong></center></div></h2>"
                + "<body><div><center>A new pass has been created for you by <strong>" + member.getName() + "</strong> on https://www.gate-guard.com/.</center></div>"
                + "<center>This pass has been created for you to gain access to " + member.getName() + "'s community and " + mes + "</center>" 
                + "<center>In order to gain access to the community, have the QR code below scanned upon arrival.</center>"
                + "<center><img src=https://www.gate-guard.com:8443/GateGuard-0.0.1-SNAPSHOT/qrcode/" + pass.getPassID() + "></center>"
                + "<center> Or use the link below at supporting communities to open the gate from your phone:</center>"
                + "<center>" + pass.getURL() + "</center>"
                + "</body></html>",
                "ascii", "html");

                mimeMultipart.addBodyPart(textMime);
                message.setContent(mimeMultipart);

        } catch (MessagingException e) {
          e.printStackTrace();
        }
        emailSender.send(message);
  }

  public void sendPasswordResetEmail(Member member, UUID resetID) {
    MimeMessage message = emailSender.createMimeMessage();
    MimeMultipart mimeMultipart = new MimeMultipart();
    MimeBodyPart textMime = new MimeBodyPart();

    try {
      MimeMessageHelper mess = new MimeMessageHelper(message, true);
      mess.setFrom(FROM);
      mess.setTo(member.getEmail());
      mess.setSubject("GateGuard Password Reset Link");

            textMime.setText("<html><head>"

            + "<title>The title is not usually displayed</title>"
            + "</head>"
            + "<center><img src=https://www.gate-guard.com/static/media/gate_guard.png width=175 height=175></center>"
            + "<h2><div><center><strong> Hello " + member.getName() + "!</strong></center></div></h2>"
            + "<body><div><center>We recieved your request to reset your password.<center></div>"
            + "<center><div>To do so click the button below:</center></div>"
            + "<center><a href=https://www.gate-guard.com/reset-password?resetID=" + resetID.toString() + "> <button style=background-color:#0096FF;color:white;text-align:center;text-decoration:none;font-size:14px;cursor:pointer;border-radius:10px> Reset Your Password</button></a><center>"
            + "<br></br>"
            + "<br></br>"
            + "<center>If the Reset Your Password button does not work click the link below"
            + "<center>https://www.gate-guard.com/reset-password?resetID=" + resetID.toString() + "</center>"
            + "<center>If you did not request this password reset, you can safely ignore this e-mail.</center>"
            + "</body></html>",
            "ascii", "html");

            mimeMultipart.addBodyPart(textMime);
            message.setContent(mimeMultipart);

    } catch (MessagingException e) {
      e.printStackTrace();
    }
    emailSender.send(message);
  }

  public void sendPasswordResetNotifyEmail(Member member) {
 MimeMessage message = emailSender.createMimeMessage();
 MimeMultipart mimeMultipart = new MimeMultipart();
 MimeBodyPart textMime = new MimeBodyPart();

 try {
   MimeMessageHelper mess = new MimeMessageHelper(message, true);
   mess.setFrom(FROM);
   mess.setTo(member.getEmail());
   mess.setSubject("Your GateGuard password has been reset.");

         textMime.setText("<html><head>"

         + "<title>The title is not usually displayed</title>"
         + "</head>"
         + "<center><img src=https://www.gate-guard.com/static/media/gate_guard.png width=175 height=175></center>"
         + "<h2><div><center><strong> Hello " + member.getName() + "!</strong></center></div></h2>"
         + "<body><div><center>Your password has been reset! Visit the website by clicking the link below: </center></div>"
         + "<body><div><center>https://www.gate-guard.com/.</center></div>"
         + "<br></br>"

         + "<center>If you did not reset your password contact the website administration at <a href=\"mailto:admin@gate-guard.com\">admin@gate-guard.com</a></center>"
         + "</body></html>",
         "ascii", "html");

         mimeMultipart.addBodyPart(textMime);
         message.setContent(mimeMultipart);

 } catch (MessagingException e) {
   e.printStackTrace();
 }
 emailSender.send(message);
}

public void emailNewAccount(Member member) {
  MimeMessage message = emailSender.createMimeMessage();
  MimeMultipart mimeMultipart = new MimeMultipart();
  MimeBodyPart textMime = new MimeBodyPart();

  try {
    MimeMessageHelper mess = new MimeMessageHelper(message, true);
    mess.setFrom(FROM);
    mess.setTo(member.getEmail());
    mess.setSubject("Welcome to GateGuard!");

          textMime.setText("<html><head>"

          + "<title>The title is not usually displayed</title>"
          + "</head>"
          + "<center><img src=https://www.gate-guard.com/static/media/gate_guard.png width=175 height=175></center>"
          + "<h2><div><center><strong>  Hello " + member.getName() + "!</strong></center></div></h2>"
          + "<body><div><center>Welcome to https://www.gate-guard.com/.</center></div>"
          + "<center>You are recieving this email because you have just created an account with Gate Guard.</center>"
          + "<br></br>"
          + "<center>The app's primary focus is safety in your neighborhood.</center>"
          + "<center>The app allows you to join a community and create passes that are in the form of QR codes.</center>"
          + "<center> Whoever you send passes to will arrive to your community and in order to gain access must have the QR code scanned, or use a provided link to open the gate from their phone at supporting communities.</center>"
          + "<center>If you have any questions please reach out to the app admins and they will be able to assist you.</center>" 
          + "</body></html>",
          "ascii", "html");

          mimeMultipart.addBodyPart(textMime);
          message.setContent(mimeMultipart);

  } catch (MessagingException e) {
    e.printStackTrace();
  }
  emailSender.send(message);
}

  public static EmailSender getInstance() {
    return instance;
  }
}


