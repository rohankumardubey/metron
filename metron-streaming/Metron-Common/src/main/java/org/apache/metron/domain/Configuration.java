/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.metron.domain;

import org.apache.curator.framework.CuratorFramework;
import org.apache.metron.utils.ConfigurationsUtils;

import java.nio.file.Path;
import java.util.Map;

public class Configuration extends Configurations {

    protected CuratorFramework curatorFramework = null;
    private Path configFileRoot;

    public Configuration(CuratorFramework curatorFramework){

        this.curatorFramework = curatorFramework;

    }


    public Configuration(Path configFileRoot){

        this.configFileRoot = configFileRoot;
    }

    public void update() throws Exception {

        if( null != curatorFramework ) {

            ConfigurationsUtils.updateConfigsFromZookeeper(this, this.curatorFramework);

        } else {

            updateGlobalConfig(ConfigurationsUtils.readGlobalConfigFromFile(configFileRoot.toAbsolutePath().toString()));
            Map<String, byte[]> sensorEnrichmentConfigs = ConfigurationsUtils.readSensorEnrichmentConfigsFromFile(configFileRoot.toAbsolutePath().toString());
            for(String sensorType: sensorEnrichmentConfigs.keySet()) {
                updateSensorEnrichmentConfig(sensorType, sensorEnrichmentConfigs.get(sensorType));
            }

        }

    }
}
