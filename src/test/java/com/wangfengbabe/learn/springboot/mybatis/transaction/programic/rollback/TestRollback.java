package com.wangfengbabe.learn.springboot.mybatis.transaction.programic.rollback;

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
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;


@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRollback {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
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
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//        Object savepoint = txManager.getTransaction(def).createSavepoint();

//        PlatformTransactionManager txManager = ContextLoader.getCurrentWebApplicationContext()
//                .getBean(PlatformTransactionManager.class);
        TransactionStatus status = txManager.getTransaction(def);
        try {
            int insert = accountMapper.insert(account);
        } finally {
            txManager.rollback(status);
//            txManager.getTransaction(def).rollbackToSavepoint(savepoint);
        }
//        throw new RuntimeException();

    }

    @Test
    public void testInConcurrency() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//        PlatformTransactionManager txManager = ContextLoader.getCurrentWebApplicationContext()
//                .getBean(PlatformTransactionManager.class);
                TransactionStatus status = txManager.getTransaction(def);
                Account account = new Account();
                account.setUserName("王峰");
                account.setBalance(100D);
                account.setUserId(10000L);
                try {
                    int insert = accountMapper.insert(account);
                } finally {
                    logger.info("开始rollback");

                    txManager.rollback(status);
                }
            }
        }).start();
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
