package vn.iotstar.controllers.user;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import jakarta.servlet.http.Part;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entities.UserEntity;
import vn.iotstar.services.IUserService;
import vn.iotstar.utils.UploadUtils;
import vn.iotstar.utils.path.Constant;

@Controller("userProfileController")
@RequestMapping("/user/profile")
public class ProfileController {

    @Autowired
    private IUserService userService;

    @GetMapping
    public String showProfile(HttpSession session, ModelMap model) {
        UserEntity user = (UserEntity) session.getAttribute("account");

        if (user == null) {
            return "redirect:/login"; // chuyển về login nếu chưa đăng nhập
        }

        model.addAttribute("user", user);
        return "user/profile";
    }

    @PostMapping
    public String updateProfile(
            @RequestPart("image") Part file,
            HttpSession session,
            ModelMap model,
            String fullname,
            String phone) {

        UserEntity user = (UserEntity) session.getAttribute("account");

        if (user == null) {
            return "redirect:/login";
        }

        try {
            user.setFullname(fullname);
            user.setPhone(phone);

            if (file != null) {
                String fileName = UploadUtils.processUploadFile(file, Constant.DIR + "/avatar");
                user.setImage(fileName);
            }

            userService.save(user);
            session.setAttribute("account", user); // cập nhật lại session
            return "redirect:/user/home";

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Cập nhật thất bại");
            return "user/profile";
        }
    }
}
