package com.cheer.demo.dto;

// 引入 Jakarta Validation 标准校验注解（对应前端 Element Plus 的 FormRules 或 Zod 校验）
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 新增 / 编辑任务时的入参数据传输对象（DTO = Data Transfer Object）
 * （对应前端 types/task.ts 中的 TaskSavePayload）
 *
 * 思考：为什么不直接用 CheerTask 实体类接收前端表单，而要单独建这个 TaskSaveDTO？
 * 1. 安全性：CheerTask 里有 id、createdAt、updatedAt，如果直接用 Entity 接参，前端恶意传个 id
 * 或时间就能篡改数据；
 * 2. 职责清晰：把 @NotBlank、@Size 等表单校验规则写在 DTO 上，保持数据库 Entity 干净纯粹。
 */
public class TaskSaveDTO {

    /**
     * 任务标题
     * - @NotBlank：专门校验字符串，要求不能为 null，且去掉首尾空格后长度必须大于 0
     * - @Size(max = 100)：限制字符串最大长度为 100，防止超过数据库 VARCHAR(100) 报错
     */
    @NotBlank(message = "任务标题不能为空")
    @Size(max = 100, message = "标题最多 100 个字符")
    private String title;

    /** 任务描述（选填，不加 @NotBlank，但限制最大长度 500） */
    @Size(max = 500, message = "任务描述最多 500 个字符")
    private String description;

    /** 任务分类（必填，如 STUDY / WORK / LIFE / FITNESS） */
    @NotBlank(message = "请选择任务分类")
    private String category;

    // 优先级（1-普通，2-重要，3-紧急），前端未传时默认赋值为 2；Min/Max 用于限制整数上下界
    @Min(value = 1, message = "优先级范围 1-3")
    @Max(value = 3, message = "优先级范围 1-3")
    private Integer priority = 2;

    /** 任务状态（0-待开始，1-进行中，2-已打卡），前端未传时默认赋值为 0 */
    @Min(value = 0, message = "状态范围 0-2")
    @Max(value = 2, message = "状态范围 0-2")
    private Integer status = 0;

    /** 打卡感悟（选填，最多 200 字符） */
    @Size(max = 200, message = "打卡感悟最多 200 个字符")
    private String cheerNote;

    // ==================== 标准 Getter / Setter 方法 ====================
    // 显式写出每个私有属性的 getXxx / setXxx 方法，不仅直观易懂，也能确保在任何未挂载 Lombok Agent 的编辑器中 100% 零报错

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCheerNote() {
        return cheerNote;
    }

    public void setCheerNote(String cheerNote) {
        this.cheerNote = cheerNote;
    }
}
