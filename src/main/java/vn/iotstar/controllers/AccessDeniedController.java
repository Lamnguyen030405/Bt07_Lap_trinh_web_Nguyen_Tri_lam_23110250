package vn.iotstar.controllers;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import vn.iotstar.entities.UserEntity;

@Controller
public class AccessDeniedController {

    @GetMapping("/access-denied")
    public String accessDenied(ModelMap model, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("account");
        
        if (user != null) {
            model.addAttribute("message", "Bạn không có quyền truy cập trang này!");
            model.addAttribute("username", user.getUsername());
            model.addAttribute("role", getRoleName(user.getRoleid()));
        } else {
            return "redirect:/login";
        }
        
        return "access-denied";
    }
    
    private String getRoleName(int roleId) {
        switch (roleId) {
            case 3:
                return "Admin";
            case 2:
                return "Manager";
            case 1:
                return "User";
            default:
                return "Unknown";
        }
    }
}