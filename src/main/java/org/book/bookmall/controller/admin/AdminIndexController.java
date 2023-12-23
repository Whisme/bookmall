package org.book.bookmall.controller.admin;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.book.bookmall.entity.Store;
import org.book.bookmall.entity.User;
import org.book.bookmall.service.IStoreService;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminIndexController {

    @Autowired
    private IStoreService storeService;


    @RequestMapping({"", "/", "/index"})
    @RequiresPermissions("system")
    public String adminIndex() {
        return "admin/index";
    }

    @RequestMapping("/adminLogin")
    public String adminLogin(@RequestParam(value = "username", required = false) String username,
                             @RequestParam(value = "password", required = false) String password,
                             HttpServletRequest request, Model model) {
        //??????
        Subject userSubject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        token.setRememberMe(false);//???????
        try {
            //????
            userSubject.login(token);
            User loginUser = (User) userSubject.getPrincipal();
            request.getSession().setAttribute("loginUser", loginUser);
            Store store = storeService.findStoreByUserId(loginUser.getUserId());
            request.getSession().setAttribute("loginStore", store);
            String url = "/admin/index";
            if(StringUtils.isEmpty(url) || url.equals("/favicon.ico")){
                url = "/";
            }

            return "redirect:" + url;
        } catch (UnknownAccountException | IncorrectCredentialsException uae) {
            return "login";
        } catch (LockedAccountException lae) {
            model.addAttribute("loginMsg", "???????");
            return "login";
        } catch (AuthenticationException ae) {
            model.addAttribute("loginMsg", "?????");
            return "login";
        }

    }
}
