package com.demo.main;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RestController
public class DemoApplication {
        public static void main(String[] args) {
                SpringApplication.run(DemoApplication.class, args);
        }
		

    @PostMapping("/postdata")
    public void postData(@RequestBody String str) {
        System.out.println("inside postData");
    }


      @Bean
      public CommonsRequestLoggingFilter logFilter() {
          CommonsRequestLoggingFilter filter
            = new CommonsRequestLoggingFilter();
          filter.setIncludeQueryString(true);
          filter.setIncludePayload(true);
          filter.setMaxPayloadLength(10000);
          filter.setIncludeHeaders(false);
          filter.setAfterMessagePrefix("REQUEST DATA : ");
          return filter;
      }

}

