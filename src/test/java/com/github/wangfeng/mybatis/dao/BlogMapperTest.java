package com.github.wangfeng.mybatis.dao;

import com.github.wangfeng.mybatis.entity.BlogDO;
import com.github.wangfeng.mybatis.enums.DataStatusEnum;
import com.github.wangfeng.mybatis.util.SqlSessionFactoryBuilderUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class BlogMapperTest {
    @Test
    public void test() throws IOException {
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryBuilderUtils.buildSqlSessionFactoryFromFile("mybatis/mybatis-config.xml");
        SqlSession sqlSession = sqlSessionFactory.openSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        BlogDO insertDO = new BlogDO();
        insertDO.setName("test");
        insertDO.setStatus(DataStatusEnum.AUDIT);
        mapper.insertSelective(insertDO);
        sqlSession.commit();
        System.out.println(insertDO.getId());
        Long id = insertDO.getId();
        BlogDO blogDO = mapper.selectByPrimaryKey(id);
        System.out.println(blogDO);

        BlogDO blogDO1 = mapper.findById(id);
        System.out.println(blogDO1);

        sqlSession.close();
    }

}