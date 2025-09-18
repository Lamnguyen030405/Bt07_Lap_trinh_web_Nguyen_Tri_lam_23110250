package vn.iotstar.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import vn.iotstar.entities.UserEntity;
import vn.iotstar.services.IUserService;

@Controller
@RequestMapping("/reset-password")
public class ResetPasswordController {

    @Autowired
    private IUserService userService;

    @GetMapping
    public String showForm(HttpSession session, ModelMap model) {
        String email = (String) session.getAttribute("reset_email");
        if (email == null) {
            return "redirect:/login";
        }
        model.addAttribute("email", email);
        return "reset-password"; // /views/reset-password.jsp
    }

    @PostMapping
    public String resetPassword(
            @RequestParam("newpass") String newPass,
            @RequestParam("confirmpass") String confirmPass,
            HttpSession session,
            ModelMap model) {

        String email = (String) session.getAttribute("reset_email");
        if (email == null) {
            return "redirect:/login";
        }

        if (!newPass.equals(confirmPass)) {
            model.addAttribute("alert", "Mật khẩu xác nhận không khớp.");
            return "reset-password";
        }

        UserEntity user = userService.findByEmail(email);
        if (user == null) {
            model.addAttribute("alert", "Không tìm thấy tài khoản.");
            return "reset-password";
        }

        user.setPassword(newPass);
        userService.save(user);
        session.removeAttribute("reset_email");
        return "redirect:/login";
    }
}
