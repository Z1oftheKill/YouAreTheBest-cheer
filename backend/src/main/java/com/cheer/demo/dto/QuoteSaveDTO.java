package com.cheer.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class QuoteSaveDTO {
    @NotBlank(message = "内容不能为空")
    @Size(max = 200, message = "不能超过200字!")
    private String content;

    @Size(max = 50, message = "不能超过50字!")
    private String source;

    

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
