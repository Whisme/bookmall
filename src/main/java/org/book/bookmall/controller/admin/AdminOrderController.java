package org.book.bookmall.controller.admin;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.book.bookmall.entity.*;
import org.book.bookmall.service.IOrderService;
import org.book.bookmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("admin/order")
@RequiresPermissions("order-manage")
public class AdminOrderController {
    @Autowired
    private IOrderService orderService;

    @RequestMapping("/list")
    @RequiresPermissions("order-list")
    public String orderList(HttpServletRequest request){
        Store loginStore = (Store) request.getSession().getAttribute("loginStore");
        List<OrderCustom> orderCustoms = orderService.findOrdersByStoreId(loginStore.getStoreId());
        request.setAttribute("orderCustoms", orderCustoms);
        return "admin/order/list";
    }
    @Autowired
    private IUserService userService;

    @RequestMapping("/toUpdate/{orderId}")
    @RequiresPermissions("order-edit")
    public String updateOrder(@PathVariable("orderId") String orderId, Model model) {
        OrderCustom orderCustom = orderService.findOrderCustomById(orderId);
        User buyer = userService.findById(orderCustom.getOrder().getUserId());
        model.addAttribute("orderCustom", orderCustom);
        model.addAttribute("buyer", buyer);
        return "admin/order/edit";
    }
    @RequestMapping("/update")
    @RequiresPermissions("order-edit")
    public String updateOrder(Orders order, OrderShipping orderShipping, Model model) {
        OrderCustom orderCustom = new OrderCustom();
        orderCustom.setOrder(order);
        orderCustom.setOrderShipping(orderShipping);
        orderService.updateOrder(orderCustom);
        model.addAttribute("saveMsg", "????");
        return "forward:toUpdate/"+order.getOrderId();
    }
}
