package com.toolShop.constant;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.toolShop.cache.Tool;

public class Constants {
    
    public static final List<String> ALL_TOOLS = Collections.unmodifiableList(
        Arrays.asList("CHNS", "LADW", "JAKD", "JAKR"));

    public static final Map<String, Tool> TOOL_MAP = Map.ofEntries(
        Map.entry("CHNS", new Tool("Chainsaw", "stihl", 1.99f ,false, false, false)),
        Map.entry("LADW", new Tool("Ladder", "Werner", 1.49f ,false, false, false)),
        Map.entry("JAKD", new Tool("Jackhammer", "DeWalt", 2.99f ,false, false, false)),
        Map.entry("JAKR", new Tool("Jackhammer", "Ridgid", 2.99f ,false, false, false))
    );

}
