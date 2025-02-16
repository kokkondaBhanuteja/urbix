package com.urbix.controller;

import com.urbix.dto.TownshipDTO;

import com.urbix.service.TownShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    @Autowired
    private TownShipService townShipService;
    @GetMapping("/dashboard")
    public String dashboard(OAuth2AuthenticationToken token , Model model) {
        model.addAttribute("name",token.getPrincipal().getAttribute("name"));
        model.addAttribute("email",token.getPrincipal().getAttribute("email"));
        model.addAttribute("photo",token.getPrincipal().getAttribute("picture"));
        return "dashboard";
    }
    @GetMapping("/")
    public String home(Model model) {
        List<TownshipDTO> townships =townShipService.getAllTownships();

        // Group townships by state
        Map<String, List<TownshipDTO>> groupedTownships = townships.stream()
                .collect(Collectors.groupingBy(TownshipDTO::getState));

        model.addAttribute("states", groupedTownships);
        model.addAttribute("pageTitle", "URBIX - Land Investment Platform");
        model.addAttribute("appName", "URBIX");
        model.addAttribute("heroTitle", "Your Gateway to Land Investments Made Simple");
        model.addAttribute("heroSubtitle", "Connect with landowners and discover prime properties through our interactive platform");
        return "home";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/register")
    public String showRegistrationPage(){
        return "register";
    }
}