/*
 * Copyright © 2018 organization baomidou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.piesat.framework.dynamic.datasource.interceptor;


import cn.piesat.framework.dynamic.datasource.annotation.DSTransactional;
import cn.piesat.framework.dynamic.datasource.tx.DataSourceClassResolver;
import cn.piesat.framework.dynamic.datasource.tx.TransactionalExecutor;
import cn.piesat.framework.dynamic.datasource.tx.TransactionalInfo;
import cn.piesat.framework.dynamic.datasource.tx.TransactionalTemplate;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Dynamic DataSource Transaction Interceptor
 *
 * @author funkye
 */
@Slf4j
public class DynamicLocalTransactionInterceptor implements MethodInterceptor {
    private final TransactionalTemplate transactionalTemplate;
    private final DataSourceClassResolver dataSourceClassResolver;

    public DynamicLocalTransactionInterceptor(Boolean allowedPublicOnly) {
        transactionalTemplate = new TransactionalTemplate();
        dataSourceClassResolver = new DataSourceClassResolver(allowedPublicOnly);
    }

    @Override
    public Object invoke(final MethodInvocation methodInvocation) throws Throwable {
        final Method method = methodInvocation.getMethod();

        TransactionalExecutor transactionalExecutor = new TransactionalExecutor() {
            @Override
            public Object execute() throws Throwable {
                return Objects.requireNonNull(methodInvocation.proceed());
            }

            @Override
            public TransactionalInfo getTransactionInfo() {
                return dataSourceClassResolver.findTransactionalInfo(method, methodInvocation.getThis(), DSTransactional.class);
            }
        };
        return transactionalTemplate.execute(transactionalExecutor);
    }

}