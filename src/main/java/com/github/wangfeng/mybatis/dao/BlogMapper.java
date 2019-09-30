package com.github.wangfeng.mybatis.dao;


import com.github.wangfeng.mybatis.entity.BlogDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface BlogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BlogDO record);

    int insertSelective(BlogDO record);

    BlogDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BlogDO record);

    int updateByPrimaryKey(BlogDO record);

    @Select("select * from blog where id = #{id}")
    BlogDO findById(@Param("id") long id);
}