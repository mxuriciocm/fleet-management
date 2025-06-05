package com.example.fleetmanagement.vehicles.application.acl;

import com.example.fleetmanagement.vehicles.domain.model.aggregates.Vehicle;
import com.example.fleetmanagement.vehicles.domain.model.queries.GetVehicleByIdQuery;
import com.example.fleetmanagement.vehicles.domain.model.queries.GetVehicleByLicensePlateQuery;
import com.example.fleetmanagement.vehicles.domain.model.queries.GetVehiclesByManagerIdQuery;
import com.example.fleetmanagement.vehicles.domain.services.VehicleQueryService;
import com.example.fleetmanagement.vehicles.interfaces.acl.VehicleContextFacade;
import com.example.fleetmanagement.vehicles.interfaces.acl.dto.VehicleDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleContextFacadeImpl implements VehicleContextFacade {
    private final VehicleQueryService vehicleQueryService;

    public VehicleContextFacadeImpl(VehicleQueryService vehicleQueryService) {
        this.vehicleQueryService = vehicleQueryService;
    }

    @Override
    public Optional<VehicleDto> fetchVehicleById(Long vehicleId){
        return vehicleQueryService.handle(new GetVehicleByIdQuery(vehicleId))
                .map(this::mapToDto);
    }

    @Override
    public List<VehicleDto> fetchVehiclesByManagerId(Long managerId){
        return vehicleQueryService.handle(new GetVehiclesByManagerIdQuery(managerId))
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<VehicleDto> fetchVehicleByLicensePlate(String licensePlate) {
        return vehicleQueryService.handle(new GetVehicleByLicensePlateQuery(licensePlate))
                .map(this::mapToDto);
    }

    @Override
    public Optional<VehicleDto> fetchVehicleByCarrierId(Long carrierId) {
        return vehicleQueryService.handle(new GetVehicleByIdQuery(carrierId))
                .map(this::mapToDto);
    }

    @Override
    public int countVehiclesByManagerId(Long managerId) {
        return vehicleQueryService.countVehiclesByManagerId(managerId);
    }

    private VehicleDto mapToDto(Vehicle vehicle) {
        return new VehicleDto(
                vehicle.getId(),
                vehicle.getLicensePlate(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getStatus(),
                vehicle.getCarrierId(),
                vehicle.getManagerId()
        );
    }
}
