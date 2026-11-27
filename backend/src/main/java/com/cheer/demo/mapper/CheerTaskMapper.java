package com.cheer.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cheer.demo.entity.CheerTask;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;
import com.cheer.demo.dto.CategoryStatVO;

import java.util.List;

/**
 * 任务数据持久层接口（Mapper / DAO 层，唯一与 MySQL 数据库直接打交道的地方）
 *
 * 核心看点：
 * 1. 这是一个 interface（接口），你不需要手写它的实现类！MyBatis-Plus 会在启动时用动态代理自动生成实现对象。
 * 2. extends BaseMapper<CheerTask>：
 *    只要继承了 BaseMapper 并传入实体类 CheerTask，立刻免费获得一套完整的单表 CRUD 方法：
 *    - insert(entity)           -> 插入一条数据
 *    - deleteById(id)           -> 按主键删除
 *    - deleteByIds(idList)      -> 按主键集合批量删除
 *    - updateById(entity)       -> 按主键更新非空字段
 *    - selectById(id)           -> 按主键查询单条
 *    - selectCount(wrapper)     -> 按条件统计数量
 *    - selectPage(page,wrapper) -> 按条件物理分页查询
 */
public interface CheerTaskMapper extends BaseMapper<CheerTask> {

    /**
     * 自定义 SQL 查询示例：统计今天（DATE(completed_at) = CURDATE()）完成打卡（status = 2）的任务数
     * 当内置的单表方法不够用（比如需要用 MySQL 日期函数或 GROUP BY / 多表 JOIN）时，
     * 直接在方法上贴 @Select("原生 SQL 语句") 即可执行并自动把结果转为返回值 Long！
     */
    @Select("SELECT COUNT(*) FROM cheer_task WHERE status = 2 AND DATE(completed_at) = CURDATE() AND user_id = #{userId}")
    Long countTodayCompleted(@Param("userId") Long userId);

    List<CategoryStatVO> countByCategory(@Param("userId") Long userId, @Param("status") Integer status);

}
