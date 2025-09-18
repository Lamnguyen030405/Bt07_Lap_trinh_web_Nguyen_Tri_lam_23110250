package vn.iotstar.controllers.manager;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("managerHomeController")
public class HomeController {

    @GetMapping("/manager/home")
    public String home() {
        return "manager/home"; // Trả về file /WEB-INF/views/manager/home.jsp
    }
}
