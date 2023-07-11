package com.ba.field;

import com.ba.base.BaseModel;
import com.ba.base.UserContext;
import com.ba.uid.impl.CachedUidGenerator;
import com.ba.util.CommonUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 自定义 Mybatis 插件，自动设置 createTime 和 updateTime 的值。
 * 拦截 update 操作（添加和修改）
 */
@Component
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class FieldInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];

        // 获取 SQL 命令
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        // 只判断新增和修改
        if (SqlCommandType.INSERT.equals(sqlCommandType) || SqlCommandType.UPDATE.equals(sqlCommandType)) {
            // 获取参数
            Object parameter = invocation.getArgs()[1];
            //批量操作时
            if (parameter instanceof MapperMethod.ParamMap) {
                MapperMethod.ParamMap map = (MapperMethod.ParamMap) parameter;
                Object obj = map.get("list");
                List<?> list = (List<?>) obj;
                if (list != null) {
                    for (Object o : list) {
                        setParameter(o, sqlCommandType);
                    }
                }
            } else {
                setParameter(parameter, sqlCommandType);
            }
        }
        // 执行方法
        return invocation.proceed();
    }

    public void setParameter(Object parameter, SqlCommandType sqlCommandType) throws Throwable {
        Class<?> aClass = parameter.getClass();
        Field[] declaredFields;
        //如果常用字段提取了公共类 BaseModel
        //判断 BaseModel 是否是父类
        if (BaseModel.class.isAssignableFrom(aClass)) {
            // 获取父类私有成员变量
            declaredFields = aClass.getSuperclass().getDeclaredFields();
        } else {
            // 获取私有成员变量
            declaredFields = aClass.getDeclaredFields();
        }
        for (Field field : declaredFields) {
            TableField annotation = field.getAnnotation(TableField.class);
            if (annotation == null) {
                continue;
            }
            FieldFill fill = annotation.fill();
            String name = field.getName();
            if (SqlCommandType.INSERT.equals(sqlCommandType) && FieldFill.INSERT.equals(fill)) { // insert 语句
                if ("createBy".equals(name)) { // 插入 createBy
                    field.setAccessible(true);
                    field.set(parameter, UserContext.getUserId());
                }
                if ("createTime".equals(name)) { // 插入 createTime
                    field.setAccessible(true);
                    field.set(parameter, CommonUtils.getCurrentDate());
                }
            }
            if (SqlCommandType.UPDATE.equals(sqlCommandType) && FieldFill.UPDATE.equals(fill)) { // update 语句
                if ("updateBy".equals(name)) { // 插入 updateBy
                    field.setAccessible(true);
                    field.set(parameter, UserContext.getUserId());
                }
                if ("updateTime".equals(name)) { // 插入 updateTime
                    field.setAccessible(true);
                    field.set(parameter, CommonUtils.getCurrentDate());
                }
            }
        }
    }

    @Bean
    private CachedUidGenerator uidGenerator() {
        return new CachedUidGenerator();
    }

}
