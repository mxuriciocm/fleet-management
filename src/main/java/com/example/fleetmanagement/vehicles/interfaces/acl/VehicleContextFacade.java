package com.example.fleetmanagement.vehicles.interfaces.acl;

import com.example.fleetmanagement.vehicles.interfaces.acl.dto.VehicleDto;

import java.util.List;
import java.util.Optional;

public interface VehicleContextFacade {

    Optional<VehicleDto> fetchVehicleById(Long vehicleId);

    List<VehicleDto> fetchVehiclesByManagerId(Long managerId);

    Optional<VehicleDto> fetchVehicleByCarrierId(Long carrierId);

    Optional<VehicleDto> fetchVehicleByLicensePlate(String licensePlate);

    int countVehiclesByManagerId(Long managerId);

}
