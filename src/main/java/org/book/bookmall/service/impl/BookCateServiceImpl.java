package org.book.bookmall.service.impl;

import org.book.bookmall.dao.BookCategoryMapper;
import org.book.bookmall.entity.BookCategory;
import org.book.bookmall.service.IBookCateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BookCateServiceImpl implements IBookCateService {
    @Autowired(required = false)
    private BookCategoryMapper categoryMapper;

    @Override
    public List<BookCategory> getCategoryList() {
        return categoryMapper.selectAll();

    }
}
