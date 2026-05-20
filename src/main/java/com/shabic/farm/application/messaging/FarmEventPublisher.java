package com.shabic.farm.application.messaging;

import com.shabic.farm.domain.events.FarmCreated;
import com.shabic.farm.domain.events.FarmDeleted;

public interface FarmEventPublisher {
	void publishFarmCreated(FarmCreated event);

	void publishFarmDeleted(FarmDeleted event);
}
