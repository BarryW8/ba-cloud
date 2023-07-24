package com.ba.field;

import com.ba.annotation.TableField;
import com.ba.base.UserContext;
import com.ba.enums.FieldFill;
import com.ba.enums.FieldType;
import com.ba.uid.impl.CachedUidGenerator;
import com.ba.util.ApplicationUtils;
import com.ba.util.BeanUtils;
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
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

        // sql类型：insert、update、select、delete
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

        // 获取参数
        Object parameter = invocation.getArgs()[1];
        if (parameter == null) {
            return invocation.proceed();
        }

        // 只判断新增和修改
        if (SqlCommandType.INSERT.equals(sqlCommandType) || SqlCommandType.UPDATE.equals(sqlCommandType)) {
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
        Field[] fields = getAllFields(parameter);
        for (Field field : fields) {
            TableField annotation = field.getAnnotation(TableField.class);
            if (annotation == null) {
                continue;
            }
            FieldType type = annotation.type();
            FieldFill fill = annotation.fill();

            // insert 语句
            if (SqlCommandType.INSERT.equals(sqlCommandType)
                && (FieldFill.INSERT.equals(fill) || FieldFill.INSERT_UPDATE.equals(fill))
            ) {
//                if (FieldType.ID.equals(type)) {
//                    field.setAccessible(true);
//                    field.set(parameter, uidGenerator.getUID());
//                }
                if (FieldType.USER.equals(type)) {
                    field.setAccessible(true);
                    field.set(parameter, UserContext.getUserId());
                }
                if (FieldType.TIME.equals(type)) {
                    field.setAccessible(true);
                    field.set(parameter, CommonUtils.getCurrentDate());
                }
            }

            // update 语句
            if (SqlCommandType.UPDATE.equals(sqlCommandType)
                && (FieldFill.UPDATE.equals(fill) || FieldFill.INSERT_UPDATE.equals(fill))
            ) {
//                if (FieldType.ID.equals(type)) {
//                    field.setAccessible(true);
//                    field.set(parameter, uidGenerator.getUID());
//                }
                if (FieldType.USER.equals(type)) {
                    field.setAccessible(true);
                    field.set(parameter, UserContext.getUserId());
                }
                if (FieldType.TIME.equals(type)) {
                    field.setAccessible(true);
                    field.set(parameter, CommonUtils.getCurrentDate());
                }
            }
        }
    }

    /**
     * 获取类的所有属性，包括父类
     *
     * @param object
     * @return
     */
    private Field[] getAllFields(Object object) {
        Class<?> clazz = object.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }
}
