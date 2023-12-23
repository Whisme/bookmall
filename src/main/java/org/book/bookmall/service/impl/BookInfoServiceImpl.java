package org.book.bookmall.service.impl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.RowBounds;
import org.book.bookmall.dao.BookCategoryMapper;
import org.book.bookmall.dao.BookDescMapper;
import org.book.bookmall.dao.BookInfoMapper;
import org.book.bookmall.entity.Bar;
import org.book.bookmall.entity.BookDesc;
import org.book.bookmall.entity.BookInfo;
import org.book.bookmall.entity.Pie;
import org.book.bookmall.service.IBookInfoService;
import org.book.bookmall.utils.BSException;
import org.book.bookmall.utils.BSResult;
import org.book.bookmall.utils.BSResultUtil;
import org.book.bookmall.utils.IDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookInfoServiceImpl implements IBookInfoService {

    @Autowired(required = false)
    private BookInfoMapper bookInfoMapper;

    @Override
    @Cacheable(cacheNames = "book", key = "'bookInfo_'+#cateId+'_'+#currentPage+#pageSize")
    public List<BookInfo> findBookListByCateId(int cateId, int currentPage, int pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        Example bookInfoExample = new Example(BookInfo.class);
        Example.Criteria criteria = bookInfoExample.createCriteria();
        criteria.andEqualTo("bookCategoryId", cateId);
        criteria.andEqualTo("isShelf", 1);
        bookInfoExample.setOrderByClause("deal_mount DESC,look_mount DESC");
        List<BookInfo> books = bookInfoMapper.selectByExample(bookInfoExample);
        PageInfo<BookInfo> pageInfo = new PageInfo<>(books);
        return pageInfo.getList();
    }

    @Override
    public List<BookInfo> findHotBookList(int cateId, int top) {
        //????
        RowBounds rowBounds=new RowBounds(0,top);
        Example bookInfoExample = new Example(BookInfo.class);
        Example.Criteria criteria = bookInfoExample.createCriteria();
        if(cateId>0) {
            criteria.andEqualTo("bookCategoryId", cateId);
        }
        criteria.andEqualTo("isShelf", 1);
        bookInfoExample.setOrderByClause("deal_mount DESC,look_mount DESC");
        List<BookInfo> books = bookInfoMapper.selectByExampleAndRowBounds(bookInfoExample,rowBounds);
        return books;
    }

    @Autowired(required = false)
    private BookCategoryMapper categoryMapper;
    @Override
    @Cacheable(cacheNames = "book", key = "'bookInfo_'+#bookId")
    public BookInfo findById(Long bookId) throws BSException {
        Example bookInfoExample = new Example(BookInfo.class);
        Example.Criteria criteriaOfIsShelf = bookInfoExample.createCriteria();
        criteriaOfIsShelf.andEqualTo("isShelf", 1);
        criteriaOfIsShelf.andEqualTo("bookId", bookId);
        List<BookInfo> bookInfos = bookInfoMapper.selectByExample(bookInfoExample);
        if (bookInfos == null || bookInfos.size() == 0) {
            throw new BSException("??????????????");
        }
        BookInfo bookInfo = bookInfos.get(0);
        bookInfo.setName(categoryMapper.selectByPrimaryKey(bookInfo.getBookCategoryId()).getName());
        return bookInfo;
    }

    public int addLookMount(BookInfo bookInfo) {
        bookInfo.setLookMount(bookInfo.getLookMount() == null ? 0 : bookInfo.getLookMount() + 1);
        return bookInfoMapper.updateByPrimaryKeySelective(bookInfo);
    }
    public BookInfo queryBookAvailable(Long bookId) {
        Example bookInfoExample = new Example(BookInfo.class);
        Example.Criteria criteria = bookInfoExample.createCriteria();
        criteria.andEqualTo("bookId", bookId);
        criteria.andEqualTo("isShelf", 1);
        criteria.andGreaterThan("storeMount", 0);
        List<BookInfo> bookInfos = bookInfoMapper.selectByExample(bookInfoExample);
        if (bookInfos != null && !bookInfos.isEmpty()) {
            return bookInfos.get(0);
        }
        return null;
    }

    @Autowired(required = false)
    private BookDescMapper bookDescMapper;

    @Override
    @Transactional
    @CacheEvict(cacheNames = "book", allEntries = true)
    public BSResult saveBook(BookInfo bookInfo, String bookDescStr) {
        bookInfo.setIsbn(String.valueOf(IDUtils.genItemId()));
        bookInfo.setVersion("1.0");

        bookInfo.setDiscount(bookInfo.getPrice().divide(bookInfo.getMarketPrice(), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(10.0)));
        bookInfo.setLookMount(100);
        bookInfo.setDealMount(100);
        bookInfo.setPackStyle("??-??");
        bookInfo.setSize("16?");
        bookInfo.setIsShelf(1);
        bookInfoMapper.insert(bookInfo);
        BookDesc bookDesc = new BookDesc();
        bookDesc.setBookDesc(bookDescStr);
        bookDesc.setBookId(bookInfo.getBookId());
        bookDescMapper.insert(bookDesc);
        return BSResultUtil.success();
    }

    @Override
    public PageInfo<BookInfo> findBookListByCondition(String keywords, int cateId, int page, int pageSize, int storeId) {
        PageHelper.startPage(page, pageSize);
        Example bookInfoExample = new Example(BookInfo.class);
        if (!StringUtils.isEmpty(keywords)) {
            String s = "%" + keywords + "%";
            Example.Criteria criteriaOfKeywords = bookInfoExample.createCriteria();
            criteriaOfKeywords.orLike("name", s);
            criteriaOfKeywords.orLike("author", s);
            criteriaOfKeywords.orLike("isbn", s);
        }
        if (cateId != 0) {
            //???Id????,where (name like ? or author like ? or isbn like ?) and cateId = ?
            Example.Criteria criteriaOfCateId = bookInfoExample.createCriteria();
            criteriaOfCateId.andEqualTo("bookCategoryId", cateId);
            bookInfoExample.and(criteriaOfCateId);
        }
        if (storeId == 0) {
            //????????????
            Example.Criteria criteriaOfIsShelf = bookInfoExample.createCriteria();
            criteriaOfIsShelf.andEqualTo("isShelf", 1);
            bookInfoExample.and(criteriaOfIsShelf);
        } else {
            //????
            Example.Criteria criteriaOfStore = bookInfoExample.createCriteria();
            criteriaOfStore.andEqualTo("storeId", storeId);
            bookInfoExample.and(criteriaOfStore);
            bookInfoExample.setOrderByClause("store_time DESC");
        }
        List<BookInfo> books = bookInfoMapper.selectByExample(bookInfoExample);
        PageInfo<BookInfo> pageInfo = new PageInfo<>(books);
        return pageInfo;
    }

    @Override
    public BookInfo adminFindById(Long bookId) throws BSException {
        Example bookInfoExample = new Example(BookInfo.class);
        Example.Criteria criteriaOfIsShelf = bookInfoExample.createCriteria();
        criteriaOfIsShelf.andEqualTo("bookId", bookId);
        BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(bookId);
        if (bookInfo == null) {
            throw new BSException("?????????!");
        }
        return bookInfo;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "book", allEntries = true)
    public BSResult updateBook(BookInfo bookInfo, String bookDescStr) {
        bookInfo.setDiscount(bookInfo.getPrice().divide(bookInfo.getMarketPrice(), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(10.0)));
        bookInfoMapper.updateByPrimaryKeySelective(bookInfo);
        BookDesc bookDesc = new BookDesc();
        bookDesc.setBookDesc(bookDescStr);
        bookDesc.setBookId(bookInfo.getBookId());
        if (bookDescMapper.selectByPrimaryKey(bookInfo.getBookId()) == null) {
            bookDescMapper.insert(bookDesc);
            return BSResultUtil.success();
        }
        bookDescMapper.updateByPrimaryKeySelective(bookDesc);
        return BSResultUtil.success();
    }
    @Override
    public List<Pie> getBookViewsPiesByStoreId(Integer storeId) {
        //top 8
        PageHelper.startPage(1, 8);
        Example example = new Example(BookInfo.class);
        example.createCriteria().andEqualTo("storeId", storeId);
        example.setOrderByClause("look_mount DESC");
        List<BookInfo> bookInfos = bookInfoMapper.selectByExample(example);
        if (bookInfos == null || bookInfos.size() == 0) {
            return new ArrayList<>();
        }
        List<Pie> pies = new ArrayList<>();
        for (BookInfo bookInfo : bookInfos) {
            Pie pie = new Pie();
            pie.setName(bookInfo.getName());
            pie.setY(bookInfo.getLookMount());
            pies.add(pie);
        }
        pies.get(0).setSliced(true);
        pies.get(0).setSelected(true);
        return pies;
    }

    @Override
    public Bar getBookSalesBarJson(Integer storeId) {
        //top 6
        PageHelper.startPage(1, 6);
        Example example = new Example(BookInfo.class);
        example.createCriteria().andEqualTo("storeId", storeId);
        example.setOrderByClause("deal_mount DESC");
        List<BookInfo> bookInfos = bookInfoMapper.selectByExample(example);
        if (bookInfos == null || bookInfos.size() == 0) {
            return null;
        }
        Bar bar = new Bar();
        bar.setBookNames(bookInfos.stream().map(BookInfo::getName).collect(Collectors.toList()));
        bar.setSales(bookInfos.stream().map(BookInfo::getDealMount).collect(Collectors.toList()));
        return bar;
    }
}
