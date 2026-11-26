package com.cheer.demo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 快捷切换状态 / 一键打卡专用入参 DTO
 * （对应 PATCH /api/tasks/{id}/status 接口的请求体 JSON）
 *
 * 知识点对比：@NotNull vs @NotBlank
 * - 数字类型（Integer / Long）校验必填必须用 @NotNull！
 * - 字符串类型（String）校验必填才用 @NotBlank！
 */
public class TaskStatusDTO {

    /** 目标状态：必填，取值只能是 0、1、2 */
    @NotNull(message = "目标状态不能为空")
    @Min(value = 0, message = "状态取值不合法")
    @Max(value = 2, message = "状态取值不合法")
    private Integer status;

    /** 打卡时填写的自我鼓励语（选填，最多 200 个字符） */
    @Size(max = 200, message = "打卡鼓励语最多 200 个字符")
    private String cheerNote;

    // ==================== 标准 Getter / Setter 方法 ====================

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
