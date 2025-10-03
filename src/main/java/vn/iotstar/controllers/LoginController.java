package vn.iotstar.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import vn.iotstar.dto.LoginDTO;
import vn.iotstar.entities.UserEntity;
import vn.iotstar.services.IUserService;
import vn.iotstar.utils.Constant;

@Controller
public class LoginController {

	@Autowired
	IUserService userService;

	// Hiển thị form đăng nhập
	@GetMapping("/login")
	public String loginForm(ModelMap model, HttpSession session) {
		// Nếu đã đăng nhập rồi thì redirect về trang chủ tương ứng
		UserEntity user = (UserEntity) session.getAttribute("account");
		if (user != null) {
			return redirectByRole(user.getRoleid());
		}
		
		model.addAttribute("login", new LoginDTO());
		return "login";
	}

	@PostMapping("/login")
	public String login(@ModelAttribute("login") @Valid LoginDTO dto,
			BindingResult result, 
			ModelMap model,
			HttpServletResponse response,
			HttpSession session) {

		// Nếu có lỗi validate => trả về form
		if (result.hasErrors()) {
			return "login";
		}

		// Gọi service để xử lý đăng nhập
		UserEntity user = userService.login(dto.getUsername(), dto.getPassword());
		if (user != null) {
			// Lưu vào session
			session.setAttribute("account", user);

			// Nếu tick Remember Me
			if (dto.isRemember()) {
				Cookie cookie = new Cookie(Constant.COOKIE_REMEMBER, dto.getUsername());
				cookie.setMaxAge(30 * 60); // 30 phút
				response.addCookie(cookie);
			}

			// Kiểm tra xem có URL cần redirect không (từ interceptor)
			String redirectURL = (String) session.getAttribute("redirectURL");
			if (redirectURL != null && !redirectURL.isEmpty()) {
				session.removeAttribute("redirectURL");
				return "redirect:" + redirectURL;
			}

			// Redirect theo role
			return redirectByRole(user.getRoleid());
		} else {
			model.addAttribute("alert", "Tài khoản hoặc mật khẩu không đúng");
			return "login";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session, HttpServletResponse response) {
		// Xóa session
		session.removeAttribute("account");
		session.invalidate();

		// Xóa cookie remember me
		Cookie cookie = new Cookie(Constant.COOKIE_REMEMBER, "");
		cookie.setMaxAge(0);
		response.addCookie(cookie);

		return "redirect:/login";
	}

	// Helper method để redirect theo role
	private String redirectByRole(int roleId) {
		switch (roleId) {
			case 3: // Admin
				return "redirect:/admin/home";
			case 2: // Manager
				return "redirect:/manager/home";
			case 1: // User
				return "redirect:/user/home";
			default:
				return "redirect:/login";
		}
	}
}