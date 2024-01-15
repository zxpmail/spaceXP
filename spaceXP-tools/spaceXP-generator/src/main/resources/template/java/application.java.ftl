package ${package}.${moduleName};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class ${moduleName?cap_first}Application {

    public static void main(String[] args) {
        SpringApplication.run(${moduleName?cap_first}Application.class, args);
    }

}
