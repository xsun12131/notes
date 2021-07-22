package com.fatpanda.notes.pojo.entity;

import com.fatpanda.notes.common.model.entity.BaseEntity;
import com.fatpanda.notes.common.utils.LocalTimeUtil;
import com.fatpanda.notes.common.utils.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author xyy
 * @date 2021/7/20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Log implements BaseEntity {

    @Id
    private String id;

    @ApiModelProperty("日志类型")
    private String type;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("请求地址")
    private String remoteAddr;

    @ApiModelProperty("请求链接")
    private String requestUri;

    @ApiModelProperty("请求方式")
    private String method;

    @ApiModelProperty("参数")
    private String params;

    @ApiModelProperty("异常")
    private String exception;

    @ApiModelProperty("操作时间")
    private LocalDateTime operateDate;

    @ApiModelProperty("消耗用时")
    private Long consumeTime;

    @ApiModelProperty("用户id")
    private String userId;

    /**
     * 设置请求参数
     *
     * @param paramMap
     */
    public void setMapToParams(Map<String, String[]> paramMap) {
        if (paramMap == null) {
            return;
        }
        StringBuilder params = new StringBuilder();
        for (Map.Entry<String, String[]> param : ((Map<String, String[]>) paramMap).entrySet()) {
            params.append(("".equals(params.toString()) ? "" : "&") + param.getKey() + "=");
            String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
            params.append(StringUtil.abbr(StringUtil.endsWithIgnoreCase(param.getKey(), "password") ? "" : paramValue, 100));
        }
        this.params = params.toString();
    }

    @Override
    public String toString() {
        return "Log{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", remoteAddr='" + remoteAddr + '\'' +
                ", requestUri='" + requestUri + '\'' +
                ", method='" + method + '\'' +
                ", params='" + params + '\'' +
                ", exception='" + exception + '\'' +
                ", operateDate=" + LocalTimeUtil.format(operateDate, "yyyy-MM-dd HH:mm:ss.SSS") +
                ", consumeTime=" + consumeTime +
                ", userId='" + userId + '\'' +
                '}';
    }
}
