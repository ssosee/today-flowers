package quokka.todayflowers.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class HomeController {
    @GetMapping("/home/{userId}")
    public String home(@PathVariable("userId") String userId, Model model) {

        System.out.println(userId);
        model.addAttribute("userId", userId);

        return "home";
    }

    @GetMapping("/home")
    public String home2() {
        return "home";
    }
}
