package com.urbix.controller;

import com.urbix.entity.Township;
import com.urbix.repository.TownShipRepository;
import com.urbix.service.TownShipService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/townships")
public class TownShipController {

    private final TownShipRepository townshipRepository;
    private final TownShipService townshipService;

    public TownShipController(TownShipRepository townshipRepository, TownShipService townshipService) {
        this.townshipRepository = townshipRepository;
        this.townshipService = townshipService;
    }

    @GetMapping("/add")
    public String showAddTownshipForm(Model model) {
        model.addAttribute("township", new Township());
        return "AddTownship"; // Thymeleaf template for form
    }
    @PostMapping("/add-townShip")
    public String addTownship(@ModelAttribute Township township,
                              @RequestParam("imageFile") MultipartFile file,
                              RedirectAttributes redirectAttributes) {
        try {
            // Convert image file to byte array
            township.setImage(file.getBytes());

            // Save the township with the image
            townshipRepository.save(township);

            redirectAttributes.addFlashAttribute("successMessage", "Township added successfully!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error uploading image.");
        }
        return "redirect:/townships/add";
    }

}
