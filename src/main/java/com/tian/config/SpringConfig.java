package com.tian.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.tian")
@PropertySource("classpath:jdbc.properties") // 导入配置文件。
@Import({JdbcConfig.class, MybatisConfig.class})
public class SpringConfig {
    // Spring配置类。
}
