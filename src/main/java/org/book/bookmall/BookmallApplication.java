package org.book.bookmall;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import tk.mybatis.spring.annotation.MapperScan;
@SpringBootApplication
@EnableCaching
@MapperScan("org.book.bookmall.dao")
@EnableAsync
public class BookmallApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookmallApplication.class, args);
    }
}
