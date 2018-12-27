package com.wangfengbabe.learn.springboot.mybatis.transaction.isolation;

import com.wangfengbabe.learn.springboot.mybatis.domain.Account;
import com.wangfengbabe.learn.springboot.mybatis.mapper.AccountMapper;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@RunWith(SpringRunner.class)
public class TestReadUnCommited {

    @Autowired
    private AccountMapper accountMapper;

    @Test
    public void test1() {
        Account account = new Account();
        account.setUserName("王峰");
        account.setBalance(100D);
        account.setUserId(10000L);

        accountMapper.insert(account);
        accountMapper.selectAll();

    }

    @Test
//    @Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRED)
    public void test() {
        ExecutorService threadpool = Executors.newFixedThreadPool(2);
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Account account = new Account();
                    account.setUserName("王峰");
                    account.setBalance(100D);
                    account.setUserId(10000L);
                    int insert = accountMapper.insert(account);
//                    try {
//                        TimeUnit.SECONDS.sleep(15);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } finally {
//                        throw new RuntimeException("异常");
//                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }


            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Account> accounts = accountMapper.selectAll();
                System.out.println("accounts:" + accounts);
            }
        });
        threadpool.submit(t1);
        threadpool.submit(t2);
        threadpool.shutdown();


    }


}
