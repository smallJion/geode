/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.geode.connectors.jdbc.internal.cli;

import static org.apache.geode.distributed.ConfigurationProperties.ENABLE_CLUSTER_CONFIGURATION;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.apache.geode.cache.CacheFactory;
import org.apache.geode.connectors.jdbc.internal.ConnectionConfiguration;
import org.apache.geode.connectors.jdbc.internal.InternalJdbcConnectorService;
import org.apache.geode.internal.cache.InternalCache;
import org.apache.geode.management.cli.Result;

public class AlterConnectionCommandIntegrationTest {

  private InternalCache cache;
  private AlterConnectionCommand alterConnectionCommand;

  private String name;

  @Before
  public void setup() throws Exception {
    name = "name";
    String url = "url";
    String user = "user";
    String password = "password";
    String[] params = new String[] {"param1:value1", "param2:value2"};

    cache = (InternalCache) new CacheFactory().set(ENABLE_CLUSTER_CONFIGURATION, "true").create();
    (new CreateConnectionCommand()).createConnection(name, url, user, password, params);

    alterConnectionCommand = new AlterConnectionCommand();
  }

  @After
  public void tearDown() {
    cache.close();
  }

  @Test
  public void altersConnectionConfigurationInService() throws Exception {
    String[] newParams = new String[] {"key1:value1", "key2:value2"};
    Result result =
        alterConnectionCommand.alterConnection(name, "newUrl", "newUser", "newPassword", newParams);

    assertThat(result.getStatus()).isSameAs(Result.Status.OK);

    InternalJdbcConnectorService service = cache.getService(InternalJdbcConnectorService.class);
    ConnectionConfiguration connectionConfig = service.getConnectionConfig(name);

    assertThat(connectionConfig).isNotNull();
    assertThat(connectionConfig.getName()).isEqualTo(name);
    assertThat(connectionConfig.getUrl()).isEqualTo("newUrl");
    assertThat(connectionConfig.getUser()).isEqualTo("newUser");
    assertThat(connectionConfig.getPassword()).isEqualTo("newPassword");
    assertThat(connectionConfig.getConnectionProperties()).containsEntry("key1", "value1")
        .containsEntry("key2", "value2");
  }

}
