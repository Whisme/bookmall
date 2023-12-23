package org.book.bookmall.service;
import org.book.bookmall.entity.BookInfo;
import org.book.bookmall.entity.Cart;
import org.book.bookmall.utils.BSResult;
public interface ICartService {
    BSResult addToCart(BookInfo bookInfo, Cart cart,int buyNum);
    BSResult orderCart(Cart cart,Long[] bookIds);


}
