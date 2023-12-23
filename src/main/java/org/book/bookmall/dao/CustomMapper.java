package org.book.bookmall.dao;
import org.book.bookmall.entity.*;

import java.util.List;
/**
 * ???mapper
 */
public interface CustomMapper {
    List<Role> findRolesByUserId(int userId);
    List<Privilege> findPrivilegesByRoleId(Integer roleId);
    List<OrderCustom> findOrdersByUserId(int userId);
    List<BookInfo> findRecommenderBooksByUserId(int userId);

    List<User> findBusinesses(int roleId);
    List<OrderCustom> findOrdersByStoreId(int storeId);

}
