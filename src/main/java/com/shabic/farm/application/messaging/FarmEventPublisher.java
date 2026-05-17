package com.shabic.farm.application.messaging;

import com.shabic.farm.domain.events.FarmCreated;

public interface FarmEventPublisher {
	void publishFarmCreated(FarmCreated event);
}
