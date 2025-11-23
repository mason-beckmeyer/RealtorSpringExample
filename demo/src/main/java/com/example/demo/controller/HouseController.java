package com.example.demo.controller;

import com.example.demo.service.HouseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.JsonNode;

import java.util.Map;
import java.util.UUID;

@RequestMapping("/houses")
@RestController
public class HouseController {
    HouseService houseService;
    public HouseController(HouseService houseService) {
        this.houseService = houseService;
    }


    @GetMapping
    public ResponseEntity<JsonNode> getHouses(){

        return houseService.getHouses();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<JsonNode> updateHouse(
            @PathVariable UUID id,
            @RequestBody Map<String, Object> updates
    ){

        if (!houseService.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        return houseService.updateHouse(id,updates);
    }
}
