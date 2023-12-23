package org.book.bookmall.controller.admin;

import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.book.bookmall.dao.BookDescMapper;
import org.book.bookmall.entity.BookDesc;
import org.book.bookmall.entity.BookInfo;
import org.book.bookmall.entity.Store;
import org.book.bookmall.service.IBookInfoService;
import org.book.bookmall.utils.BSException;
import org.book.bookmall.utils.IDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/book")
@RequiresPermissions("book-manage")
public class AdminBookController {
    @Autowired
    private IBookInfoService bookInfoService;
    @Value("${image.url.prefix}")
    private String urlPrefix;

    @RequestMapping("toAddition")
    @RequiresPermissions("book-add")
    public String toAddition(){
        return "admin/book/add";
    }
    @RequestMapping("/addition")
    @RequiresPermissions("book-add")
    public String addBook(BookInfo bookInfo, String bookDesc, MultipartFile pictureFile, HttpServletRequest request) throws Exception {
        uploadPicture(bookInfo, pictureFile, request);
        bookInfoService.saveBook(bookInfo, bookDesc);
        return "redirect:/admin/book/list";
    }

    private void uploadPicture(BookInfo bookInfo, MultipartFile pictureFile, HttpServletRequest request) throws IOException {
        if(pictureFile != null){
            if (StringUtils.isNotBlank(pictureFile.getOriginalFilename())) {
                String realPath = request.getSession().getServletContext().getRealPath("/" + urlPrefix);
                //??????
                String pictureFileName = pictureFile.getOriginalFilename();
                //?????
                String newFileName = IDUtils.genShortUUID() + pictureFileName.substring(pictureFileName.lastIndexOf("."));
                //????
                File uploadPic = new File(realPath + "/" + newFileName);
                //??????
                pictureFile.transferTo(uploadPic);
                bookInfo.setImageUrl(urlPrefix + "/" + newFileName);
            }
        }
    }

    @RequestMapping(value = "/list")
    @RequiresPermissions("book-query")
    public String bookList(@RequestParam(defaultValue = "", required = false) String keywords,
                           @RequestParam(value = "page",defaultValue = "1",required = false) int page,
                           HttpSession session,
                           Model model) {
        keywords = keywords.trim();
        Store store = (Store)session.getAttribute("loginStore");
        if(store != null){
            PageInfo<BookInfo> books = bookInfoService.findBookListByCondition(keywords,0,page,10,store.getStoreId());
            model.addAttribute("bookPageInfo", books);
            model.addAttribute("keywords", keywords);
        }else {
            model.addAttribute("exception", "?????????");
            return "exception";
        }
        return "admin/book/list";
    }

    @Autowired(required = false)
    private BookDescMapper bookDescMapper;

    @RequestMapping("/echo")
    @RequiresPermissions("book-edit")
    public String echo(Long bookId,Model model) throws BSException {
        BookInfo bookInfo = bookInfoService.adminFindById(bookId);
        BookDesc bookDesc = bookDescMapper.selectByPrimaryKey(bookInfo.getBookId());
        model.addAttribute("bookInfo", bookInfo);
        model.addAttribute("bookDesc", bookDesc);
        return "admin/book/edit";
    }

    @RequestMapping("/update")
    @RequiresPermissions("book-edit")
    public String updateBook(BookInfo bookInfo, String bookDesc, String keywords, MultipartFile pictureFile, HttpServletRequest request, RedirectAttributes ra) throws Exception {
        uploadPicture(bookInfo, pictureFile, request);
        bookInfoService.updateBook(bookInfo, bookDesc);
        ra.addAttribute("keywords", keywords);
        return "redirect:/admin/book/list";
    }
}
