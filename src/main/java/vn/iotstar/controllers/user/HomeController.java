package vn.iotstar.controllers.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("userHomeController")
public class HomeController {

    @GetMapping("/user/home")
    public String home() {
        return "user/home"; // Trả về file /WEB-INF/views/user/home.jsp
    }
}
