package com.cheer.demo.dto;

public class CategoryStatVO {

    // 种类
    private String category;

    // 种类数量
    private Long count;

    // 获取种类
    public String getCategory(){
        return category;
    }

    // 设置种类
    public void setCategory(String category){
        this.category = category;
    }

    // 获取种类数量
    public Long getCount(){
        return count;
    }
    
    // 设置种类数量
    public void setCount(Long count){
        this.count = count;
    }
}
