/*
 * Copyright 2019-present HiveMQ GmbH
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
package com.hivemq.edge.modules.api.adapters;

import com.hivemq.edge.modules.adapters.params.ProtocolAdapterInput;
import com.hivemq.edge.modules.config.CustomConfig;
import com.hivemq.extension.sdk.api.annotations.NotNull;

import java.util.Map;

public interface ProtocolAdapterFactory<E extends CustomConfig> {

    @NotNull ProtocolAdapterInformation getInformation();

    @NotNull ProtocolAdapter createAdapter(@NotNull ProtocolAdapterInformation adapterInformation, @NotNull ProtocolAdapterInput<E> input);

    @NotNull E convertConfigObject(final @NotNull Map<String, Object> config);

    @NotNull Map<String, Object> unconvertConfigObject(final CustomConfig config);

    @NotNull Class<E> getConfigClass();
}