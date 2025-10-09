package de.thk.gm.gdw.fitamcampus_muhammedali_garanli.loginRegister;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginRegisterController {

    @GetMapping("/register")
    public String register() {
        return "register";
    }
}
