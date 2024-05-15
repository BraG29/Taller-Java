package com.traffic.monitoring.Interface.impl;

import com.traffic.events.CustomEvent;
import com.traffic.events.VehiclePassEvent;
import com.traffic.monitoring.Interface.MonitoringController;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

@EnableAutoWeld
@AddPackages(MonitoringControllerImpl.class)
class MonitoringControllerTest {

    @Inject
    MonitoringController monitoringController;
    @Inject
    private Event<CustomEvent> singleEvent;

    @Test
    void vehiclePassTest(){
        CustomEvent customEvent = new VehiclePassEvent("Descripcion de prueba para la pasada de un vehiculo");
        singleEvent.fire(customEvent);
    }

}