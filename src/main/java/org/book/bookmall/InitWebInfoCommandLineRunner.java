package org.book.bookmall;
import org.book.bookmall.entity.BookCategory;
import org.book.bookmall.service.IBookCateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import javax.servlet.ServletContext;
import java.util.List;
@Component
public class InitWebInfoCommandLineRunner implements CommandLineRunner {
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private IBookCateService bookCateService;
    @Override
    public void run(String... args){
        List<BookCategory> bookCategories = bookCateService.getCategoryList();
        servletContext.setAttribute("bookCategories", bookCategories);
    }
}
