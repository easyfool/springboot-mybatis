package com.wangfengbabe.learn.springboot.mybatis.transaction.isolation;

import com.mysql.cj.util.TimeUtil;
import com.sun.media.jfxmedia.logging.Logger;
import com.wangfengbabe.learn.springboot.mybatis.domain.Account;
import com.wangfengbabe.learn.springboot.mybatis.mapper.AccountMapper;
import java.sql.Time;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
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

@org.springframework.stereotype.Service
@Transactional(rollbackFor = {
        RuntimeException.class}, isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRED)
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
        throw new Exception();
    }

}

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRollback {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private Service service;

    @Autowired
    private PlatformTransactionManager txManager;

    @Test
    @Transactional(rollbackFor = {
            RuntimeException.class}, isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRED)
    public void test() {

        Account account = new Account();
        account.setUserName("王峰");
        account.setBalance(100D);
        account.setUserId(10000L);
        int insert = accountMapper.insert(account);
        throw new RuntimeException();

    }

    @Transactional(rollbackFor = {Exception.class})
    public void insert() throws Exception {
        Account account = new Account();
        account.setUserName("王峰");
        account.setBalance(100D);
        account.setUserId(10000L);
        int insert = accountMapper.insert(account);
        throw new Exception();
    }

    @Test
    public void testInConcurrency() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    insert();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                DefaultTransactionDefinition def = new DefaultTransactionDefinition();
//                def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
////        PlatformTransactionManager txManager = ContextLoader.getCurrentWebApplicationContext()
////                .getBean(PlatformTransactionManager.class);
//                TransactionStatus status = txManager.getTransaction(def);
//                Account account = new Account();
//                account.setUserName("王峰");
//                account.setBalance(100D);
//                account.setUserId(10000L);
//                try {
//                    int insert = accountMapper.insert(account);
//                } finally {
//                    logger.info("开始rollback");
//
//                    txManager.rollback(status);
//                }
//            }
//        }).start();
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
