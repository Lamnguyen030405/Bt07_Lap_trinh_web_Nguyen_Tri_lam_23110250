package vn.iotstar.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("adminHomeController")
public class HomeController {

    @GetMapping("/admin/home")
    public String home() {
        return "admin/home"; // Trả về file /WEB-INF/views/admin/home.jsp
    }
}
