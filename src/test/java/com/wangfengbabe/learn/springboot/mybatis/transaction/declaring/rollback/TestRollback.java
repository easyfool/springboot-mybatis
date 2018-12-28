package com.wangfengbabe.learn.springboot.mybatis.transaction.declaring.rollback;

import com.wangfengbabe.learn.springboot.mybatis.domain.Account;
import com.wangfengbabe.learn.springboot.mybatis.mapper.AccountMapper;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * spring 中@Transactional注解是基于aop代理的，因此不能对方法所在的类中的其他方法进行注解使用，否则不起作用。
 * 将需要@Tansactional注解的方法移到其他类里面是一种方式。
 * 另外一种方式就是使用编程式事务处理
 */
@org.springframework.stereotype.Service
class Service {

    @Autowired
    private AccountMapper accountMapper;

    @Transactional(rollbackFor = {Exception.class})
    public void insert() throws Exception {
        Account account = new Account();
        account.setUserName("王峰");
        account.setBalance(100D);
        account.setUserId(10000L);
        int insert = accountMapper.insert(account);
        boolean hasTransactionInfo = TransactionInfoSupport.hasTransactionInfo();
        if (hasTransactionInfo) {
            System.out.println("has transaction");
        } else {
            System.out.println("no transaction");
        }


//TODO 请关注这个工具方法
//        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

        throw new Exception();
    }

}

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRollback {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Service service;

    @Test
    @Transactional(rollbackFor = {RuntimeException.class})
    public void test() throws Exception {
        service.insert();
    }

    @Test
    public void testInConcurrency() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    service.insert();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //主线程必须保证在最后执行完成，否则thread中会报connection has been closed
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
