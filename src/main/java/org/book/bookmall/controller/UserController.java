package org.book.bookmall.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.book.bookmall.dao.CustomMapper;
import org.book.bookmall.entity.Role;
import org.book.bookmall.entity.Store;
import org.book.bookmall.entity.User;
import org.book.bookmall.service.IStoreService;
import org.book.bookmall.service.IUserService;
import org.book.bookmall.utils.BSResult;
import org.book.bookmall.utils.BSResultUtil;
import org.book.bookmall.utils.CookieUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.cache.annotation.CacheEvict;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    private IStoreService storeService;

    @Autowired(required = false)
    private CustomMapper customMapper;

    @RequestMapping("/loginnew")
    @ResponseBody
    public Map loginnew(@RequestParam(value = "username", required = false) String username,
                        @RequestParam(value = "password", required = false) String password,
                        HttpServletRequest request, HttpServletResponse response, Model model) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            map.put("status", "0");
            map.put("msg", "用户名或密码不能为空");
            return map;
        }
        //未认证的用户
        Subject userSubject = SecurityUtils.getSubject();
        if (!userSubject.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);

            token.setRememberMe(false);//禁止记住我功能
            try {
                //登录成功
                userSubject.login(token);
                User loginUser = (User) userSubject.getPrincipal();
                request.getSession().setAttribute("loginUser", loginUser);
                Store store = storeService.findStoreByUserId(loginUser.getUserId());
                request.getSession().setAttribute("loginStore", store);

                SavedRequest savedRequest = WebUtils.getSavedRequest(request);
                String url = "bookIndex";
                if (savedRequest != null) {
                    url = savedRequest.getRequestUrl();
                    if (url.contains(request.getContextPath())) {
                        url = url.replace(request.getContextPath(), "");
                    }
                }
                if (StringUtils.isEmpty(url) || url.equals("/favicon.ico")) {
                    url = "bookIndex";
                }
                map.put("status", "1");
                map.put("msg", url);
                map.put("user", loginUser);
                map.put("sessionId", request.getSession().getId());
                //判断是否仅仅是普通用户
                List<Role> roles = customMapper.findRolesByUserId(loginUser.getUserId());
                if (roles.size() == 1 && roles.get(0).getCode().equals("customer")) {
                    loginUser.setIdentity("ordinary");
                }
                CookieUtils.setCookie(request, response, "uuid", "uuid=" + UUID.randomUUID(), false);
                CookieUtils.setCookie(request, response, "userId", "userId=" + loginUser.getUserId(), false);
                CookieUtils.setCookie(request, response, "st", "st=" + new Date().getTime(), false);
                map.put("uuid", UUID.randomUUID());
                map.put("userId", loginUser.getUserId());
                map.put("st", new Date().getTime());

                return map;
            } catch (UnknownAccountException | IncorrectCredentialsException uae) {
                map.put("status", "0");
                map.put("msg", "用户名或密码错误");
            } catch (LockedAccountException lae) {
                map.put("status", "0");
                map.put("msg", "账户已被冻结");
            } catch (AuthenticationException ae) {
                map.put("status", "0");
                map.put("msg", "登录失败！");
            }
        } else {
            //用户已经登录
            map.put("status", "1");
            map.put("msg", "bookIndex");
        }
        return map;
    }

    @Autowired
    private IUserService userService;

    /**
     * 注册 检验用户名是否存在
     *
     * @param username
     * @return
     */
    @RequestMapping("/checkUserExist")
    @ResponseBody
    public BSResult checkUserExist(String username) {
        if (StringUtils.isEmpty(username)) {
            return BSResultUtil.build(200, "用户名不能为空", false);
        }

        return userService.checkUserExistByUsername(username);
    }

    /**
     * 注册，发激活邮箱
     *
     * @param user
     * @return
     */
    @RequestMapping("/register")
    @ResponseBody
    public Map register(@RequestBody User user) {
        Map map = new HashMap();
        BSResult isExist = checkUserExist(user.getUsername());
        //尽管前台页面已经用ajax判断用户名是否存在，
        // 为了防止用户不是点击前台按钮提交表单造成的错误，后台也需要判断
        if ((Boolean) isExist.getData()) {
            user.setNickname(user.getUsername());
            BSResult bsResult = userService.saveUser(user);
            User userNotActive = (User) bsResult.getData();
            map.put("status", 1);
            return map;
        } else {
            map.put("status", 0);
            map.put("msg", "用户已存在");
            return map;
        }
    }





    @RequestMapping("/logoutnew")
    @CacheEvict(cacheNames = "authorizationCache", allEntries = true)
    @ResponseBody
    public String logoutnew(HttpServletRequest request, HttpServletResponse response) {
        SecurityUtils.getSubject().logout();
        CookieUtils.deleteCookie(request, response, "uuid");
        CookieUtils.deleteCookie(request, response, "userId");
        CookieUtils.deleteCookie(request, response, "st");
        return "logoutnew";
    }
}
