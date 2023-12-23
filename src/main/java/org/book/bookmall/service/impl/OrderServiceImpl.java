package org.book.bookmall.service.impl;
import org.book.bookmall.dao.*;
import org.book.bookmall.entity.*;
import org.book.bookmall.service.IOrderService;
import org.book.bookmall.utils.BSResult;
import org.book.bookmall.utils.BSResultUtil;
import org.book.bookmall.utils.IDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired(required = false)
    private OrdersMapper orderMapper;

    @Autowired(required = false)
    private OrderShippingMapper orderShippingMapper;

    @Autowired(required = false)
    private OrderDetailMapper orderDetailMapper;

    @Autowired(required = false)
    private UserMapper userMapper;

    //????????????
    private final int NOT_COMPLETED = 0;

    @Override
    @Transactional
    public BSResult createOrder(Cart cart, User user, String express, int payMethod,int status) {
        Map<Long, CartItem> cartItems = cart.getCartItems();
        if(cartItems.size() > 0){
            Orders order = new Orders();
            String orderId = IDUtils.genOrderId();
            order.setOrderId(orderId);
            order.setUserId(user.getUserId());
            order.setCreateTime(new Date());
            order.setUpdateTime(new Date());
            order.setBuyerRate(NOT_COMPLETED);
            order.setShippingName(express);
            order.setOrderAmount(cartItems.size());
            order.setPayment(String.format("%.2f", cart.getTotal()));
            order.setPaymentType(payMethod);
            order.setStatus(status);
            order.setBuyerRate(NOT_COMPLETED);
            order.setPostFee("0.00");//??
            orderMapper.insert(order);

            OrderShipping orderShipping = new OrderShipping();
            orderShipping.setOrderId(orderId);
            orderShipping.setCreated(new Date());
            orderShipping.setUpdated(new Date());
            orderShipping.setReceiverAddress(user.getDetailAddress());
            orderShipping.setReceiverMobile(user.getPhone());
            orderShipping.setReceiverName(user.getUsername());
            orderShipping.setReceiverZip(user.getZipCode());
            //???????????????????????????
            orderShipping.setReceiverState("??");
            orderShipping.setReceiverCity("??");
            orderShipping.setReceiverDistrict("???");
            User u = userMapper.selectByPrimaryKey(user.getUserId());
            orderShipping.setReceiverAddress(u.getDetailAddress());
            orderShippingMapper.insert(orderShipping);

            List<OrderDetail> orderDetails = new ArrayList<>();
            for (Map.Entry<Long, CartItem> cartItemEntry : cartItems.entrySet()) {
                CartItem cartItem = cartItemEntry.getValue();
                if (cartItem.getBuyNum() > 0 && cartItem.isChecked()) {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrderId(orderId);
                    orderDetail.setBookId(cartItemEntry.getKey());
                    orderDetail.setMount(cartItem.getBuyNum());
                    orderDetail.setOrderNumber(orderId);
                    orderDetail.setPostStatus(NOT_COMPLETED + "");
                    orderDetail.setReceiveStatus(NOT_COMPLETED + "");
                    orderDetail.setStoreId(cartItem.getBookInfo().getStoreId());
                    orderDetail.setTotalPrice(BigDecimal.valueOf(cartItem.getSubTotal()));
                    orderDetail.setUnitPrice(cartItem.getBookInfo().getPrice());
                    orderDetail.setImageUrl(cartItem.getBookInfo().getImageUrl());
                    orderDetail.setBookName(cartItem.getBookInfo().getName());
                    orderDetails.add(orderDetail);
                    orderDetailMapper.insert(orderDetail);
                }
            }
            return BSResultUtil.success(order);
        }else
            return BSResultUtil.build(400, "???????????????!");
    }
    @Autowired(required = false)
    private CustomMapper customMapper;

    @Override
    public List<OrderCustom> findOrdersByUserId(int userId) {
        return customMapper.findOrdersByUserId(userId);
    }

    @Override
    public List<OrderCustom> findOrdersByStoreId(int storeId) {
        return null;
    }

    @Override
    public OrderCustom findOrderCustomById(String orderId) {
        return null;
    }

    @Override
    public BSResult updateOrder(OrderCustom orderCustom) {
        return null;
    }

}
