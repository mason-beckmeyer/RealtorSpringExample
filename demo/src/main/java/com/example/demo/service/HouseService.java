package com.example.demo.service;

import com.example.demo.dao.House;
import com.example.demo.dao.HouseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class HouseService {
    HouseRepository houseRepository;

    /*
        * ObjectMapper is used to convert Java objects to JSON and vice versa.
        * Its From the Jackson library.
        * It implements serializable
     */
    ObjectMapper objectMapper;
    public HouseService(HouseRepository houseRepository,
                        ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.houseRepository = houseRepository;
    }

    public ResponseEntity<JsonNode> getHouses() {

        List<House> houses = houseRepository.findAll();

        if (houses.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        String arrayNode = objectMapper.writeValueAsString(houses);
        ArrayNode jsonNodes = (ArrayNode) objectMapper.readTree(arrayNode);

        return new ResponseEntity<>(jsonNodes, HttpStatus.OK);
    }

    public boolean existsById(UUID id) {
        return houseRepository.existsById(id);
    }

    public ResponseEntity<JsonNode> updateHouse(UUID id, Map<String, Object> updates) {

        House house= houseRepository.findById(id).get();

        updates.entrySet().forEach(entry -> {
            String key = entry.getKey();
            Object value = entry.getValue();


            switch (key) {
                case "address" -> house.setAddress((String) value);
                case "owner" -> house.setOwner((String) value);
                case "price" -> house.setPrice((Double) value);
            }

            houseRepository.save(house);
        });

        return new ResponseEntity<>(objectMapper.valueToTree(house), HttpStatus.OK);
    }

}
