package cn.piesat.framework.feign.config;


import cn.piesat.framework.common.properties.CommonProperties;
import cn.piesat.framework.feign.core.FeignExceptionHandler;
import cn.piesat.framework.feign.core.FeignRequestInterceptor;
import cn.piesat.framework.feign.core.ResultDecoder;


import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * <p/>
 *
 * @author zhouxp
 * @description :Feign配置类
 * <p/>
 * <b>@create:</b> 2022/10/10 14:06.
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class FeignAutoConfiguration{
    private final ObjectFactory<HttpMessageConverters> messageConverters;
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = {"cn.piesat.framework.common.model.vo.ApiMapResult"})
    public Decoder feignDecoder(ObjectProvider<HttpMessageConverterCustomizer> customizers, CommonProperties commonProperties) {
        return new OptionalDecoder(new ResponseEntityDecoder(new ResultDecoder(new SpringDecoder(this.messageConverters, customizers),commonProperties.getApiMapResultEnable())));
    }

    @Bean
    public FeignExceptionHandler feignExceptionHandler(){
        return  new FeignExceptionHandler();
    }

    @Bean
    public FeignRequestInterceptor feignRequestInterceptor(){
        return  new FeignRequestInterceptor();
    }


}
