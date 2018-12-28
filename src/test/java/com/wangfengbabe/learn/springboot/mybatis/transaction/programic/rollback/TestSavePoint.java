package com.wangfengbabe.learn.springboot.mybatis.transaction.programic.rollback;

import com.wangfengbabe.learn.springboot.mybatis.domain.Account;
import com.wangfengbabe.learn.springboot.mybatis.mapper.AccountMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestSavePoint {

    @Autowired
    private PlatformTransactionManager txManager;
    @Autowired
    private AccountMapper accountMapper;

    @Test
    public void test() {
        Account account = new Account();
        account.setUserName("王峰");
        account.setBalance(100D);
        account.setUserId(10000L);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        Object savepoint = txManager.getTransaction(def).createSavepoint();

//        PlatformTransactionManager txManager = ContextLoader.getCurrentWebApplicationContext()
//                .getBean(PlatformTransactionManager.class);
        TransactionStatus status = txManager.getTransaction(def);
        try {
            int insert = accountMapper.insert(account);
        } finally {
//            txManager.rollback(status);
            txManager.getTransaction(def).rollbackToSavepoint(savepoint);
        }
    }

}
