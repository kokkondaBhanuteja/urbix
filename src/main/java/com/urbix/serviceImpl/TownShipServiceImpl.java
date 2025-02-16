package com.urbix.serviceImpl;
import com.urbix.dto.TownshipDTO;
import com.urbix.entity.Township;
import com.urbix.repository.TownShipRepository;
import com.urbix.service.TownShipService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TownShipServiceImpl implements TownShipService {

    private final TownShipRepository townshipRepository;

    public TownShipServiceImpl(TownShipRepository townshipRepository) {
        this.townshipRepository = townshipRepository;
    }
    @Override
    public List<TownshipDTO> getAllTownships() {
        List<Township> townships = townshipRepository.findAll();

        return townships.stream().map(t -> {
            String base64Image = (t.getImage() != null) ? Base64.getEncoder().encodeToString(t.getImage()) : "";

            System.out.println("Encoded Image for " + t.getName() + ": " + base64Image.substring(0, Math.min(50, base64Image.length())) + "...");

            return new TownshipDTO(
                    t.getName(),
                    t.getState(),
                    t.getDescription(),
                    base64Image
            );
        }).collect(Collectors.toList());
    }
}
