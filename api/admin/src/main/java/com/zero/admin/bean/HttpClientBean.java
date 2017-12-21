package com.zero.admin.bean;

import com.zero.admin.util.HttpClient;
import com.zero.admin.vo.http.properties.HttpFeiGeProperties;
import com.zero.admin.vo.http.properties.HttpJuHeProperties;
import com.zero.admin.vo.http.properties.HttpWeChatProperties;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author yezhaoxing
 * @date 2017/11/02
 */
@Configuration
@Data
public class HttpClientBean {

    @Resource
    private HttpFeiGeProperties httpFeiGeProperties;
    @Resource
    private HttpWeChatProperties httpWeChatProperties;
    @Resource
    private HttpJuHeProperties httpJuHeProperties;

    @Bean("feiGeHttpClient")
    public HttpClient feiGeHttpClient() {
        return new HttpClient(httpFeiGeProperties.getScheme(), httpFeiGeProperties.getHostname(),
                Integer.valueOf(httpFeiGeProperties.getPort()));
    }

    @Bean("weChatHttpClient")
    public HttpClient weChatHttpClient() {
        return new HttpClient(httpWeChatProperties.getScheme(), httpWeChatProperties.getHostname(),
                Integer.valueOf(httpWeChatProperties.getPort()));
    }

    @Bean("juHeHttpClient")
    public HttpClient juHeHttpClient() {
        return new HttpClient(httpJuHeProperties.getScheme(), httpJuHeProperties.getHostname(),
                Integer.valueOf(httpJuHeProperties.getPort()));
    }
}
