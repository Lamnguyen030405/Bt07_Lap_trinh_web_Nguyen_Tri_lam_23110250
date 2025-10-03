package vn.iotstar.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import vn.iotstar.dto.RegisterDTO;
import vn.iotstar.entities.UserEntity;
import vn.iotstar.services.IUserService;
import vn.iotstar.utils.EmailUtils; // Thêm import

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private IUserService userService;

    @Autowired
    private EmailUtils emailUtils; // Tiêm dependency cho EmailUtils

    @GetMapping
    public String showRegisterPage(ModelMap model) {
        model.addAttribute("register", new RegisterDTO());
        return "register"; // Thymeleaf sẽ dùng templates/register.html
    }


    @PostMapping
    public String register(
            @ModelAttribute("register") @Valid RegisterDTO dto,
            BindingResult result,
            ModelMap model,
            HttpSession session) {

        // Nếu có lỗi validate => trả về form
        if (result.hasErrors()) {
            return "register";
        }

        // Kiểm tra mật khẩu khớp
        if (!dto.getPassword().equals(dto.getPasswordRepeat())) {
            model.addAttribute("alert", "Mật khẩu không khớp!");
            return "register";
        }

        // Xử lý tạo người dùng mới
        UserEntity user = new UserEntity();
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword()); // Nên mã hóa
        user.setFullname(dto.getFullname());
        user.setPhone(dto.getPhone());
        user.setRoleid(2);
        user.setStatus(0);
        user.setCreatedate(new java.sql.Date(System.currentTimeMillis()));

        String code = emailUtils.getRandom();
        user.setCode(code);

        try {
            userService.save(user);
            boolean emailSent = emailUtils.sendEmail(user);
            if (emailSent) {
                session.setAttribute("register_email", dto.getEmail());
                return "redirect:/verify";
            } else {
                model.addAttribute("alert", "Gửi email xác thực thất bại.");
                return "register";
            }
        } catch (Exception e) {
            model.addAttribute("alert", "Email hoặc tên đăng nhập đã tồn tại.");
            return "register";
        }
    }
}