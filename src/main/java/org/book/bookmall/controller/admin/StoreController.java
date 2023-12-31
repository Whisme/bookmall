package org.book.bookmall.controller.admin;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.book.bookmall.entity.Store;
import org.book.bookmall.entity.User;
import org.book.bookmall.service.IStoreService;
import org.book.bookmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/admin/store")
@RequiresPermissions("store-manage")
public class StoreController {
    @Autowired
    private IStoreService storeService;

    @RequestMapping("/list")
    @RequiresPermissions("store-list")
    public String storeList(Model model){
        List<Store> stores = storeService.findStores();
        model.addAttribute("stores", stores);
        return "admin/store/list";
    }


    @Autowired
    private IUserService userService;

    @Value("${business.role-id}")
    private String business;

    @GetMapping("/toAddition")
    @RequiresPermissions("store-add")
    public String addStore(Model model){
        List<User> users = userService.findBusinesses(Integer.parseInt(business));
        model.addAttribute("users", users);
        return "admin/store/add";
    }

    @RequestMapping("/addition")
    @RequiresPermissions("store-add")
    public String addStore(Store store){
        storeService.addStore(store);
        return "redirect:/admin/store/list";
    }

    @RequestMapping("/{storeId}")
    @RequiresPermissions("store-edit")
    public String toEdit(@PathVariable("storeId") int storeId, Model model){
        Store store = storeService.findById(storeId);
        model.addAttribute("store", store);
        return "admin/store/edit";
    }

    @PostMapping("/edit")
    @RequiresPermissions("store-edit")
    public String editStore(Store store,Model model){
        storeService.updateStore(store);
        model.addAttribute("saveMsg", "????");
        return "forward:"+store.getStoreId();
    }

    @RequestMapping("/deletion/{storeId}")
    @RequiresPermissions("store-delete")
    public String deleteStore(@PathVariable("storeId") int storeId){
        storeService.deleteStore(storeId);
        return "redirect:/admin/store/list";
    }
}
