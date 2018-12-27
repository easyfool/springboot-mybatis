package com.wangfengbabe.learn.springboot.mybatis.transaction.isolation;

import com.wangfengbabe.learn.springboot.mybatis.domain.Account;
import com.wangfengbabe.learn.springboot.mybatis.mapper.AccountMapper;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.context.ContextLoader;


@SpringBootTest
@RunWith(SpringRunner.class)
public class TestReadUnCommited {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    CountDownLatch latch = new CountDownLatch(2);
    CountDownLatch first = new CountDownLatch(1);
    CountDownLatch second = new CountDownLatch(1);
    CountDownLatch third = new CountDownLatch(1);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PlatformTransactionManager txManager;

    @Test
    public void test() {
        Account account = new Account();
        account.setUserName("王峰");
        account.setBalance(100D);
        account.setUserId(10000L);

        accountMapper.insert(account);
        accountMapper.selectAll();

    }


    //    @Transactional(rollbackFor = {RuntimeException.class}, propagation = Propagation.REQUIRES_NEW)
    public void insert() {
        // spring无法处理thread的事务，声明式事务无效
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        PlatformTransactionManager txManager = ContextLoader.getCurrentWebApplicationContext()
                .getBean(PlatformTransactionManager.class);
        TransactionStatus status = txManager.getTransaction(def);
        Account account = new Account();
        account.setUserName("王峰");
        account.setBalance(100D);
        account.setUserId(10000L);
        int insert = accountMapper.insert(account);


    }




}
