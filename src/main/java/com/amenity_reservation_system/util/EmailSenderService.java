package com.amenity_reservation_system.util;

import com.amenity_reservation_system.entity.Reservation;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailSenderService implements Runnable{

    private final JavaMailSender mailSender;
    private final String toEMail;
    private final Reservation reservation;

    public EmailSenderService(JavaMailSender mailSender, String toEMail, Reservation reservation) {
        this.mailSender = mailSender;
        this.toEMail = toEMail;
        this.reservation = reservation;
    }

    @Override
    public void run() {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("aqua.aqua@gmx.com");
        mailMessage.setTo(toEMail);
        mailMessage.setSubject("Amenity Reservation System");
        mailMessage.setText(
                "--------\n" +
                        "Amenity Reservation System Details:\n" +
                        "Hi " + reservation.getUser().getFullName() + ",\n" +
                        "This is a confirmation of your booking\n" +
                        "Who:\n" +
                        reservation.getUser().getUsername() + "\n" +
                        "What:\n" +
                        reservation.getAmenityType().getAmenityName() + "\n" +
                        "When:\n" +
                        reservation.getReservationDate() + "\n" +
                        reservation.getStartTime() + " - " + reservation.getEndTime() + "\n" +
                        "Thank you!\n" +
                        "---------\n");


        mailSender.send(mailMessage);

        System.out.println("Email Sent Successfully!! " + toEMail);
    }
}




