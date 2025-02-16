package com.urbix.controller;

import com.urbix.entity.User;
import com.urbix.enums.UserType;
import com.urbix.repository.UserRepository;
import com.urbix.service.OTPService;
import com.urbix.serviceImpl.OTPServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.Map;
import com.urbix.serviceImpl.*;

@Controller
@RequestMapping("/api/otp")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OTPService otpService;



    // Send OTP for Registration
    @PostMapping("/send-otp")
    public String sendOTP(@RequestParam String email,
                          @RequestParam String fullName,
                          @RequestParam String password,
                          @RequestParam String userType,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        try {
            // Check if email already exists
            if (userRepository.findByEmail(email).isPresent()) {
                redirectAttributes.addFlashAttribute("errorMessage","This Email Is Already Taken Please Login!");
                return "/login";
            }

            // Generate and send OTP
            String otp = otpService.generateOTP(email);
            otpService.sendOTP(email, otp);

            // Store user details in session (temporarily)
            session.setAttribute("pendingUserEmail", email);
            session.setAttribute("pendingUserFullName", fullName);
            session.setAttribute("pendingUserPassword", password);
            session.setAttribute("pendingUserType", userType);
            redirectAttributes.addFlashAttribute("successMessage","OTP sent to email. Please verify.");
            return "verify-register";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage","Error sending OTP. Please try again.");
            return "register";
        }
    }

    // Verify OTP and Register User
    @PostMapping("/verify-otp")
    @Transactional
    public String verifyOTP(@RequestParam String email,
                            @RequestParam String otp,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        try {
            // Verify OTP
            if (!otpService.verifyOTP(email, otp)) {
                redirectAttributes.addFlashAttribute("errormessage","Invalid OTP , try Again!");
                return "verify-register";
            }

            // Retrieve user details from session
            String fullName = (String) session.getAttribute("pendingUserFullName");
            String password = (String) session.getAttribute("pendingUserPassword");
            String userType = (String) session.getAttribute("pendingUserType");

            if (fullName == null || password == null || userType == null) {
                redirectAttributes.addFlashAttribute("errorMessage","Session expired. Please register again.");
                return "/login";
            }

            // Save user to database
            User newUser = new User();
            newUser.setName(fullName);
            newUser.setProvider("URBIX");
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setUserType(userType);
            userRepository.save(newUser);

            // Clean up session
            session.removeAttribute("pendingUserEmail");
            session.removeAttribute("pendingUserFullName");
            session.removeAttribute("pendingUserPassword");
            session.removeAttribute("pendingUserType");
            redirectAttributes.addFlashAttribute("successMessage","User registered successfully, Please Login");
            return "/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",e.getLocalizedMessage());
            return "/login";
        }
    }
    @GetMapping("/resend-otp")
    public String resendOTP(@RequestParam String email,
                            RedirectAttributes redirectAttributes) {
     if(otpService.resendOTP(email)) {
         redirectAttributes.addFlashAttribute("successMessage", "A new OTP has been sent to your email.");
     }
     else{
         redirectAttributes.addFlashAttribute("errorMessage", "Your current OTP is still valid. Please use it.");
     }

        return "redirect:/auth/verify-otp?email=" + email;
    }

}