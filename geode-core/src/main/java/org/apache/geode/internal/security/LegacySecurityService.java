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
package org.apache.geode.internal.security;

import java.util.Properties;
import java.util.concurrent.Callable;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadState;

import org.apache.geode.management.internal.security.ResourceOperation;
import org.apache.geode.security.PostProcessor;
import org.apache.geode.security.ResourcePermission;
import org.apache.geode.security.SecurityManager;

/**
 * implementing SecurityService when only legacy authenticators are specified
 */
public class LegacySecurityService implements SecurityService {

  private final boolean hasClientAuthenticator;

  private final boolean hasPeerAuthenticator;

  LegacySecurityService() {
    hasClientAuthenticator = false;
    hasPeerAuthenticator = false;
  }

  LegacySecurityService(final String clientAuthenticator, final String peerAuthenticator) {
    this.hasClientAuthenticator = StringUtils.isNotBlank(clientAuthenticator);
    this.hasPeerAuthenticator = StringUtils.isNotBlank(peerAuthenticator);
  }

  @Override
  public boolean isClientSecurityRequired() {
    return this.hasClientAuthenticator;
  }

  @Override
  public boolean isIntegratedSecurity() {
    return false;
  }

  @Override
  public boolean isPeerSecurityRequired() {
    return this.hasPeerAuthenticator;
  }

}