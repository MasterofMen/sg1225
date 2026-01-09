package com.toolShop.controller;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toolShop.cache.Tool;
import com.toolShop.services.ToolService;

@RestController
@RequestMapping("/api/tools")
public class ToolApi {

    @Autowired
    ToolService toolService;

    @GetMapping
    public List<Tool> listTools(){
        toolService.getAllTools();
        return null;
    }
    // The rental tool api call. 
    @PostMapping("/{code}/{days}/{dicount}/{date}")
    public ResponseEntity<Tool> rentTool(@PathVariable String code, @PathVariable int days, @PathVariable float discount, @PathVariable LocalDate date){
        toolService.rentTool(code, days, discount, date);
        return null;
    }
}
