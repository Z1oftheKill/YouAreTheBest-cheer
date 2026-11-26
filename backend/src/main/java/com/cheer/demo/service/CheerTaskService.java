package com.cheer.demo.service;

import java.util.List;

import com.cheer.demo.common.PageResult;
import com.cheer.demo.dto.DashboardStatsVO;
import com.cheer.demo.dto.TaskSaveDTO;
import com.cheer.demo.dto.TaskStatusDTO;
import com.cheer.demo.entity.CheerTask;

/**
 * 任务业务逻辑层接口（Service 接口 —— 相当于餐厅后厨的“菜单目录”）
 *
 * 为什么 Java 的 Service 层通常要拆成【接口 CheerTaskService】+【实现类 CheerTaskServiceImpl】？
 * 1. 规范清晰：Controller 只需要看这个接口，就能一目了然知道业务层提供了哪 7 个功能，不用关心内部怎么算的；
 * 2. 方便扩展与代理：Spring 的事务控制（@Transactional）、AOP 切面等底层机制更青睐面向接口的设计。
 */
public interface CheerTaskService {

    /** 获取顶部看板的统计数据与激励语 */
    DashboardStatsVO getStats();

    /** 多条件动态筛选 + 分页查询任务列表（包含你亲手新增的 priority 优先级筛选参数） */
    PageResult<CheerTask> pageTasks(String keyword, String category, Integer status, long pageNum, long pageSize,
            Integer priority);

    /** 根据主键 ID 查询单条任务详情（若不存在则抛出 404 BusinessException） */
    CheerTask getDetail(Long id);

    /** 创建新任务 */
    CheerTask createTask(TaskSaveDTO dto);

    /** 根据主键 ID 全量更新任务信息 */
    CheerTask updateTask(Long id, TaskSaveDTO dto);

    /** 快捷修改任务状态与打卡感悟（一键打卡） */
    CheerTask changeStatus(Long id, TaskStatusDTO dto);

    /** 根据主键 ID 删除任务 */
    void removeTask(Long id);

    
    /** 根据主键 ID 批量删除任务 */
    void batchRemoveTasks(List<Long> ids);
}
