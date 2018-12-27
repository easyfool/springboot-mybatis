package com.wangfengbabe.learn.springboot.mybatis.service.impl;

import com.wangfengbabe.learn.springboot.mybatis.domain.Account;
import com.wangfengbabe.learn.springboot.mybatis.mapper.AccountMapper;
import com.wangfengbabe.learn.springboot.mybatis.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service

public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;


    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public int addNewAccount(Account account) {
        return accountMapper.insert(account);
    }
}
