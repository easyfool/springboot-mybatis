package com.wangfengbabe.learn.springboot.mybatis.transaction.declaring.isolation;

import com.wangfengbabe.learn.springboot.mybatis.domain.Account;
import com.wangfengbabe.learn.springboot.mybatis.mapper.AccountMapper;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.context.ContextLoader;

@Service
class ReadUncommited {

    @Autowired
    private AccountMapper accountMapper;
    private volatile CountDownLatch countDownLatch = new CountDownLatch(1);

    @Transactional
    public void insert() {
        Account account = new Account();
        account.setUserName("王峰");
        account.setBalance(100D);
        account.setUserId(10000L);
        accountMapper.insert(account);
        System.out.println("已完成insert");
        countDownLatch.countDown();
        try {
            TimeUnit.SECONDS.sleep(2);
            throw new RuntimeException("抛出此Exception 以使insert事务回滚.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void list() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("开始读取");
        List<Account> accounts = accountMapper.selectAll();
        System.out.println(accounts);
    }
}


@SpringBootTest
@RunWith(SpringRunner.class)
public class TestReadUnCommited {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ReadUncommited readUncommited;

//    CountDownLatch latch = new CountDownLatch(2);
//    CountDownLatch first = new CountDownLatch(1);
//    CountDownLatch second = new CountDownLatch(1);
//    CountDownLatch third = new CountDownLatch(1);
//
//    @Autowired
//    private AccountMapper accountMapper;

    @Test
    public void test() {
        CountDownLatch latch = new CountDownLatch(2);
        //完成insert之后再开始读取

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    readUncommited.insert();

                } catch (RuntimeException e) {

                } finally {
                    latch.countDown();
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                readUncommited.list();
                latch.countDown();
            }
        });
        t1.start();
        t2.start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    //    @Transactional(rollbackFor = {RuntimeException.class}, propagation = Propagation.REQUIRES_NEW)
    public void insert() {
        // spring无法处理thread的事务，声明式事务无效
//        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
//        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//        PlatformTransactionManager txManager = ContextLoader.getCurrentWebApplicationContext()
//                .getBean(PlatformTransactionManager.class);
//        TransactionStatus status = txManager.getTransaction(def);
//        Account account = new Account();
//        account.setUserName("王峰");
//        account.setBalance(100D);
//        account.setUserId(10000L);
//        int insert = accountMapper.insert(account);

    }


}
