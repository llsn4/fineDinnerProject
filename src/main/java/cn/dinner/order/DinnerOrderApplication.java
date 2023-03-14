package cn.dinner.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 86139
 */
@SpringBootApplication
@Slf4j
//@ServletComponentScan
@EnableTransactionManagement
public class DinnerOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(DinnerOrderApplication.class, args);
    }

}
