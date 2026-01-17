package com.toolShop.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toolShop.cache.RentalAgreement;
import com.toolShop.services.ToolService;

@RestController
@RequestMapping("/api/tools")
public class ToolApi {

    @Autowired
    ToolService toolService;

    // GET endpoint that returns a list of all available tool codes
    @GetMapping
    public List<String> listTools(){
        return toolService.getAllTools();
    }
    // POST endpoint to rent a tool
    // Takes a rental request body and tool code as path parameter
    // Returns the completed rental agreement with calculated prices and dates
    @PostMapping("/rental/{toolCode}")
    public ResponseEntity<?> rentTool(@Valid @RequestBody RentalAgreement rentalRequest, @PathVariable String toolCode){
        RentalAgreement ra = toolService.rentTool(rentalRequest, toolCode);
        return new ResponseEntity<>(ra, HttpStatus.CREATED);
    }
}
