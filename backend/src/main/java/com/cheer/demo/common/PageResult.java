package com.cheer.demo.common;

import java.util.List;

/**
 * 统一分页结果封装类（对应前端 types/task.ts 中的 PageResult<T>）
 */
public class PageResult<T> {

    /** 当前页的数据列表（Java 的 List<T> 对应前端 TS 的数组 T[]） */
    private List<T> records;

    /** 符合筛选条件的总记录条数（用于前端 el-pagination 计算总页数） */
    private Long total;

    /** 当前页码（从 1 开始） */
    private Long pageNum;

    /** 每页显示条数 */
    private Long pageSize;

    /** 无参构造方法（供框架序列化/反序列化使用） */
    public PageResult() {
    }

    /** 全参构造方法（供 Service 层一行代码快速组装分页结果） */
    public PageResult(List<T> records, Long total, Long pageNum, Long pageSize) {
        this.records = records;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    // ==================== 标准 Getter / Setter 方法 ====================

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getPageNum() {
        return pageNum;
    }

    public void setPageNum(Long pageNum) {
        this.pageNum = pageNum;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }
}
