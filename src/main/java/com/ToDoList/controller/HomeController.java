package com.ToDoList.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * Created by awaeschoudhary on 2/9/17.
 */
@Controller
public class HomeController {

    @RequestMapping("/")
    public String home() {

        return "WEB-INF/pages/HelloWorld";
    }
}
