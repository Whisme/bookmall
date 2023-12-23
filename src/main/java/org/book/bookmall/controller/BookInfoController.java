package org.book.bookmall.controller;
import org.book.bookmall.dao.BookDescMapper;
import org.book.bookmall.entity.BookDesc;
import org.book.bookmall.entity.BookInfo;
import org.book.bookmall.service.IBookInfoService;
import org.book.bookmall.utils.BSException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/book")
@CrossOrigin
public class BookInfoController {
    @Autowired
    private IBookInfoService bookInfoService;

    @Autowired(required = false)
    private BookDescMapper bookDescMapper;

    @RequestMapping("/getInfo/{bookId}")
    @ResponseBody
    public Map<String,Object> getInfo(@PathVariable("bookId") Long bookId, Model model) throws BSException {
        Map<String,Object> map =new HashMap<>();
        //????
        BookInfo bookInfo = bookInfoService.findById(bookId);
        //????????
        List<BookInfo> recommendBookList = bookInfoService.findBookListByCateId(bookInfo.getBookCategoryId(), 1, 5);
        //??????
        BookDesc bookDesc = bookDescMapper.selectByPrimaryKey(bookId);
        //?????
        bookInfoService.addLookMount(bookInfo);
        Collections.shuffle(recommendBookList);
        map.put("bookInfo", bookInfo);
        map.put("bookDesc", bookDesc);
        map.put("recommendBookList", recommendBookList);
        return map;
    }
}
