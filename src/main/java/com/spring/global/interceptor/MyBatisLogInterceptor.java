package com.spring.global.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spring.global.helper.LoggingBuilder;
import com.spring.global.helper.LoggingBuilder.LogColors;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * MyBatis의 SQL 실행 로깅을 담당하는 인터셉터로, SQL 문과 파라미터를 로그로 기록하고 바인딩 값을 표시
 *
 * @author C23024
 * @Modification <pre>
 *
 * since        author        description
 * ============ ============= ===========================
 * 2024. 5. 10. C23024        최초 생성
 *               </pre>
 * @see
 * @since 2024. 5. 10.
 */
@Component
@Slf4j
@Intercepts(value = {
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class})})
public class MyBatisLogInterceptor implements Interceptor {

    @Autowired
    private LoggingBuilder loggingBuilder;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object paramObj = invocation.getArgs()[1];
        MappedStatement statement = (MappedStatement) invocation.getArgs()[0];

        try {
            BoundSql boundSql = statement.getBoundSql(paramObj);
            String paramSql = getParamBindSQL(boundSql);
            String extractedStatementId = statement.getId()
                    .substring(statement.getId().lastIndexOf('.', statement.getId().lastIndexOf('.') - 1) + 1);
            loggingBuilder.appendTitle(LogColors.GREEN, "-- SQL START: {} ", extractedStatementId);
            loggingBuilder.append(LogColors.GREEN, "# XML File Location: {} ", statement.getResource());
            loggingBuilder.append(LogColors.GREEN, "# Statement ID     : {} ", statement.getId());
            loggingBuilder.append(LogColors.GREEN, paramSql);

            // Query 실행 및 결과 처리
            Object result = invocation.proceed();

            // 결과가 List 형태인 경우 조회된 건수를 로깅
            String statementId = statement.getId().substring(statement.getId().lastIndexOf('.') + 1);
            if (statementId.startsWith("update")) {
                // Update 메서드인 경우
                if (result instanceof Integer) {
                    loggingBuilder.append(LogColors.GREEN, "# Updated: {} row(s)", result);
                } else {
                    loggingBuilder.append(LogColors.GREEN, "# Update executed successfully");
                }
            } else if (statementId.startsWith("delete")) {
                // Delete 메서드인 경우
                if (result instanceof Integer) {
                    loggingBuilder.append(LogColors.GREEN, "# Deleted: {} row(s)", result);
                } else {
                    loggingBuilder.append(LogColors.GREEN, "# Delete executed successfully");
                }
            } else if (statementId.startsWith("select")) {
                // Select 메서드인 경우
                loggingBuilder.append(LogColors.GREEN, "# Selected: {} row(s)", ((List<?>) result).size());
            } else {
                // 기타 SQL인 경우
                loggingBuilder.append(LogColors.GREEN, "# SQL executed successfully");
            }

            loggingBuilder.appendTitle(LogColors.GREEN, "-- SQL END: {} ", extractedStatementId);

            return result;

        } catch (NoSuchFieldException nsf) {
            return invocation.proceed();
        }
    }

    // 파라미터 sql 바인딩 처리
    @SuppressWarnings("null")
    private String getParamBindSQL(BoundSql boundSql) throws NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException, JsonProcessingException {

        Object parameterObject = boundSql.getParameterObject();
        StringBuilder sqlStringBuilder = new StringBuilder(boundSql.getSql());

        // stringBuilder 파라미터 replace 처리
        BiConsumer<StringBuilder, Object> sqlObjectReplace = (sqlSb, value) -> {

            int questionIdx = sqlSb.indexOf("?");

            if (value == null) {
                sqlSb.replace(questionIdx, questionIdx + 1, "null/*<--[Param]*/");
            } else if (value instanceof String || value instanceof LocalDate || value instanceof LocalDateTime
                    || value instanceof Enum<?>) {
                sqlSb.replace(questionIdx, questionIdx + 1,
                        "'" + (value != null ? value.toString() : "") + "'/*<--[Param]*/");
            } else {
                sqlSb.replace(questionIdx, questionIdx + 1, value.toString() + "/*<--[Param]*/");
            }
            questionIdx = sqlSb.indexOf("?", questionIdx + 1);

        };

        if (parameterObject == null) {
            // Null 인 경우
            int questionIdx = 0;
            while ((questionIdx = sqlStringBuilder.indexOf("?", questionIdx)) != -1) { // BoundSql에 ?가 없을 때까지 반복
                sqlStringBuilder.replace(questionIdx, questionIdx + 1, "null/*<--[Param]*/");
                questionIdx = sqlStringBuilder.indexOf("?", questionIdx + 1);
            }
        } else {
            if (parameterObject instanceof Integer
                    || parameterObject instanceof Long
                    || parameterObject instanceof Float
                    || parameterObject instanceof Double
                    || parameterObject instanceof String) {
                // 기본 타입인 경우
                Object value = parameterObject;
                int questionIdx = 0;
                while ((questionIdx = sqlStringBuilder.indexOf("?", questionIdx)) != -1) { // BoundSql에 ?가 없을 때까지 반복
                    if (value instanceof String || value instanceof LocalDate || value instanceof LocalDateTime
                            || value instanceof Enum<?>) {
                        sqlStringBuilder.replace(questionIdx, questionIdx + 1,
                                "'" + (value != null ? value.toString() : "") + "'/*<--[Param]*/");
                    } else {
                        sqlStringBuilder.replace(questionIdx, questionIdx + 1, value.toString() + "/*<--[Param]*/");
                    }
                    questionIdx = sqlStringBuilder.indexOf("?", questionIdx + 1);
                }
            } else if (parameterObject instanceof Map) {
                // Map 타입인 경우
                @SuppressWarnings("rawtypes")
                Map paramterObjectMap = (Map) parameterObject;
                List<ParameterMapping> paramMappings = boundSql.getParameterMappings();

                for (ParameterMapping parameterMapping : paramMappings) {
                    String propertyKey = parameterMapping.getProperty();
                    try {
                        Object paramValue = null;
                        if (boundSql.hasAdditionalParameter(propertyKey)) {
                            // 동적 SQL로 인해 __frch_item_0 같은 파라미터가 생성되어 적재됨, additionalParameter로 획득
                            paramValue = boundSql.getAdditionalParameter(propertyKey);
                        } else {
                            paramValue = paramterObjectMap.get(propertyKey);
                        }

                        sqlObjectReplace.accept(sqlStringBuilder, paramValue);
                    } catch (Exception e) {
                        sqlObjectReplace.accept(sqlStringBuilder, "[cannot binding : " + propertyKey + "]");
                    }

                }
            } else {
                List<ParameterMapping> paramMappings = boundSql.getParameterMappings();
                Class<? extends Object> paramClass = parameterObject.getClass();

                for (ParameterMapping parameterMapping : paramMappings) {
                    String propertyKey = parameterMapping.getProperty();

                    try {
                        Object paramValue = null;
                        if (boundSql.hasAdditionalParameter(propertyKey)) {
                            // 동적 SQL로 인해 __frch_item_0 같은 파라미터가 생성되어 적재됨, additionalParameter로 획득
                            paramValue = boundSql.getAdditionalParameter(propertyKey);
                        } else {
                            Field field = ReflectionUtils.findField(paramClass, propertyKey);
                            field.setAccessible(true);
                            paramValue = field.get(parameterObject);
                        }

                        sqlObjectReplace.accept(sqlStringBuilder, paramValue);
                    } catch (Exception e) {
                        sqlObjectReplace.accept(sqlStringBuilder, "[cannot binding : " + propertyKey + "]");
                    }

                }
            }
        }
        return sqlStringBuilder.toString().replaceAll("([\\r\\n\\s]){2,}([\\r\\n])+", "\n");
    }

}