package vn.iotstar.controllers;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import vn.iotstar.entities.UserEntity;

@Controller
public class WaitingController {

    @GetMapping("/waiting")
    public String waiting(HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("account");

        if (user != null) {
            int role = user.getRoleid();
            if (role == 3) {
                return "redirect:/admin/home";
            } else if (role == 2) {
                return "redirect:/manager/home";
            } else {
                return "redirect:/user/home";
            }
        } else {
            return "redirect:/login";
        }
    }
}
