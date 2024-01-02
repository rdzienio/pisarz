package pl.gienius.pisarz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PisarzApplication {

    public static void main(String[] args) {
        SpringApplication.run(PisarzApplication.class, args);
        Menu menu = new Menu();
        menu.init();
        System.out.println("You can close the application now...");
    }

}
