package org.book.bookmall.service;
import org.book.bookmall.entity.Role;
import org.book.bookmall.utils.BSResult;
import java.util.List;
public interface IRoleService {
    List<Role> findAllRoles();
    BSResult updateUserRoleRelationship(Integer userId, int[] roleIds);
    Role findById(int roleId);
    BSResult deleteById(int roleId);
    BSResult updateRole(Role role);
    BSResult addRole(Role role);
}
