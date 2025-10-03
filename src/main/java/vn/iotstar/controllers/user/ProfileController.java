package vn.iotstar.controllers.user;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import jakarta.servlet.http.Part;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.dto.UserProfileDTO;
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
            return "redirect:/login";
        }

        model.addAttribute("userProfile", new UserProfileDTO(user.getFullname(), user.getPhone()));
        model.addAttribute("user", user); 
        return "user/profile";
    }


    @PostMapping
    public String updateProfile(
            @Valid @ModelAttribute("userProfile") UserProfileDTO dto,
            BindingResult result,
            @RequestPart(value = "image", required = false) Part file,
            HttpSession session,
            ModelMap model) {

        UserEntity user = (UserEntity) session.getAttribute("account");
        if (user == null) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            return "user/profile";
        }

        try {
            user.setFullname(dto.getFullname());
            user.setPhone(dto.getPhone());

            if (file != null && file.getSize() > 0) {
                String fileName = UploadUtils.processUploadFile(file, Constant.DIR + "/avatar");
                user.setImage(fileName);
            }

            userService.save(user);
            session.setAttribute("account", user);
            return "redirect:/user/home";

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Cập nhật thất bại");
            return "user/profile";
        }
    }
}
