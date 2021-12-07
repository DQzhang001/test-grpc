package com.convertlab.testdemos;

import com.atf.cap.api.APITest;
import com.convertlab.service.DemoService;
import com.convertlab.testNG.TestNGConfig;
import com.qa.testrail.annotations.TestRailCase;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

public class demoClient extends APITest {

    @Autowired
    private TestNGConfig config;

    @SneakyThrows
    @Test
    @TestRailCase("demo展示")
    public void helloClient(){
        DemoService demoService = new DemoService(config.getProperty("host"),config.getProperty("port"));
        demoService.afterPropertiesSet();
        String resp = demoService.sayHello("hahha");
        System.out.printf(" demoService.getChannel()= ", demoService.getChannel().getState(true));
        System.out.println("resp = " + resp);
    }
}
