package com.example.bmi.controller;

import com.example.bmi.service.BmiService;
import com.example.bmi.service.TavilySearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BmiController {

    @Autowired
    private BmiService bmiService;

    @Autowired
    private TavilySearchService tavilySearchService;

    @GetMapping("/")
    public String home() {
        return "redirect:/index.html";
    }

    @PostMapping("/calculate")
    public String calculateBmi(@RequestParam("height") double height,
                               @RequestParam("weight") double weight,
                               Model model) {
        double bmi = bmiService.calculateBmi(height, weight);
        String category = bmiService.getBmiCategory(bmi);
        model.addAttribute("bmi", bmi);
        model.addAttribute("category", category);
        return "result";
    }

    @GetMapping("/api/bmi")
    @ResponseBody
    public BmiResponse getBmi(@RequestParam("height") double height,
                              @RequestParam("weight") double weight) {
        double bmi = bmiService.calculateBmi(height, weight);
        String category = bmiService.getBmiCategory(bmi);
        return new BmiResponse(bmi, category);
    }

    @GetMapping("/api/search")
    @ResponseBody
    public Map<String, Object> search(@RequestParam("query") String query) {
        List<Map<String, String>> results = tavilySearchService.search(query);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("query", query);
        response.put("results", results);
        return response;
    }

    public static class BmiResponse {
        private double bmi;
        private String category;

        public BmiResponse(double bmi, String category) {
            this.bmi = bmi;
            this.category = category;
        }

        public double getBmi() {
            return bmi;
        }

        public String getCategory() {
            return category;
        }
    }
}
