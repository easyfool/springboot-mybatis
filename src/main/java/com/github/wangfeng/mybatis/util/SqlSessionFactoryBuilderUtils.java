package com.github.wangfeng.mybatis.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * SqlSessionFactory 构造
 */
public class SqlSessionFactoryBuilderUtils {
    /**
     * 从文件构造
     *
     * @param resourcePath
     * @return
     * @throws IOException
     */
    public static SqlSessionFactory buildSqlSessionFactoryFromFile(String resourcePath) throws IOException {
//        String resource = "mybatis/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resourcePath);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        return sqlSessionFactory;
    }
}
