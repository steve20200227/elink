package cn.snowsoft.iot.module.job.framework.security.config;

import cn.snowsoft.iot.framework.security.config.AuthorizeRequestsCustomizer;
import cn.snowsoft.iot.module.job.enums.ApiConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * System 模块的 Security 配置
 */
@Configuration(proxyBeanMethods = false, value = "xxljobSecurityConfiguration")
public class SecurityConfiguration {

    @Value("${spring.boot.admin.context-path:''}")
    private String adminSeverContextPath;
    @Bean("xxljobAuthorizeRequestsCustomizer")
    public AuthorizeRequestsCustomizer authorizeRequestsCustomizer() {
        return new AuthorizeRequestsCustomizer() {

            @Override
            public void customize(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry) {
                // TODO 芋艿：这个每个项目都需要重复配置，得捉摸有没通用的方案
                // Swagger 接口文档
                registry.antMatchers("/jobinfo/**").permitAll() // Swagger UI
                        .antMatchers("**/"+adminSeverContextPath + "/**").permitAll()
                        .antMatchers(ApiConstants.PREFIX + "/**").permitAll();
            }

        };
    }

}
