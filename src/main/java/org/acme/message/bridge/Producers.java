/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.acme.message.bridge;

import com.solacesystems.jms.SolConnectionFactory;
import com.solacesystems.jms.SolJmsUtility;
import io.smallrye.common.annotation.Identifier;
import jakarta.jms.ConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;
import org.eclipse.microprofile.config.ConfigProvider;

public class Producers {
    /**
     * Create a connection factory for Solace Event Broker.
     * <p/>
     * Since there is no Solace Event Broker extension for quarkus, we need to create the connection factory manually
     *
     * @return a new connection factory instance
     */
    @Identifier("solConnectionFactory")
    public SolConnectionFactory createSolConnectionFactory() throws Exception {
        SolConnectionFactory connectionFactory = SolJmsUtility.createConnectionFactory();
        connectionFactory.setHost(ConfigProvider.getConfig().getValue("solace.jms.host", String.class));
        connectionFactory.setVPN(ConfigProvider.getConfig().getValue("solace.jms.vpn", String.class));
        connectionFactory.setUsername((ConfigProvider.getConfig().getValue("solace.jms.username", String.class)));
        connectionFactory.setPassword((ConfigProvider.getConfig().getValue("solace.jms.password", String.class)));
        return connectionFactory;
    }

    /**
     * Define the "solace" jms component.
     *
     * @param  cf solace jms connection factory that is automatically injected by Quarkus based on the given identifier
     * @return    a new JmsComponent instance
     */
    @Identifier("solace")
    JmsComponent solace(@Identifier("solConnectionFactory") ConnectionFactory cf) {
        JmsComponent solace = new JmsComponent();
        solace.setConnectionFactory(cf);
        return solace;
    }
}
