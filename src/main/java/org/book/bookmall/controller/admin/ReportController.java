package org.book.bookmall.controller.admin;
import org.book.bookmall.entity.Bar;
import org.book.bookmall.entity.Pie;
import org.book.bookmall.entity.Store;
import org.book.bookmall.service.IBookInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/admin/report")
@RequiresPermissions("store-manage")
public class ReportController {
    @Autowired
    private IBookInfoService bookInfoService;

    /**
     * ???????????
     * @return
     */
    @RequestMapping("/views/pie")
    public List<Pie> getBookViewsPieJson(HttpSession session){
        Store loginStore = (Store) session.getAttribute("loginStore");
        if(loginStore == null){
            return new ArrayList<>();
        }
        return bookInfoService.getBookViewsPiesByStoreId(loginStore.getStoreId());
    }

    @RequestMapping("/sales/bar")
    public Bar getBookSalesBarJson(HttpSession session){
        Store loginStore = (Store) session.getAttribute("loginStore");
        if(loginStore == null){
            return null;
        }
        return bookInfoService.getBookSalesBarJson(loginStore.getStoreId());
    }
}
