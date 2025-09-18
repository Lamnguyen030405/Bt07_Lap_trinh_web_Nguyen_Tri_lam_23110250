package vn.iotstar.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import vn.iotstar.entities.UserEntity;
import vn.iotstar.services.IUserService;
import vn.iotstar.services.impl.UserServiceImpl;

@Controller
@RequestMapping("/verify")
public class VerifyController {

	IUserService userService = new UserServiceImpl();
	
    @GetMapping
    public String showVerifyPage(ModelMap model, HttpSession session) {
        String email = (String) session.getAttribute("register_email");
        if (email == null) {
            return "redirect:/register";
        }
        model.addAttribute("email", email);
        return "verify";
    }

    @PostMapping
    public String verify(@RequestParam("code") String code, ModelMap model, HttpSession session) {
        String email = (String) session.getAttribute("register_email");
        if (email == null) {
            return "redirect:/register";
        }

        UserEntity user = userService.findByEmail(email); // Giả sử có phương thức findByEmail
        if (user != null && user.getCode().equals(code)) {
            user.setStatus(1); // Kích hoạt tài khoản
            userService.save(user);
            session.removeAttribute("register_email");
            model.addAttribute("message", "Xác minh thành công! Bạn có thể đăng nhập.");
            return "login";
        } else {
            model.addAttribute("alert", "Mã xác nhận không đúng. Vui lòng thử lại!");
            return "verify";
        }
    }
}