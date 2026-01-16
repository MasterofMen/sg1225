package com.toolShop.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import org.springframework.web.bind.MethodArgumentNotValidException;

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
    // Returns BAD_REQUEST if validation fails or if illegal argument is thrown
    public ResponseEntity<?> rentTool(@Valid @RequestBody RentalAgreement rentalRequest, @PathVariable String toolCode){
        try{
            RentalAgreement ra = toolService.rentTool(rentalRequest, toolCode);
            return new ResponseEntity<>(ra, HttpStatus.CREATED);
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid request"));
        }
    }

    // Exception handler for validation errors
    // When input validation fails, this method formats all validation errors into a single message
    // and returns them to the client as a BAD_REQUEST response
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidation(MethodArgumentNotValidException ex){
        // Combine all field validation errors into a single string with field names and error messages
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest().body(Map.of("error", message));
    }
}
