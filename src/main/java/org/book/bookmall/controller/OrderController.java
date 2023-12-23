package org.book.bookmall.controller;
import org.book.bookmall.entity.Cart;
import org.book.bookmall.entity.OrderCustom;
import org.book.bookmall.entity.User;
import org.book.bookmall.service.ICartService;
import org.book.bookmall.service.IOrderService;
import org.book.bookmall.utils.BSResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/order")
@CrossOrigin( originPatterns = "*", allowCredentials = "true", allowedHeaders = "*")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private ICartService cartService;

    @PostMapping("/payOrder")
    @ResponseBody
    public String payOrder(@RequestBody Map<String,Object> map, HttpServletRequest request) {

        User userDTO = new User();
        userDTO.setUserId(Integer.parseInt(map.get("userId").toString()));
        userDTO.setZipCode("000");

        //?????
        Cart cart = (Cart) request.getSession().getAttribute("cart");
        String bs = map.get("bookIds").toString();
        String[] bookIds = bs.split(",");
        Long[] bIds = new Long[bookIds.length];
        for (int i = 0; i < bookIds.length; i++) {
            bIds[i] = Long.valueOf(bookIds[i]);
        }
        Cart orderCart = (Cart)cartService.orderCart(cart, bIds).getData();
        if (cart != null) {
            BSResult bsResult = orderService.createOrder(orderCart, userDTO, "????", 1, 1);
            if (bsResult.getCode() == 200) {
                //request.setAttribute("order", bsResult.getData());
                //cartService.clearCart(request, "cart");
                return "payment";
            } else {
                request.setAttribute("exception", bsResult.getMessage());
                return "exception";
            }

        } else {
            request.setAttribute("exception", "??????");
            return "exception";
        }
    }

    @PostMapping("/createOrder")
    @ResponseBody
    public String createOrder(@RequestBody Map<String, Object> map, HttpServletRequest request) {

        User userDTO = new User();
        userDTO.setUserId(Integer.parseInt(map.get("userId").toString()));
        userDTO.setZipCode("000");

        //?????
        Cart cart = (Cart) request.getSession().getAttribute("cart");
        String bs = map.get("bookIds").toString();
        String[] bookIds = bs.split(",");
        Long[] bIds = new Long[bookIds.length];
        for (int i = 0; i < bookIds.length; i++) {
            bIds[i] = Long.valueOf(bookIds[i]);
        }
        Cart orderCart = (Cart) cartService.orderCart(cart, bIds).getData();
        if (cart != null) {
            BSResult bsResult = orderService.createOrder(orderCart, userDTO, "????", 1, 0);
            if (bsResult.getCode() == 200) {
                //request.setAttribute("order", bsResult.getData());
                //cartService.clearCart(request, "cart");
                return "createment";
            } else {
                request.setAttribute("exception", bsResult.getMessage());
                return "exception";
            }

        } else {
            request.setAttribute("exception", "??????");
            return "exception";
        }
    }
    @PostMapping("/list")
    @ResponseBody
    public List<OrderCustom> orderList(@RequestBody Map<String,Integer> map, HttpServletRequest request) {
        List<OrderCustom> orderCustoms = orderService.findOrdersByUserId(map.get("userId"));
        return orderCustoms;
    }
}
