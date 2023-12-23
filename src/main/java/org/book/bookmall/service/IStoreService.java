package org.book.bookmall.service;
import org.book.bookmall.entity.Store;
import org.book.bookmall.utils.BSResult;

import java.util.List;

public interface IStoreService {
    Store findStoreByUserId(Integer userId);
    List<Store> findStores();
    BSResult addStore(Store store);
    Store findById(int storeId);
    BSResult updateStore(Store store);
    BSResult deleteStore(int storeId);
}
