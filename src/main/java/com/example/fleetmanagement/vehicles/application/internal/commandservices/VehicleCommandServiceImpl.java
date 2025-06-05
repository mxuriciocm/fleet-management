package com.example.fleetmanagement.vehicles.application.internal.commandservices;

import com.example.fleetmanagement.vehicles.domain.model.aggregates.Vehicle;
import com.example.fleetmanagement.vehicles.domain.model.commands.CreateVehicleCommand;
import com.example.fleetmanagement.vehicles.domain.model.commands.UpdateVehicleCommand;
import com.example.fleetmanagement.vehicles.domain.model.valueobjects.VehicleStatus;
import com.example.fleetmanagement.vehicles.domain.services.VehicleCommandService;
import com.example.fleetmanagement.vehicles.infrastructure.persistence.jpa.repositories.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VehicleCommandServiceImpl implements VehicleCommandService {

    private static final int NON_PRO_VEHICLE_LIMIT = 10;

    private final VehicleRepository vehicleRepository;

    public VehicleCommandServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Vehicle handle(CreateVehicleCommand command) {
        var vehicle = new Vehicle(command);
        return vehicleRepository.save(vehicle);
    }

    @Override
    public Optional<Vehicle> handle(Long vehicleId, UpdateVehicleCommand command) {
        return vehicleRepository.findById(vehicleId)
                .map(vehicle -> {
                    // Actualizar solo los campos que no son nulos
                    if (command.brand() != null) {
                        vehicle.setBrand(command.brand());
                    }
                    if (command.model() != null) {
                        vehicle.setModel(command.model());
                    }
                    if (command.status() != null) {
                        vehicle.changeStatus(command.status());
                    }
                    return vehicleRepository.save(vehicle);
                });
    }

    @Override
    public Optional<Vehicle> handle(Long vehicleId, Long carrierId) {
        // Asignar un carrier a un vehículo
        return vehicleRepository.findById(vehicleId)
                .map(vehicle -> {
                    vehicle.assignCarrier(carrierId);
                    return vehicleRepository.save(vehicle);
                });
    }

    @Override
    public Optional<Vehicle> handle(Long vehicleId, VehicleStatus status) {
        // Cambiar el estado de un vehículo
        return vehicleRepository.findById(vehicleId)
                .map(vehicle -> {
                    vehicle.changeStatus(status);
                    return vehicleRepository.save(vehicle);
                });
    }

    @Override
    public Optional<Vehicle> handle(Long vehicleId) {
        // Eliminar un carrier de un vehículo
        return vehicleRepository.findById(vehicleId)
                .map(vehicle -> {
                    vehicle.removeCarrier();
                    return vehicleRepository.save(vehicle);
                });
    }

    @Override
    public boolean hasReachedVehicleLimit(Long managerId, boolean isPro) {
        if (isPro) {
            // Los usuarios PRO no tienen límite de vehículos
            return false;
        } else {
            // Los usuarios no PRO tienen un límite de 10 vehículos
            int vehicleCount = vehicleRepository.countByManagerId(managerId);
            return vehicleCount >= NON_PRO_VEHICLE_LIMIT;
        }
    }
}
