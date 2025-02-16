package com.urbix.serviceImpl;

import com.urbix.service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OTPServiceImpl implements OTPService {

    public static Map<String, OTPEntry> otpStorage = new HashMap<>();

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public String generateOTP(String email) {
        String otp = String.format("%06d", new Random().nextInt(1000000));
        otpStorage.put(email, new OTPEntry(otp, LocalDateTime.now().plusMinutes(5)));
        return otp;
    }
    @Override
    public boolean resendOTP(String email) {
        OTPEntry entry = otpStorage.get(email);

        // Check if OTP already exists and is still valid
        if (entry != null && LocalDateTime.now().isBefore(entry.expirationTime)) {
            return false;
        }

        // Generate and send a new OTP
        String newOtp = generateOTP(email);
        otpStorage.put(email, new OTPEntry(newOtp, LocalDateTime.now().plusMinutes(5)));

        sendOTP(email, newOtp);
        return true;
    }

    @Override
    public void sendOTP(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp + "\nThis OTP is valid for 5 minutes.");
        mailSender.send(message);
    }

    @Override
    public boolean verifyOTP(String email, String otp) {
        OTPEntry entry = otpStorage.get(email);
        if (entry == null || LocalDateTime.now().isAfter(entry.expirationTime)) {
            otpStorage.remove(email);
            return false;
        }
        if (entry.otp.equals(otp)) {
            otpStorage.remove(email);
            return true;
        }
        return false;
    }

    public static class OTPEntry {
        String otp;
        public LocalDateTime expirationTime;

        public OTPEntry(String otp, LocalDateTime expirationTime) {
            this.otp = otp;
            this.expirationTime = expirationTime;
        }

    }
}
