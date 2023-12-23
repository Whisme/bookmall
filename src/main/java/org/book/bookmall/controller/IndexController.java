package org.book.bookmall.controller;
import org.book.bookmall.dao.CustomMapper;
import org.book.bookmall.entity.BookCategory;
import org.book.bookmall.entity.BookInfo;
import org.book.bookmall.service.IBookCateService;
import org.book.bookmall.service.IBookInfoService;
import org.book.bookmall.utils.BSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
import java.util.Random;

@Controller
@CrossOrigin
public class IndexController {
    @Autowired
    private IBookInfoService bookInfoService;

    @Autowired
    private IBookCateService cateService;

    private List<BookCategory> categoryList;


    //????
    @RequestMapping("/newBooks")
    @ResponseBody
    public List<BookInfo> newBooks() {
        if(categoryList == null){
            categoryList = cateService.getCategoryList();
        }
        List<BookInfo> bookInfos = bookInfoService.findBookListByCateId(categoryList.get(new Random().nextInt(6)).getCateId(), new Random().nextInt(3), 18);
        return bookInfos;
    }


    //????
    @RequestMapping("/bookCategories")
    @ResponseBody
    public List<BookCategory> bookCategories() {
        categoryList = cateService.getCategoryList();
        return categoryList;
    }

    //??????
    @RequestMapping("/hotBooks/{cateId}")
    @ResponseBody
    public List<BookInfo> hotBooks(@PathVariable("cateId") int cateId) {
        List<BookInfo> bookInfos = bookInfoService.findHotBookList(cateId,20);
        return bookInfos;
    }


    @Autowired(required = false)
    private CustomMapper customMapper;

    @RequestMapping("/recommenderBooks/{userId}")
    @ResponseBody
    public List<BookInfo> recommenderBooks(@PathVariable("userId") int userId) throws BSException {
        List<BookInfo> bookInfos =null;
        //?????userId??
        if(userId!=0) {
            bookInfos = customMapper.findRecommenderBooksByUserId(userId);
        }
        //????????????????????
        if(bookInfos==null||bookInfos.size()==0){
            if(categoryList == null){
                categoryList = cateService.getCategoryList();
            }
            bookInfos = bookInfoService.findHotBookList(categoryList.get(new Random().nextInt(6)).getCateId(), 6);
        }
        return bookInfos;
    }
}
