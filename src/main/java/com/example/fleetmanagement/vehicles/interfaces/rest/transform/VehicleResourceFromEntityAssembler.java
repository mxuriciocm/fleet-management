package com.example.fleetmanagement.vehicles.interfaces.rest.transform;

import com.example.fleetmanagement.vehicles.domain.model.aggregates.Vehicle;
import com.example.fleetmanagement.vehicles.interfaces.rest.resources.VehicleResource;

public class VehicleResourceFromEntityAssembler {
    public static VehicleResource toResourceFromEntity(Vehicle entity){
        return new VehicleResource(entity.getId(), entity.getLicensePlate(), entity.getBrand(), entity.getModel(), entity.getStatus(), entity.getCarrierId(), entity.getManagerId());
    }
}
