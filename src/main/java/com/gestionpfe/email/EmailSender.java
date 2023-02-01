package com.gestionpfe.email;

public interface EmailSender {

    void sendEmail(String to, String email, String subject);
}
