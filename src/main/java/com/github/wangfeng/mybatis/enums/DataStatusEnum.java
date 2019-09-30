package com.github.wangfeng.mybatis.enums;

public enum DataStatusEnum {
    UNAUDITED("未审核", 0), AUDIT("待审核", 1), AUDITED("已审核", 2);

    private String label;

    private Integer value;

    DataStatusEnum(String label, Integer value) {
        this.label = label;
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public static DataStatusEnum getByValue(Integer value) {
        for (DataStatusEnum enum1 : values()) {
            if (enum1.getValue() == value) {
                return enum1;
            }
        }
        return null;
    }


}
