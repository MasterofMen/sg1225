package com.toolShop.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.toolShop.cache.Tool;

// Constants class that holds all available tools and their rental information
public class Constants {
    
    // List of all valid tool codes that can be rented
    public static final List<String> ALL_TOOLS = Collections.unmodifiableList(
        Arrays.asList("CHNS", "LADW", "JAKD", "JAKR"));

    // Map of all tools with their details including type, brand, daily charge, and charge rules
    // Key: Tool code (CHNS, LADW, JAKD, JAKR)
    // Value: Tool object with rental information (type, brand, daily charge, weekday charge, weekend charge, holiday charge)
    public static final Map<String, Tool> TOOL_MAP = Map.ofEntries(
        // Chainsaw (CHNS): Stihl brand, $1.99/day, charges on weekdays and holidays, not on weekends
        Map.entry("CHNS", new Tool("Chainsaw", "stihl", 1.99f ,true, false, true)),
        
        // Ladder (LADW): Werner brand, $1.49/day, charges every day (weekdays, weekends, and holidays)
        Map.entry("LADW", new Tool("Ladder", "Werner", 1.49f ,true, true, false)),
        
        // Jackhammer (JAKD): DeWalt brand, $2.99/day, charges only on weekdays, not weekends or holidays
        Map.entry("JAKD", new Tool("Jackhammer", "DeWalt", 2.99f ,true, false, false)),
        
        // Jackhammer (JAKR): Ridgid brand, $2.99/day, charges only on weekdays, not weekends or holidays
        Map.entry("JAKR", new Tool("Jackhammer", "Ridgid", 2.99f ,true, false, false))
    );

}
