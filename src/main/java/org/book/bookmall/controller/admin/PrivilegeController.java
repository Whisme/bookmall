package org.book.bookmall.controller.admin;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.book.bookmall.entity.Privilege;
import org.book.bookmall.service.IPrivilegeService;
import org.book.bookmall.utils.BSResult;
import org.book.bookmall.utils.ZTreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

@Controller
@RequestMapping("admin/privilege")
@RequiresPermissions("privilege-manage")
public class PrivilegeController {
    @Autowired(required = false)
    private IPrivilegeService privilegeService;

    @RequestMapping("/list")
    @RequiresPermissions("privilege-list")
    public String privilegeList(){
        return "admin/privilege/list";
    }

    @ResponseBody
    @RequestMapping("/treeNodes")
    public List<ZTreeNode> treeNodesJsonData(){
        return privilegeService.getZTreeNodes();
    }

    @RequestMapping("/addition")
    @ResponseBody
    @RequiresPermissions("privilege-add")
    public BSResult addPrivilege(Privilege privilege){
        ////?????????????????
        BSResult bsResult = privilegeService.addPrivilege(privilege);
        return bsResult;
    }

    @ResponseBody
    @RequestMapping("/edit")
    @RequiresPermissions("privilege-edit")
    public BSResult editPrivilege(Privilege privilege){
        BSResult bsResult = privilegeService.updatePrivilege(privilege);
        return bsResult;
    }

    @RequestMapping("/deletion/{privId}")
    @ResponseBody
    @RequiresPermissions("privilege-delete")
    public BSResult deletePrivilege(@PathVariable("privId") int privId){
        BSResult bsResult = privilegeService.deleteById(privId);
        return bsResult;
    }

    @ResponseBody
    @RequestMapping("/{privId}")
    public BSResult getPrivilege(@PathVariable("privId") int privId){
        return privilegeService.findById(privId);
    }
    @RequestMapping("/toEdit/{roleId}")
    @RequiresPermissions("privilege-edit")
    public String toEditPrivilege(@PathVariable("roleId") int roleId, Model model){
        model.addAttribute("roleId", roleId);
        return "admin/privilege/edit";
    }

}
