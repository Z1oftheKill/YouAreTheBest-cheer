package com.cheer.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cheer.demo.common.BusinessException;
import com.cheer.demo.common.PageResult;
import com.cheer.demo.common.UserContext;
import com.cheer.demo.dto.CategoryStatVO;
import com.cheer.demo.dto.DashboardStatsVO;
import com.cheer.demo.dto.TaskSaveDTO;
import com.cheer.demo.dto.TaskStatusDTO;
import com.cheer.demo.entity.CheerTask;
import com.cheer.demo.mapper.CheerTaskMapper;
import com.cheer.demo.service.CheerTaskService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务业务逻辑实现类（真正执行所有业务计算与判断的“后厨主厨”）
 *
 * 注解说明：
 * 1. @Service：告诉 Spring 容器启动时自动创建该类的单例对象（Bean），供 Controller 注入使用
 * 2. @SuppressWarnings("null")：压制编辑器对第三方库泛型方法引用的空安全黄线警告
 */
@Service
@SuppressWarnings("null")
public class CheerTaskServiceImpl implements CheerTaskService {

    /** 持久层数据库操作对象（由 Spring 容器通过下面的构造方法自动注入） */
    private final CheerTaskMapper taskMapper;

    /**
     * 构造器依赖注入（DI = Dependency Injection）：
     * Spring 启动创建 CheerTaskServiceImpl 时，会自动从容器中找到 CheerTaskMapper 实例传进来赋值给
     * this.taskMapper
     */
    public CheerTaskServiceImpl(CheerTaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    /**
     * 统计看板数据组装
     */
    @Override
    public DashboardStatsVO getStats() {

        long userId = UserContext.getUserId();

        // 1. selectCount(null) 传 null 表示没有任何 WHERE 条件，直接查询全表总条数：SELECT COUNT(*) FROM
        // cheer_task
        long total = taskMapper.selectCount(new 
            LambdaQueryWrapper<CheerTask>().eq(CheerTask::getUserId, userId)
        );

        // 2. 使用 LambdaQueryWrapper 构造 WHERE status = 2 条件，统计累计已打卡数量
        long completed = taskMapper.selectCount(
                new LambdaQueryWrapper<CheerTask>().eq(CheerTask::getStatus, 2)
                .eq(CheerTask::getUserId ,userId));

        // 3. 构造 WHERE status = 1 条件，统计进行中数量
        long inProgress = taskMapper.selectCount(
                new LambdaQueryWrapper<CheerTask>().eq(CheerTask::getStatus, 1).eq(CheerTask::getUserId, userId));

        // 4. 调用我们在 CheerTaskMapper 中手写 @Select SQL 的自定义方法，统计今日完成数量
        Long todayDone = taskMapper.countTodayCompleted(userId);

        // 5. 创建 VO 对象，将计算好的各项指标塞进去
        DashboardStatsVO vo = new DashboardStatsVO();
        vo.setTotalCount(total);
        vo.setCompletedCount(completed);
        vo.setInProgressCount(inProgress);
        // 防空兜底：如果返回 null 则设为 0L
        vo.setTodayCompletedCount(todayDone == null ? 0L : todayDone);

        // 6. 计算完成率百分比（注意用 100.0 浮点数避免整数相除直接丢掉小数部分，再四舍五入转 int）
        int rate = total == 0 ? 0 : (int) Math.round(completed * 100.0 / total);
        vo.setCompletionRate(rate);

        // 7. 根据完成进度生成激励文案
        vo.setCheerQuote(buildProgressTip(completed, rate));
        return vo;
    }

    /**
     * 内部私有辅助方法（private 表示仅供当前类内部调用）：根据进度生成看板提示语
     */
    private String buildProgressTip(long completed, int rate) {
        if (completed == 0) {
            return "先从第一条小目标开始打卡，跑通请求就是胜利！";
        }
        if (rate >= 80) {
            return "当前完成率已达 " + rate + "%，前后端全链路节奏非常稳！";
        }
        return "已累计完成 " + completed + " 项成长打卡，继续保持节奏！";
    }

    /**
     * 多条件动态筛选 + 物理分页查询
     */
    @Override
    public PageResult<CheerTask> pageTasks(String keyword, String category, Integer status, long pageNum, long pageSize,
            Integer priority) {
        // 创建 MyBatis-Plus 的 Lambda 条件构造器（用来动态拼装 SQL 的 WHERE 和 ORDER BY 子句）
        LambdaQueryWrapper<CheerTask> wrapper = new LambdaQueryWrapper<>();

        // 链式动态拼接条件（第一个布尔参数为 true 时才会把该条件拼进最终 SQL，极其方便！）：
        wrapper
                // 1. 当 keyword 非空时，拼接 AND (title LIKE '%keyword%' OR description LIKE
                // '%keyword%')
                .and(StringUtils.hasText(keyword), w -> w
                        .like(CheerTask::getTitle, keyword)
                        .or()
                        .like(CheerTask::getDescription, keyword))
                // 2. 当 category 非空时，拼接 AND category = ?
                .eq(StringUtils.hasText(category), CheerTask::getCategory, category)
                // 3. 当 status 不为 null 时，拼接 AND status = ?
                .eq(status != null, CheerTask::getStatus, status)
                // 4. 【你亲手实现的功能】当 priority 不为 null 时，拼接 AND priority = ?
                .eq(priority != null, CheerTask::getPriority, priority)
                .eq(CheerTask::getUserId, UserContext.getUserId())
                // 5. 排序规则：未完成的排前面（status 升序） -> 紧急的排前面（priority 降序） -> 最新创建的排前面（id 降序）
                .orderByAsc(CheerTask::getStatus)
                .orderByDesc(CheerTask::getPriority)
                .orderByDesc(CheerTask::getId);

        // 执行分页查询：传入 Page(当前页码, 每页条数) 和条件 wrapper
        Page<CheerTask> page = taskMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);

        // 将 MyBatis-Plus 的 Page 对象转换为我们统一的 PageResult<CheerTask> 返回
        return new PageResult<>(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    /**
     * 按主键 ID 查询单条详情（同时作为更新、删除前的“存在性检查”公共方法）
     */
    @Override
    public CheerTask getDetail(Long id) {
        // 执行 SELECT * FROM cheer_task WHERE id = ?
        CheerTask task = taskMapper.selectById(id);
        // 如果查不到，主动抛出 404 BusinessException，会被 GlobalExceptionHandler 捕获并返回给前端
        if (task == null || !UserContext.getUserId().equals(task.getUserId())) {
            throw new BusinessException(404, "未找到 ID=" + id + " 的任务记录");
        }
        return task;
    }

    /**
     * 新增待办任务
     */
    @Override
    public CheerTask createTask(TaskSaveDTO dto) {
        // 1. 创建一个空的数据库实体对象
        CheerTask entity = new CheerTask();
        // 2. 将前端传来的 DTO 字段拷贝到实体对象中
        applyDto(entity, dto);
        // 3. 如果创建时直接选择了“已打卡(2)”状态，自动记录完成时间为当前系统时间
        if (Integer.valueOf(2).equals(entity.getStatus())) {
            entity.setCompletedAt(LocalDateTime.now());
        }
        // 4. 执行 INSERT INTO cheer_task ...（插入成功后，MySQL 生成的自增 ID 会自动回填到 entity.getId()
        // 中）
        entity.setUserId(UserContext.getUserId());
        taskMapper.insert(entity);
        // 5. 重新按主键查出包含数据库默认 createdAt / updatedAt 的完整记录返回给前端
        return taskMapper.selectById(entity.getId());
    }

    /**
     * 全量编辑更新任务
     */
    @Override
    public CheerTask updateTask(Long id, TaskSaveDTO dto) {
        // 1. 先检查该 ID 的任务是否存在（不存在会自动抛 404 异常）
        CheerTask existing = getDetail(id);
        // 2. 记下修改前的旧状态，用于对比状态是否刚刚变成“已打卡”
        Integer oldStatus = existing.getStatus();
        // 3. 将前端修改后的 DTO 字段覆盖到实体对象上
        applyDto(existing, dto);
        // 4. 根据新旧状态变化，自动维护 completedAt 完成打卡时间
        syncCompletedTime(existing, oldStatus, dto.getStatus());
        // 5. 执行 UPDATE cheer_task SET ... WHERE id = ?
        taskMapper.updateById(existing);
        // 6. 返回更新后的最新记录
        return taskMapper.selectById(id);
    }

    /**
     * 快捷切换状态 / 一键完成打卡
     */
    @Override
    public CheerTask changeStatus(Long id, TaskStatusDTO dto) {
        // 1. 查出原任务记录
        CheerTask existing = getDetail(id);
        Integer oldStatus = existing.getStatus();
        // 2. 更新为目标状态
        existing.setStatus(dto.getStatus());
        // 3. 如果前端传了新的打卡鼓励语，则一并更新
        if (dto.getCheerNote() != null) {
            existing.setCheerNote(dto.getCheerNote());
        }
        // 4. 自动维护完成打卡时间
        syncCompletedTime(existing, oldStatus, dto.getStatus());
        // 5. 落库保存并返回最新数据
        taskMapper.updateById(existing);
        return taskMapper.selectById(id);
    }

    /**
     * 删除指定 ID 的任务
     */
    @Override
    public void removeTask(Long id) {
        // 1. 先确认记录存在（若不存在则抛 404 提示）
        getDetail(id);
        // 2. 执行 DELETE FROM cheer_task WHERE id = ?
        taskMapper.deleteById(id);
    }

    /**
     * 批量删除指定 ID 的任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class) // 开启事务：批量删除中途一旦报错，自动全部回滚
    public void batchRemoveTasks(List<Long> ids) {
        // 1. 先确认记录存在（若不存在则抛 404 提示）
        if(ids == null || ids.isEmpty()) throw new BusinessException("请先勾选要删除的任务");
        taskMapper.delete(new LambdaQueryWrapper<CheerTask>()
                .in(CheerTask::getId, ids)
                .eq(CheerTask::getUserId, UserContext.getUserId()));
    }


    /**
     * 获取类目数量
     */
    @Override 
    public List<CategoryStatVO> getCategoryStats(Integer status){
        return taskMapper.countByCategory(UserContext.getUserId(), status);
    }


    /**
     * 内部辅助方法：将入参 DTO 的属性安全拷贝到数据库实体 Entity，并做去空格与默认值兜底
     */
    private void applyDto(CheerTask target, TaskSaveDTO dto) {
        target.setTitle(dto.getTitle().trim());
        target.setDescription(dto.getDescription() == null ? "" : dto.getDescription().trim());
        target.setCategory(dto.getCategory());
        target.setPriority(dto.getPriority() == null ? 2 : dto.getPriority());
        target.setStatus(dto.getStatus() == null ? 0 : dto.getStatus());
        target.setCheerNote(dto.getCheerNote() == null ? "" : dto.getCheerNote().trim());
    }

    /**
     * 内部辅助方法：根据状态流转自动维护 completedAt（完成时间）
     * - 当从“非已打卡”变为“已打卡(2)”时：自动记录当前时间 LocalDateTime.now()
     * - 当从“已打卡”改回“待开始(0)”或“进行中(1)”时：将完成时间清空为 null
     */
    private void syncCompletedTime(CheerTask task, Integer oldStatus, Integer newStatus) {
        if (Integer.valueOf(2).equals(newStatus) && !Integer.valueOf(2).equals(oldStatus)) {
            task.setCompletedAt(LocalDateTime.now());
        } else if (!Integer.valueOf(2).equals(newStatus)) {
            task.setCompletedAt(null);
        }
    }
}
