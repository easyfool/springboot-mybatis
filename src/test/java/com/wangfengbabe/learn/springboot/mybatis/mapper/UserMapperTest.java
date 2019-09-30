package com.wangfengbabe.learn.springboot.mybatis.mapper;

import static org.junit.Assert.*;

import com.wangfengbabe.learn.springboot.mybatis.domain.User;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

// 获取启动类，加载配置，确定装载 Spring 程序的装载方法，它回去寻找 主配置启动类（被 @SpringBootApplication 注解的）
@SpringBootTest
// 让 JUnit 运行 Spring 的测试环境， 获得 Spring 环境的上下文的支持
@RunWith(SpringRunner.class)
@Transactional(value = "transactionManager")
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testInsert() {
        User insertDO = new User();
        insertDO.setName("wangfeng");
        insertDO.setAge(30);
        insertDO.setMoney(100.00);

        int insert = userMapper.insert(insertDO);
        System.out.println(insert);

    }

    @Test

    public void testInsertInConcurrency() {
        CountDownLatch latch = new CountDownLatch(2);
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                User insertDO = new User();
                insertDO.setName("wangfeng");
                insertDO.setAge(30);
                insertDO.setMoney(100.00);
                int insert = userMapper.insert(insertDO);
                latch.countDown();

            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                List<User> userList = userMapper.selectAll();
                System.out.println(userList);
                latch.countDown();
            }
        });
        t1.start();
        t2.start();
        //如果此处不加await会导致主线程完成，t1 t2还没有执行或者在执行的时候connection已经关闭
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}