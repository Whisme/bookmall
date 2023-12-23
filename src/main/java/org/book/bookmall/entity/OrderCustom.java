package org.book.bookmall.entity;
import java.util.List;
public class OrderCustom {
    private String orderId;
    private Orders order;
    private OrderShipping orderShipping;
    /**
     * ????????????
     */
    private List<OrderDetail> orderDetails;

    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public Orders getOrder() {
        return order;
    }
    public void setOrder(Orders order) {
        this.order = order;
    }
    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }
    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
    public OrderShipping getOrderShipping() {
        return orderShipping;
    }

    public void setOrderShipping(OrderShipping orderShipping) {
        this.orderShipping = orderShipping;
    }
}
