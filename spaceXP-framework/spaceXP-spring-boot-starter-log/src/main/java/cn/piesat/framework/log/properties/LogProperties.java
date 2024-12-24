package cn.piesat.framework.log.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * <p/>
 * {@code @description}  :日志配置类
 * <p/>
 * <b>@create:</b> 2023/10/8 13:34.
 *
 * @author zhouxp
 */
@Data
@ConfigurationProperties("space.log")
public class LogProperties {
    /**
     * 使用日志标志类 1使用OpLog注解 2使用swagger注解
     */
    private Integer logFlag = 1;

    /**
     * 从header中获取值写入日志
     */
    private List<String> headers;


    /**
     * mdc信息
     */
    private Mdc mdc;

    @Data
    public static class Mdc {
        /**
         * 贴点表达式
         */
        private String pointcutExpression ="@annotation(cn.piesat.framework.log.annotation.MdcLog)";

        /**
         * APP应用信息
         */
        private String appInfo ="info";
        /**
         * App错误信息
         */
        private String errorInfo ="error";
        /**
         * 日志类型
         */
        private String logType ="BUSINESS";

        /**
         * 日志类型代码
         */
        private String logTypeCode ="2";

        /**
         * mdc线程池队列数
         */
        private Integer threadPoolQueueCapacity =500;

        /**
         * mdc线程存活
         */
        private Integer threadAliveSeconds =60;
    }

}
