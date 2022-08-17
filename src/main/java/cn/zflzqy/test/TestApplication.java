package cn.zflzqy.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.function.Function;

@SpringBootApplication
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
    // 很简单，定义一个方法，接收一个方法
    public static void cs(Function function){
        function.apply(null);
    }

}
