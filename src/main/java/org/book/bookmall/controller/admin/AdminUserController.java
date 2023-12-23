package org.book.bookmall.controller.admin;
import com.github.pagehelper.PageInfo;
import org.book.bookmall.dao.CustomMapper;
import org.book.bookmall.entity.Role;
import org.book.bookmall.entity.User;
import org.book.bookmall.service.IRoleService;
import org.book.bookmall.service.IUserService;
import org.book.bookmall.utils.BSResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/user")
public class AdminUserController {
    @Autowired
    private IUserService userService;

    @RequestMapping("/list")
    @RequiresPermissions("user-list")
    public String userList(Model model, @RequestParam(value = "page",defaultValue = "1",required = false) int page){
        PageInfo<User> users = userService.findUserListByCondition(page,10);
        model.addAttribute("users", users);
        return "admin/user/list";
    }


    @Autowired
    private IRoleService roleService;

    @RequestMapping("/toAddition")
    @RequiresPermissions("user-add")
    public String toAddition(Model model){
        //??????
        model.addAttribute("allRoles", roleService.findAllRoles());
        return "admin/user/add";
    }

    @RequestMapping("/addition")
    @RequiresPermissions("user-add")
    public String addUser(User user, int[] roleIds){
        User userFromDB = userService.addUser(user);
        //???????????
        roleService.updateUserRoleRelationship(userFromDB.getUserId(),roleIds);
        return "forward:list";
    }

    @Autowired(required = false)
    private CustomMapper customMapper;

    @RequestMapping("/echo/{userId}")
    @RequiresPermissions("user-edit")
    public String echo(@PathVariable("userId") int userId,Model model){
        User user = userService.findById(userId);
        List<Role> userRoles = customMapper.findRolesByUserId(userId);
        List<Role> allRoles = roleService.findAllRoles();
        model.addAttribute("user", user);
        model.addAttribute("userRoles", userRoles);
        model.addAttribute("allRoles", allRoles);
        return "admin/user/edit";
    }

    @RequestMapping("/update")
    @RequiresPermissions("user-edit")
    public String updateUser(User user, int[] roleIds, Model model){
        BSResult bsResult = userService.updateUser(user);
        if(bsResult.getCode() == 200){
            model.addAttribute("saveMsg","????");
        }
        //???????????
        roleService.updateUserRoleRelationship(user.getUserId(),roleIds);
        return "forward:/admin/user/echo/"+user.getUserId();
    }

    @RequestMapping("/deletion/{userId}")
    @RequiresPermissions("user-delete")
    public String delUser(@PathVariable("userId") int userId){
        userService.delUser(userId);
        return "redirect:/admin/user/list";
    }


}
