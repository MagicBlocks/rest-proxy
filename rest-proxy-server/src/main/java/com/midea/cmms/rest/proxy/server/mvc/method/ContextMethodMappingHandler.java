package com.midea.cmms.rest.proxy.server.mvc.method;

import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.Map;

/**
 * 通过Spring上下文获取RestProxy的方法映射配置
 */
public class ContextMethodMappingHandler extends AbstractMethodMappingHandler {
    private ApplicationContext applicationContext;

    public ContextMethodMappingHandler(ApplicationContext context) {
        applicationContext = context;
        super.init();
    }

    @Override
    protected Collection<HandleMethod> initHandleMethods() {
        Map<String, HandleMethod> beansOfType = applicationContext.getBeansOfType(HandleMethod.class);
        Collection<HandleMethod> collection = beansOfType.values();

        // setBean
        for (HandleMethod handleMethod : collection) {
            String className = handleMethod.getServiceClassName();
            try {
                Class<?> aClass = Class.forName(className);
                Object bean = applicationContext.getBean(aClass);
                handleMethod.setBean(bean);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        }

        return collection;
    }
}
