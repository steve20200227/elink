///*
// * Licensed to the Apache Software Foundation (ASF) under one or more
// * contributor license agreements.  See the NOTICE file distributed with
// * this work for additional information regarding copyright ownership.
// * The ASF licenses this file to You under the Apache License, Version 2.0
// * (the "License"); you may not use this file except in compliance with
// * the License.  You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package cn.snowsoft.iot.module.message.config;
//
//import cn.snowsoft.iot.module.message.model.entity.UserInfo;
//import cn.snowsoft.iot.module.message.util.ThreadLocalUtils;
//import com.baomidou.mybatisplus.annotation.DbType;
//import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
//import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
//import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ibatis.reflection.MetaObject;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.stereotype.Component;
//
///**
// * Mybatis-Plus 配置类
// *
// * @author oszero
// * @version 1.0.0
// */
//@Configuration
//@MapperScan("cn.snowsoft.iot.module.message.mapper")
//@Primary
//public class AdminMyBatisPlusConfig {
//    /**
//     * 添加分页插件
//     *
//     * @return 拦截器
//     */
//    @Bean
//    public MybatisPlusInterceptor mybatisPlusInterceptor() {
//        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));//如果配置多个插件,切记分页最后添加
//        //interceptor.addInnerInterceptor(new PaginationInnerInterceptor()); 如果有多数据源可以不配具体类型 否则都建议配上具体的DbType
//        return interceptor;
//    }
//
//    /**
//     * MyBatisPlus 字段自动注入配置
//     */
//    @Slf4j
//    @Component
//    public static class MyMetaObjectHandler implements MetaObjectHandler {
//
//        @Override
//        public void insertFill(MetaObject metaObject) {
//            log.info("start insert fill ....");
//            UserInfo userInfo = ThreadLocalUtils.getUserInfo();
//            this.strictInsertFill(metaObject, "createUser", userInfo::getUsername, String.class); // 起始版本 3.3.3(推荐)
//            this.strictInsertFill(metaObject, "updateUser", userInfo::getUsername, String.class); // 起始版本 3.3.3(推荐)
//        }
//
//        @Override
//        public void updateFill(MetaObject metaObject) {
//            log.info("start update fill ....");
//            UserInfo userInfo = ThreadLocalUtils.getUserInfo();
//            this.strictUpdateFill(metaObject, "updateUser", userInfo::getUsername, String.class); // 起始版本 3.3.3(推荐)
//        }
//    }
//}
