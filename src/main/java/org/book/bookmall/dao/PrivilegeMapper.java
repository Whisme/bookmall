package org.book.bookmall.dao;

import org.book.bookmall.common.MyMapper;
import org.book.bookmall.entity.Privilege;
import org.book.bookmall.utils.ZTreeNode;

public interface PrivilegeMapper extends MyMapper<ZTreeNode> {
    void updateByPrimaryKeySelective(Privilege privilege);

    void insert(Privilege privilege);
}
