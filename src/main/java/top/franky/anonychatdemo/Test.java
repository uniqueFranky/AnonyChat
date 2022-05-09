package top.franky.anonychatdemo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Test {
    @RequestMapping("")
    public String index() {
        return "index";
    }

    @ResponseBody
    @RequestMapping("test")
    public String test() {
        return "123123123";
    }
}