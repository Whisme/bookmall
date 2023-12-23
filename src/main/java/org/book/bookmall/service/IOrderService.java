package org.book.bookmall.service;
import org.book.bookmall.entity.Cart;
import org.book.bookmall.entity.OrderCustom;
import org.book.bookmall.entity.User;
import org.book.bookmall.utils.BSResult;

import java.util.List;

public interface IOrderService {
    BSResult createOrder(Cart cart, User user, String express, int payMethod, int status);
    List<OrderCustom> findOrdersByUserId(int userId);

    List<OrderCustom> findOrdersByStoreId(int storeId);
    OrderCustom findOrderCustomById(String orderId);
    BSResult updateOrder(OrderCustom orderCustom);
}
