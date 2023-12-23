package org.book.bookmall.service;
import com.github.pagehelper.PageInfo;
import org.book.bookmall.entity.User;
import org.book.bookmall.utils.BSResult;

import java.util.List;

public interface IUserService {
    BSResult checkUserExistByUsername(String username);
    BSResult saveUser(User user);
    PageInfo<User> findUserListByCondition(int page, int pageSize);
    User addUser(User user);
    BSResult updateUser(User user);
    void delUser(int userId);
    User findById(int userId);
    List<User> findBusinesses(int parseInt);

}
