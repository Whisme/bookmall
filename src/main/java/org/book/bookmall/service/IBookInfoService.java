package org.book.bookmall.service;
import com.github.pagehelper.PageInfo;
import org.book.bookmall.entity.Bar;
import org.book.bookmall.entity.BookInfo;
import org.book.bookmall.entity.Pie;
import org.book.bookmall.utils.BSException;
import org.book.bookmall.utils.BSResult;

import java.util.List;
public interface IBookInfoService {
    List<BookInfo> findBookListByCateId(int cateId, int currentPage, int pageSize);
    List<BookInfo> findHotBookList(int cateId, int i);
    BookInfo findById(Long bookId) throws BSException;
    int addLookMount(BookInfo bookInfo);
    BookInfo queryBookAvailable(Long bookId);
    BSResult saveBook(BookInfo bookInfo, String bookDescStr);
    PageInfo<BookInfo> findBookListByCondition(String keywords, int cateId, int page, int pageSize, int storeId);
    BSResult updateBook(BookInfo bookInfo, String bookDesc);
    BookInfo adminFindById(Long bookId) throws BSException;
    List<Pie> getBookViewsPiesByStoreId(Integer storeId);
    Bar getBookSalesBarJson(Integer storeId);
}

