package eu.arima.fileencodingdetection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication
public class FileEncodingApplication {

  public static void main(final String[] args) {
    SpringApplication.run(FileEncodingApplication.class, args);
  }

  @Configuration
  public static class MyWebMvcConfig {

    @Bean
    public WebMvcConfigurer forwardToIndex() {
      return new WebMvcConfigurer() {
        @Override
        public void addViewControllers(final ViewControllerRegistry registry) {
          registry.addRedirectViewController("/", "/fileencoding");
        }
      };
    }

  }
}
