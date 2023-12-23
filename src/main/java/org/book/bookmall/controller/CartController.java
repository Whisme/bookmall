package org.book.bookmall.controller;
import org.book.bookmall.entity.BookInfo;
import org.book.bookmall.entity.Cart;
import org.book.bookmall.service.IBookInfoService;
import org.book.bookmall.service.ICartService;
import org.book.bookmall.utils.BSResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/cart")
@CrossOrigin( originPatterns = "*", allowCredentials = "true", allowedHeaders = "*")
public class CartController {

    @GetMapping("/getCart")
    @ResponseBody
    public Cart getCart(HttpServletRequest request){
        Cart cart = (Cart)request.getSession().getAttribute("cart");
        return cart;
    }
    private IBookInfoService bookInfoService;

    @Autowired
    private ICartService cartService;

    @RequestMapping("/addition")
    @ResponseBody
    public String addToCart(@RequestParam(value = "bookId",defaultValue = "0") int bookId,
                            @RequestParam(required = false,defaultValue = "0") int buyNum,
                            HttpServletRequest request) {

        Cart cart = (Cart) request.getSession().getAttribute("cart");
        //?????????bookId??bookInfo
        BookInfo bookInfo = bookInfoService.queryBookAvailable((long) bookId);

        if (bookInfo != null) {
            //????????
            BSResult bsResult = cartService.addToCart(bookInfo, cart, buyNum);
            request.getSession().setAttribute("cart", bsResult.getData());
            request.setAttribute("bookInfo", bookInfo);
        } else {
            //?????????,?????
            request.setAttribute("bookInfo", null);
            return "fail";
        }
        return "addcart";
    }

    @PostMapping("/orderCart")
    @ResponseBody
    public BSResult orderCart(@RequestBody Map<String,Long[]> map, HttpServletRequest request){
        Cart cart = (Cart)request.getSession().getAttribute("cart");
        Long[] bookIds = map.get("bookIds");
        return cartService.orderCart(cart, bookIds);
    }
}
