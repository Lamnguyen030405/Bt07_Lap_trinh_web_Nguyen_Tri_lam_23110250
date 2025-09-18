package vn.iotstar.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
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
    public String showRegisterPage() {
        return "register"; // tương ứng /views/register.jsp
    }

    @PostMapping
    public String register(
            @RequestParam("email") String email,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("password-repeat") String passwordRepeat,
            @RequestParam("fullname") String fullname,
            @RequestParam("phone") String phone,
            ModelMap model,
            HttpSession session) {

        // Kiểm tra các trường rỗng
        if (email.isEmpty() || username.isEmpty() || password.isEmpty() || passwordRepeat.isEmpty() || fullname.isEmpty() || phone.isEmpty()) {
            model.addAttribute("alert", "Vui lòng điền đầy đủ thông tin!");
            return "register";
        }

        // Kiểm tra mật khẩu khớp
        if (!password.equals(passwordRepeat)) {
            model.addAttribute("alert", "Mật khẩu không khớp!");
            return "register";
        }

        // Xử lý tạo người dùng mới
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password); // Lưu ý: Nên mã hóa mật khẩu trước khi lưu
        user.setFullname(fullname);
        user.setPhone(phone);
        user.setRoleid(2); // Giả sử roleid mặc định là 2 (user thông thường)
        user.setStatus(0); // Giả sử status mặc định là 0 (chưa kích hoạt)
        user.setCreatedate(new java.sql.Date(System.currentTimeMillis())); // Ngày tạo

        // Tạo mã xác nhận ngẫu nhiên
        String code = new EmailUtils().getRandom(); // Sử dụng EmailUtils để tạo mã
        user.setCode(code); // Gán mã xác nhận vào user

        try {
            userService.save(user); // Lưu user vào cơ sở dữ liệu
            boolean emailSent = emailUtils.sendEmail(user); // Gửi email xác thực

            if (emailSent) {
                session.setAttribute("register_email", email);
                return "redirect:/verify"; // Chuyển hướng đến trang xác minh
            } else {
                model.addAttribute("alert", "Gửi email xác thực thất bại. Vui lòng thử lại!");
                return "register";
            }
        } catch (Exception e) {
            model.addAttribute("alert", "Email hoặc tên đăng nhập đã được sử dụng.");
            return "register";
        }
    }
}