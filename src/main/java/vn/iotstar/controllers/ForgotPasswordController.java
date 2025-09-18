package vn.iotstar.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import vn.iotstar.services.IUserService;

@Controller
@RequestMapping("/forgot-password")
public class ForgotPasswordController {

    @Autowired
    private IUserService userService;

    @GetMapping
    public String showForm() {
        return "forgot-password"; // tương ứng /views/forgot-password.jsp
    }

    @PostMapping
    public String handleForgotPassword(
            @RequestParam("email") String email,
            ModelMap model,
            HttpSession session) {

        if (email == null || email.isEmpty()) {
            model.addAttribute("alert", "Vui lòng nhập email.");
            return "forgot-password";
        }

        if (!userService.checkExistEmail(email)) {
            model.addAttribute("alert", "Email không tồn tại.");
            return "forgot-password";
        }

        session.setAttribute("reset_email", email);
        return "redirect:/verify";
    }
}
