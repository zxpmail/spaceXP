package cn.piesat.framework.common.pipeline;

import cn.piesat.framework.common.pipeline.context.ChannelHandlerContext;
import cn.piesat.framework.common.pipeline.handler.ChannelBoundHandler;
import cn.piesat.framework.common.pipeline.model.ChannelHandlerRequest;
import cn.piesat.framework.common.pipeline.utils.ChannelContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * <p/>
 * {@code @description}: 链路管道
 * <p/>
 * {@code @create}: 2024-11-14 11:07
 * {@code @author}: zhouxp
 */
@Slf4j
public class ChannelPipeline {
    private final LinkedBlockingDeque<ChannelBoundHandler> channelHandlers = new LinkedBlockingDeque<>();

    public ChannelPipeline addFirst(ChannelBoundHandler channelHandler) {
        channelHandlers.addFirst(channelHandler);
        return this;
    }

    public ChannelPipeline addLast(ChannelBoundHandler channelHandler) {
        channelHandlers.addLast(channelHandler);
        return this;
    }

    public boolean start(ChannelHandlerRequest<?> channelHandlerRequest) {
        if (ObjectUtils.isEmpty(channelHandlerRequest)) {
            log.warn("channelHandlerRequest is empty");
            return false;
        }
        if (channelHandlers.isEmpty()) {
            log.warn("channelHandlers is empty");
            return false;
        }

        return handler(channelHandlerRequest);
    }

    private boolean handler(ChannelHandlerRequest<?> channelHandlerRequest) {
        if (!StringUtils.hasText((channelHandlerRequest.getId()))) {
            channelHandlerRequest.setId(UUID.randomUUID().toString());
        }
        ChannelHandlerContext channelHandlerContext = ChannelContextUtil.get();
        channelHandlerContext.setChannelHandlerRequest(channelHandlerRequest);
        boolean isSuccess = true;
        try {
            for (ChannelBoundHandler channelHandler : channelHandlers) {
                log.info("start execute class {} ..........",channelHandler.getClass().getSimpleName());
                isSuccess = channelHandler.handler(channelHandlerContext);
                if (!isSuccess) {
                    log.warn("execute class {} is error!",channelHandler.getClass().getSimpleName());
                    break;
                }
            }
        } catch (Exception e) {
            log.error("{}", e.getMessage());
            isSuccess = false;
        } finally {
            ChannelContextUtil.clear();
            channelHandlers.clear();
        }
        return isSuccess;
    }
}
