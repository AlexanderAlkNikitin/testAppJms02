package com.anikitin.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by anikitin on 15.09.2016.
 */
@Controller
//@RequestMapping("/a")
public class HomeController {

    @RequestMapping(path = "/index")
    public String index() {
        return "index";
    }
}
