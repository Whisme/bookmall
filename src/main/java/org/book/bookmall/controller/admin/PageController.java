package org.book.bookmall.controller.admin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * ??WEN-INF????
 * ????controller
 */
@Controller
public class PageController {

    //??????
    @RequestMapping("/403")
    public String to403(){
        return "403";
    }

    //????

    @RequestMapping("/admin/home")
    public String adminHome(){
        return "admin/home";
    }

    @RequestMapping("/admin/center")
    public String adminCenter(){
        return "admin/center";
    }

    @RequestMapping("/admin/right")
    public String adminRight(){
        return "admin/right";
    }

    @RequestMapping("/admin/left")
    public String adminLeft(){
        return "admin/left";
    }
    @RequestMapping("/admin/admintop")
    public String adminTop(){
        return "admin/admintop";
    }
}
