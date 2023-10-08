package com.capstone.lifesabit.gateguard;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class MailSenderBean {

  private static final String HOST = "smtp.zoho.com";
  private static final int PORT = 587;

  @Bean
  public JavaMailSender getJavaMailSender() {
      JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
      mailSender.setHost(HOST);
      mailSender.setPort(PORT);
      mailSender.setUsername("automated@gate-guard.com");
      mailSender.setPassword("VSEwhC89bgim");
      return mailSender;
  }
}
