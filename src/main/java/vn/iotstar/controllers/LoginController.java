package vn.iotstar.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import vn.iotstar.entities.UserEntity;
import vn.iotstar.services.IUserService;
import vn.iotstar.utils.Constant;

@Controller
public class LoginController {

    @Autowired
    IUserService userService;

    // Hiển thị form đăng nhập
    @GetMapping("/login")
    public String loginForm() {
        return "login"; // login.jsp
    }

    // Xử lý đăng nhập
    @PostMapping("/login")
    public String login(
            @RequestParam("uname") String username,
            @RequestParam("psw") String password,
            @RequestParam(value = "remember", required = false) String remember,
            HttpSession session,
            HttpServletResponse response,
            ModelMap model) {

        // Kiểm tra rỗng
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            model.addAttribute("alert", "Tài khoản hoặc mật khẩu không được rỗng");
            return "login";
        }

        // Gọi service để xử lý đăng nhập
        UserEntity user = userService.login(username, password);
        if (user != null) {
            // Lưu vào session
            session.setAttribute("account", user);

            // Nếu tick Remember Me
            if ("on".equals(remember)) {
                Cookie cookie = new Cookie(Constant.COOKIE_REMEMBER, username);
                cookie.setMaxAge(30 * 60); // 30 phút
                response.addCookie(cookie);
            }

            return "redirect:/waiting";
        } else {
            model.addAttribute("alert", "Tài khoản hoặc mật khẩu không đúng");
            return "login";
        }
    }
}
