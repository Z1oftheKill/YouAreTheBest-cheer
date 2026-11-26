package com.cheer.demo.controller;

import com.cheer.demo.common.PageResult;
import com.cheer.demo.common.Result;
import com.cheer.demo.dto.DashboardStatsVO;
import com.cheer.demo.dto.TaskSaveDTO;
import com.cheer.demo.dto.TaskStatusDTO;
import com.cheer.demo.entity.CheerTask;
import com.cheer.demo.service.CheerTaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 待办与成长打卡 RESTful 接口控制器（Controller 层 —— 餐厅前台服务员）
 * （对应前端 src/api/task.ts 中发起的所有请求）
 *
 * 类头上核心注解的作用：
 * 1. @RestController：声明这是 Web 控制器，且所有方法返回的 Java 对象都会自动用 Jackson 转成 JSON 响应给前端
 * 2. @RequestMapping("/api/tasks")：为当前类下所有接口统一加上基础路由前缀 "/api/tasks"
 */
@RestController
@RequestMapping("/api/tasks")
public class CheerTaskController {

    /** 注入业务逻辑层接口（Spring 会自动把 CheerTaskServiceImpl 实例注入进来） */
    private final CheerTaskService taskService;

    /**
     * 构造器依赖注入（DI）：Spring 启动时自动将 CheerTaskService 实现类对象传入
     */
    public CheerTaskController(CheerTaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * 接口 1：获取顶部打卡统计看板数据
     * 请求方式与路径：GET /api/tasks/stats
     */
    @GetMapping("/stats")
    public Result<DashboardStatsVO> stats() {
        // 调用 Service 层计算统计数据，并包装在统一的 Result.ok(...) 中返回
        return Result.ok(taskService.getStats());
    }

    /**
     * 接口 2：分页 + 多条件组合查询任务列表
     * 请求方式与路径：GET /api/tasks?keyword=xxx&category=STUDY&status=1&priority=3&pageNum=1&pageSize=10
     *
     * @RequestParam 注解专门用来接收 URL 问号（?）后面的 Query 查询参数：
     * - required = false：表示该参数可选，前端不传时值为 null，不会报错
     * - defaultValue = "1"：表示前端不传时默认赋值为 1
     */
    @GetMapping
    public Result<PageResult<CheerTask>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false) Integer priority) {
        return Result.ok(taskService.pageTasks(keyword, category, status, pageNum, pageSize, priority));
    }

    /**
     * 接口 3：根据 ID 查询单条任务详情
     * 请求方式与路径：GET /api/tasks/{id} （例如 GET /api/tasks/5）
     *
     * @PathVariable 注解专门用来提取 URL 路径大括号 {id} 里的动态数字，并自动转成 Long 类型
     */
    @GetMapping("/{id}")
    public Result<CheerTask> detail(@PathVariable Long id) {
        return Result.ok(taskService.getDetail(id));
    }

    /**
     * 接口 4：新建待办任务
     * 请求方式与路径：POST /api/tasks
     *
     * 参数上的两个关键注解：
     * - @RequestBody：将前端 POST 请求体中的 JSON 字符串自动反序列化为 Java 的 TaskSaveDTO 对象
     * - @Valid：在进入方法体之前，先触发 TaskSaveDTO 里的 @NotBlank/@Min/@Max 校验；校验不过直接由 GlobalExceptionHandler 拦截返回 400
     */
    @PostMapping
    public Result<CheerTask> create(@Valid @RequestBody TaskSaveDTO dto) {
        return Result.ok(taskService.createTask(dto));
    }

    /**
     * 接口 5：编辑更新指定 ID 的任务
     * 请求方式与路径：PUT /api/tasks/{id}
     * 同时结合了 @PathVariable（从 URL 拿要修改的任务 ID）与 @Valid @RequestBody（从请求体拿修改后的表单 JSON）
     */
    @PutMapping("/{id}")
    public Result<CheerTask> update(@PathVariable Long id, @Valid @RequestBody TaskSaveDTO dto) {
        return Result.ok(taskService.updateTask(id, dto));
    }

    /**
     * 接口 6：快捷切换状态 / 一键打卡
     * 请求方式与路径：PATCH /api/tasks/{id}/status
     * RESTful 规范中，PUT 通常用于整条记录更新，PATCH 用于局部字段（如仅状态与打卡鼓励语）更新
     */
    @PatchMapping("/{id}/status")
    public Result<CheerTask> patchStatus(@PathVariable Long id, @Valid @RequestBody TaskStatusDTO dto) {
        return Result.ok(taskService.changeStatus(id, dto));
    }

    /**
     * 接口 7：根据 ID 删除任务
     * 请求方式与路径：DELETE /api/tasks/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        taskService.removeTask(id);
        // 删除无需返回业务数据，调用无参 Result.ok() 返回 { code: 200, message: "success", data: null }
        return Result.ok();
    }
    
    @PostMapping("/batch-delete")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        taskService.batchRemoveTasks(ids);
        return Result.ok();
    }
}
