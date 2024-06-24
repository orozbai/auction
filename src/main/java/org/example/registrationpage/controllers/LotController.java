package org.example.registrationpage.controllers;

import lombok.AllArgsConstructor;
import org.example.registrationpage.dtos.LotRegisterDto;
import org.example.registrationpage.entities.LotEntity;
import org.example.registrationpage.services.LotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class LotController {
    @Autowired
    private LotService lotService;

    @GetMapping("get-lots")
    private ResponseEntity<?> getAllLots(Model model) {
        List<LotEntity> lots = lotService.getAllLots();
        model.addAttribute("lots", lots);
        if (lots != null) {
            return new ResponseEntity<>(lots, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>("Lots not found", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/auc/get-lot-by-id/{lotid}")
    private LotEntity getUser(@PathVariable Long lotid, Model model) {
        LotEntity lot = lotService.getLot(lotid);
        model.addAttribute("lot", lot);
        return lot;
    }

    @DeleteMapping("/auc/delete-lot-by-id/{lotid}")
    private void deleteLot(@PathVariable Long lotid) {
        lotService.deleteLotById(lotid);
    }

    @PostMapping("/auc/update-lot")
    private void updateLot(@RequestBody LotRegisterDto updatedLot) {
        lotService.updateLot(updatedLot);
    }

}
