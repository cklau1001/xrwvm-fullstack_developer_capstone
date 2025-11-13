package io.cklau1001.capstone.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        log.debug("home() entered");
        return "Home";
    }

    @GetMapping("/about")
    public String about() {
        log.debug("about() entered");
        return "About";
    }

    @GetMapping("/contact")
    public String contact() {
        log.debug("contact() entered");
        return "Contact";
    }

    @GetMapping({"/login", "/register", "/dealers/**", "/dealer/**", "/postreview/**", "/searchcars/**"})
    public String routeToReact() {
        // render user registration function by a React generated index.html placed in /static folder
        log.info("[routeToReact] entered, forward to index.html");
        return "forward:/index.html";
    }

}
