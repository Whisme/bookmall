package org.book.bookmall.service.impl;
import org.book.bookmall.dao.RoleMapper;
import org.book.bookmall.dao.UserRoleMapper;
import org.book.bookmall.entity.Role;
import org.book.bookmall.entity.UserRole;
import org.book.bookmall.service.IRoleService;
import org.book.bookmall.utils.BSResult;
import org.book.bookmall.utils.BSResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import java.util.Date;
import java.util.List;

@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired(required = false)
    private RoleMapper roleMapper;

    @Autowired(required = false)
    private UserRoleMapper userRoleMapper;

    @Override
    public List<Role> findAllRoles() {
        return roleMapper.selectAll();
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames="authorizationCache",allEntries = true)
    public BSResult updateUserRoleRelationship(Integer userId, int[] roleIds) {
        if(userId != null && roleIds != null && roleIds.length != 0 ){
            Example example = new Example(UserRole.class);
            example.createCriteria().andEqualTo("userId", userId);
            userRoleMapper.deleteByExample(example);
            for (int roleId : roleIds) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRole.setCreated(new Date());
                userRole.setUpdated(new Date());
                userRoleMapper.insert(userRole);
            }
        }
        return BSResultUtil.success();
    }

    @Override
    public Role findById(int roleId) {
        return roleMapper.selectByPrimaryKey(roleId);
    }

    @Override
    @CacheEvict(cacheNames="authorizationCache",allEntries = true)
    public BSResult deleteById(int roleId) {
        roleMapper.deleteByPrimaryKey(roleId);
        return BSResultUtil.success();
    }

    @Override
    @Transactional
    public BSResult updateRole(Role role) {
        role.setUpdated(new Date());
        roleMapper.updateByPrimaryKeySelective(role);
        return BSResultUtil.success();
    }

    @Override
    @Transactional
    public BSResult addRole(Role role) {
        role.setCreated(new Date());
        role.setUpdated(new Date());
        roleMapper.insert(role);
        return BSResultUtil.success();
    }
}
