package vn.iotstar.controllers;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import vn.iotstar.entities.UserEntity;
import vn.iotstar.utils.Constant;

@Controller
public class WaitingController {

    @GetMapping("/waiting")
    public String waiting(HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute(Constant.SESSION_ACCOUNT);

        if (user != null) {
            int role = user.getRoleid();
            
            // Redirect theo role
            switch (role) {
                case Constant.ROLE_ADMIN: // Admin
                    return "redirect:/admin/home";
                case Constant.ROLE_MANAGER: // Manager
                    return "redirect:/manager/home";
                case Constant.ROLE_USER: // User
                    return "redirect:/user/home";
                default:
                    return "redirect:/login";
            }
        } else {
            return "redirect:/login";
        }
    }
}