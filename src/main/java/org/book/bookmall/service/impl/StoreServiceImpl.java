package org.book.bookmall.service.impl;
import org.book.bookmall.dao.StoreMapper;
import org.book.bookmall.dao.UserMapper;
import org.book.bookmall.entity.Store;
import org.book.bookmall.entity.User;
import org.book.bookmall.service.IStoreService;
import org.book.bookmall.utils.BSResult;
import org.book.bookmall.utils.BSResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class StoreServiceImpl implements IStoreService {
    @Autowired(required = false)
    private StoreMapper storeMapper;

    @Override
    public Store findStoreByUserId(Integer userId) {
        Example example = new Example(Store.class);
        example.createCriteria().andEqualTo("storeManagerId", userId);
        List<Store> stores = storeMapper.selectByExample(example);
        if(stores !=null && stores.size() > 0){
            return stores.get(0);
        }
        return null;
    }

    @Autowired(required = false)
    private UserMapper userMapper;

    @Override
    public List<Store> findStores() {
        List<Store> stores = storeMapper.selectAll();
        stores.forEach(store -> {
            User user = userMapper.selectByPrimaryKey(store.getStoreManagerId());
            if(user != null){
                store.setStoreManagerName(user.getUsername());
            }
        });
        return stores;
    }

    @Override
    public BSResult addStore(Store store) {
        store.setCreated((Timestamp) new Date());
        store.setUpdated((Timestamp) new Date());
        storeMapper.insert(store);
        return BSResultUtil.success();
    }

    @Override
    @Transactional
    public BSResult updateStore(Store store) {
        store.setUpdated((Timestamp) new Date());
        storeMapper.updateByPrimaryKeySelective(store);
        return BSResultUtil.success();
    }

    @Override
    @Transactional
    public BSResult deleteStore(int storeId) {
        storeMapper.deleteByPrimaryKey(storeId);
        return BSResultUtil.success();
    }

    @Override
    public Store findById(int storeId) {
        Store store = storeMapper.selectByPrimaryKey(storeId);
        User user = userMapper.selectByPrimaryKey(store.getStoreManagerId());
        store.setStoreManagerName(user.getUsername());
        return store;
    }
}
