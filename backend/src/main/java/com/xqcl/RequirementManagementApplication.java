package com.xqcl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 需求管理系统启动类
 *
 * @author xqcl
 * @since 2024-01-15
 */
@SpringBootApplication
public class RequirementManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(RequirementManagementApplication.class, args);
        System.out.println("========================================");
        System.out.println("需求管理系统启动成功！");
        System.out.println("Swagger 文档地址: http://localhost:8080/api/swagger-ui.html");
        System.out.println("========================================");
    }
}
