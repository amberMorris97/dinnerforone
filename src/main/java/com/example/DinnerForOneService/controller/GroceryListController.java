package com.example.DinnerForOneService.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GroceryListController {

    @GetMapping("/grocery-list")
    @ResponseBody
    public Map<String, String> getGroceryList(@RequestParam String link) {

    }
}
