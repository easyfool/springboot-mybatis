package com.github.wangfeng.mybatis.entity;

import com.github.wangfeng.mybatis.enums.DataStatusEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class BlogDO implements Serializable {
    private Long id;

    private String name;

    private DataStatusEnum status;

    private static final long serialVersionUID = 1L;

}