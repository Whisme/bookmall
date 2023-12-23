package org.book.bookmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.book.bookmall.dao.CustomMapper;
import org.book.bookmall.dao.UserMapper;
import org.book.bookmall.dao.UserRoleMapper;
import org.book.bookmall.entity.User;
import org.book.bookmall.entity.UserRole;
import org.book.bookmall.service.IUserService;
import org.book.bookmall.utils.BSResult;
import org.book.bookmall.utils.BSResultUtil;
import org.book.bookmall.utils.UserResourceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 用户服务
 */
@Service
@EnableConfigurationProperties(UserResourceProperties.class)
public class UserServiceImpl implements IUserService {
    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired
    private UserResourceProperties userResourceProperties;

    /**
     * 检查用户名是否存在
     * @param username
     * @return
     */
    @Override
    public BSResult checkUserExistByUsername(String username) {

        Example userExample = new Example(User.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("username",username);
        List<User> users = userMapper.selectByExample(userExample);

        if(users == null || users.size() == 0){
            return BSResultUtil.build(200, "用户名可以使用", true);
        }else{
            return BSResultUtil.build(400, "用户名已被注册", false);
        }
    }

    /**
     * 注册用户,向数据库插入一条记录
     * @param user
     * @return
     */
    @Override
    @Transactional
    public BSResult saveUser(User user) {

        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        user.setCode(UUID.randomUUID().toString());
        //刚刚注册的用户处于未激活状态
        user.setActive(userResourceProperties.getIsNotActive());
        user.setLocation(userResourceProperties.getLocation());
        user.setDetailAddress(userResourceProperties.getDetailAddress());
        user.setCreated(new Date());
        user.setUpdated(new Date());

        userMapper.insert(user);
        return BSResultUtil.success(user);
    }

    @Override
    public PageInfo<User> findUserListByCondition(int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        Example userExample = new Example(User.class);
        List<User> users = userMapper.selectByExample(userExample);
        PageInfo<User> pageInfo = new PageInfo<>(users);
        return pageInfo;
    }

    @Override
    public User addUser(User user) {
        user.setActive("1");
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        user.setCreated(new Date());
        user.setUpdated(new Date());
        userMapper.insert(user);
        return user;
    }

    @Autowired(required = false)
    private UserRoleMapper userRoleMapper;

    @Override
    public BSResult updateUser(User user) {
        if(StringUtils.isEmpty(user.getPassword())){
            user.setPassword(null);
        }else{
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        }
        userMapper.updateByPrimaryKeySelective(user);
        return BSResultUtil.build(200, "????");
    }
    @Override
    public void delUser(int userId) {
        Example exampleOfUserRole = new Example(UserRole.class);
        exampleOfUserRole.createCriteria().andEqualTo("userId", userId);
        userRoleMapper.deleteByExample(exampleOfUserRole);
        userMapper.deleteByPrimaryKey(userId);
    }
    @Override
    public User findById(int userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Autowired(required = false)
    private CustomMapper customMapper;
    @Override
    public List<User> findBusinesses(int roleId) {
        return customMapper.findBusinesses(roleId);
    }
}
