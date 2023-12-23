package org.book.bookmall.service;
import org.book.bookmall.entity.Privilege;
import org.book.bookmall.utils.BSResult;
import org.book.bookmall.utils.ZTreeNode;
import java.util.List;
public interface IPrivilegeService {
    List<ZTreeNode> getZTreeNodes();
    BSResult addPrivilege(Privilege privilege);
    BSResult updatePrivilege(Privilege privilege);
    BSResult deleteById(int privId);
    BSResult findById(int privId);
    List<ZTreeNode> getRolePrivileges(int roleId);

}
