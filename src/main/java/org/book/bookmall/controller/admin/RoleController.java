package org.book.bookmall.controller.admin;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.book.bookmall.entity.Role;
import org.book.bookmall.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/admin/role")
@RequiresPermissions("role-manage")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @RequestMapping("/list")
    @RequiresPermissions("role-list")
    public String roleList(Model model){
        List<Role> allRoles = roleService.findAllRoles();
        model.addAttribute("allRoles", allRoles);
        return "admin/role/list";
    }

    @RequestMapping("/echo/{roleId}")
    @RequiresPermissions("role-edit")
    public String toEdit(@PathVariable("roleId") int roleId, Model model){
        Role role = roleService.findById(roleId);
        model.addAttribute("role", role);
        return "admin/role/edit";
    }
    @RequestMapping("/edit")
    @RequiresPermissions("role-edit")
    public String updateRole(Role role,Model model){
        roleService.updateRole(role);
        model.addAttribute("saveMsg", "????");
        return "forward:echo/"+role.getRoleId();
    }
    @RequestMapping("/deletion/{roleId}")
    @RequiresPermissions("role-delete")
    public String deleteRole(@PathVariable("roleId")int roleId){
        roleService.deleteById(roleId);
        return "redirect:/admin/role/list";
    }

    @RequestMapping("/toAddition")
    @RequiresPermissions("role-add")
    public String toAdd(){
        return "admin/role/add";
    }

    @RequestMapping("/addition")
    @RequiresPermissions("role-add")
    public String addRole(Role role){
        roleService.addRole(role);
        return "forward:list";
    }
}
