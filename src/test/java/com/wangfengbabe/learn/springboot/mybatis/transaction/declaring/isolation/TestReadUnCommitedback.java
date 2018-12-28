package com.wangfengbabe.learn.springboot.mybatis.transaction.declaring.isolation;

import com.wangfengbabe.learn.springboot.mybatis.domain.Account;
import com.wangfengbabe.learn.springboot.mybatis.mapper.AccountMapper;
import java.util.List;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestReadUnCommitedback {

    private final Logger logger = LoggerFactory.getLogger(TestReadUnCommitedback.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PlatformTransactionManager txManager;

    /**
     * 自动提交
     */
    @Test
    public void testAutoCommit() {
        Account insertAccount = new Account();
        insertAccount.setUserId(10001L);
        insertAccount.setUserName("王峰");
        insertAccount.setBalance(1000D);
        accountMapper.insert(insertAccount);
    }

    /**
     * 手动提交
     */
    @Test
    public void testManufacturalCommit() {
        //开启事务
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
//        def.setIsolationLevel(Isolation.READ_UNCOMMITTED.value());
//        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = txManager.getTransaction(def); // get
        Account insertAccount = new Account();
        insertAccount.setUserId(10001L);
        insertAccount.setUserName("王峰");
        insertAccount.setBalance(1000D);
        accountMapper.insert(insertAccount);
        //事务提交
        txManager.commit(status);


    }

    /**
     * Isolation:ReadUnCommited
     */
    @Test
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void readUncommited() {
//        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
//        def.setIsolationLevel(Isolation.READ_UNCOMMITTED.value());
////        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//        TransactionStatus status = txManager.getTransaction(def); // get
        List<Account> accounts = accountMapper.selectAll();
        System.out.println(accounts);
//        txManager.commit(status);

    }

    /**
     * rollback
     */
    @Test
    public void testRollback() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
//        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = txManager.getTransaction(def); // get
        Account insertAccount = new Account();
        insertAccount.setUserId(10001L);
        insertAccount.setUserName("王峰");
        insertAccount.setBalance(1000D);
        accountMapper.insert(insertAccount);
        //事务提交
        txManager.rollback(status);
    }


    @Test
//    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void testRunFirst() throws InterruptedException {

        Account insertAccount = new Account();
        insertAccount.setUserId(10001L);
        insertAccount.setUserName("王峰");
        insertAccount.setBalance(1000D);
        TransactionDefinition definition = new DefaultTransactionDefinition();
        ((DefaultTransactionDefinition) definition)
                .setIsolationLevel(TransactionDefinition.ISOLATION_READ_UNCOMMITTED);
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
        accountMapper.insert(insertAccount);
//        throw new RuntimeException("异常");

//        txManager.commit(status);
        TimeUnit.SECONDS.sleep(60);

//        TransactionAspectSupport.currentTransactionStatus().flush();
//        List<Account> accounts = accountMapper.selectAll();
        txManager.rollback(status);
//        System.out.println(accounts);

    }

    @Test
//    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void testRunSecond() {
        TransactionDefinition definition = new DefaultTransactionDefinition();
        ((DefaultTransactionDefinition) definition)
                .setIsolationLevel(TransactionDefinition.ISOLATION_READ_UNCOMMITTED);
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
        List<Account> accounts = accountMapper.selectAll();
        System.out.println(accounts);

    }

}
