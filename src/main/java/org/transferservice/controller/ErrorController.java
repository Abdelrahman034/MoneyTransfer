package org.transferservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ErrorController {

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String expired, Model model) {
        if ("expired".equals(expired)) {
            model.addAttribute("message", "Your session has expired. Please log in again.");
        }
        return "login";
    }
}
