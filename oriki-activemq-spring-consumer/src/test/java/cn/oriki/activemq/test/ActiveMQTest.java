package cn.oriki.activemq.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-mq.xml"})
public class ActiveMQTest {

    @Test
    public void test() {
        while (true) {

        }
    }
}
