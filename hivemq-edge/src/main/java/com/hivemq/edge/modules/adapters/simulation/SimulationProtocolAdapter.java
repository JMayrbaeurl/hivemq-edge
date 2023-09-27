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
package com.hivemq.edge.modules.adapters.simulation;

import com.codahale.metrics.MetricRegistry;
import com.hivemq.edge.modules.adapters.data.ProtocolAdapterDataSample;
import com.hivemq.edge.modules.adapters.impl.AbstractPollingPerSubscriptionAdapter;
import com.hivemq.edge.modules.adapters.params.ProtocolAdapterStartInput;
import com.hivemq.edge.modules.adapters.params.ProtocolAdapterStartOutput;
import com.hivemq.edge.modules.api.adapters.ProtocolAdapterInformation;
import com.hivemq.edge.modules.config.impl.AbstractProtocolAdapterConfig;
import com.hivemq.extension.sdk.api.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class SimulationProtocolAdapter extends AbstractPollingPerSubscriptionAdapter<SimulationAdapterConfig, ProtocolAdapterDataSample> {

    public SimulationProtocolAdapter(
            @NotNull final ProtocolAdapterInformation adapterInformation,
            @NotNull final SimulationAdapterConfig adapterConfig,
            @NotNull final MetricRegistry metricRegistry) {
        super(adapterInformation, adapterConfig, metricRegistry);
    }

    @Override
    public ConnectionStatus getConnectionStatus() {
        return ConnectionStatus.STATELESS;
    }

    @Override
    protected CompletableFuture<Void> startInternal(
            final @NotNull ProtocolAdapterStartInput input, final @NotNull ProtocolAdapterStartOutput output) {
        try {
            if (adapterConfig.getSubscriptions() != null) {
                for (SimulationAdapterConfig.Subscription subscription : adapterConfig.getSubscriptions()) {
                    startPolling(new SubscriptionSampler(adapterConfig, subscription));
                }
            }
            output.startedSuccessfully("Successfully connected");
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            output.failStart(e, e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    @Override
    protected CompletableFuture<Void> stopInternal() {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    protected ProtocolAdapterDataSample onSamplerInvoked(
            final SimulationAdapterConfig config,
            final AbstractProtocolAdapterConfig.Subscription subscription) {
        ProtocolAdapterDataSample dataSample =
                new ProtocolAdapterDataSample(ThreadLocalRandom.current().nextDouble(),
                    subscription.getDestination(),
                    subscription.getQos());
        return dataSample;
    }
}
