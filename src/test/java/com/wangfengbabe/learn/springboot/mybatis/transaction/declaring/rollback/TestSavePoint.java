package com.wangfengbabe.learn.springboot.mybatis.transaction.declaring.rollback;

import com.wangfengbabe.learn.springboot.mybatis.domain.Account;
import com.wangfengbabe.learn.springboot.mybatis.mapper.AccountMapper;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * 判断当前的环境是不是有事务，经过对Spring源码的研究，
 * 找到了一个transactionInfo，只要判断这个是否为空即可判断当前是不是在事务的环境中
 */
class TransactionInfoSupport extends TransactionAspectSupport {

    public static boolean hasTransactionInfo() throws NoTransactionException {
        return currentTransactionInfo() != null;
    }

}

@Service
class MyServiceTest {

    @Autowired
    private AccountMapper accountMapper;

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void testRun() {
        Account account = new Account();
        account.setUserId(1002L);
        account.setBalance(1000D);
        account.setUserName("蛋蛋");
        accountMapper.insert(account);
        boolean hasTransactionInfo = TransactionInfoSupport.hasTransactionInfo();
        if (hasTransactionInfo) {
            System.out.println("has transaction");
        } else {
            System.out.println("no transaction");
        }
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Object savepoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
        Account account2 = new Account();
        account2.setUserId(1003L);
        account2.setBalance(100D);
        account2.setUserName("点点");
        accountMapper.insert(account2);
        TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savepoint);

    }
}


@SpringBootTest
@RunWith(SpringRunner.class)
//@Transactional
public class TestSavePoint {

    @Autowired
    private MyServiceTest myServiceTest;

    @Test
//    @Transactional
    public void testSavePoint() {
        myServiceTest.testRun();

    }

}
