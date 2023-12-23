package org.book.bookmall.service.impl;
import org.book.bookmall.entity.BookInfo;
import org.book.bookmall.entity.Cart;
import org.book.bookmall.entity.CartItem;
import org.book.bookmall.service.ICartService;
import org.book.bookmall.utils.BSResult;
import org.book.bookmall.utils.BSResultUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class CartServiceImpl implements ICartService {
    @Override
    public BSResult addToCart(BookInfo bookInfo, Cart cart, int buyNum) {
        //??????????
        if (cart == null) {
            cart = new Cart();
        }
        Map<Long, CartItem> cartItems = cart.getCartItems();
        double total = 0;
        if (cartItems.containsKey(bookInfo.getBookId())) {
            CartItem cartItem = cartItems.get(bookInfo.getBookId());
            cartItem.setBuyNum(cartItem.getBuyNum() + buyNum);
            cartItem.setSubTotal(cartItem.getBuyNum() * bookInfo.getPrice().doubleValue());
            cartItem.setBookInfo(bookInfo);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setBuyNum(buyNum);
            cartItem.setBookInfo(bookInfo);
            cartItem.setSubTotal(bookInfo.getPrice().doubleValue() * buyNum);
            cartItem.setSubTotal((double) Math.round(cartItem.getSubTotal() * 100) / 100);
            cartItems.put(bookInfo.getBookId(), cartItem);
        }
        for (CartItem cartItem : cartItems.values()) {
            total += cartItem.getSubTotal();
        }
        total = (double) Math.round(total * 100) / 100;
        cart.setTotal(total);
        return BSResultUtil.success(cart);
    }

    public BSResult orderCart(Cart cart,Long[] bookIds) {
        Cart orderCart = new Cart();
        Map<Long, CartItem> cartItems = cart.getCartItems();
        Map<Long, CartItem> cartItems2 = new HashMap<>();
        for(Long bookId : bookIds){
            if (cartItems.containsKey(bookId)) {
                cartItems2.put(bookId,cartItems.get(bookId));
            }
        }
        orderCart.setCartItems(cartItems2);
        orderCart.setTotal(cart.getTotal());
        return BSResultUtil.build(200,"OK",orderCart);
    }
}
