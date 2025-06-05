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

    /**
     * Create a new vehicle.
     * @param command the command containing the vehicle details
     * @return the created vehicle
     */
    @Override
    public Vehicle handle(CreateVehicleCommand command) {
        var vehicle = new Vehicle(command);
        return vehicleRepository.save(vehicle);
    }

    /**
     * Update a vehicle's details.
     * @param vehicleId the vehicle id
     * @param command the command containing the updated details
     * @return an Optional containing the updated vehicle if successful, or empty if not found
     */
    @Override
    public Optional<Vehicle> handle(Long vehicleId, UpdateVehicleCommand command) {
        return vehicleRepository.findById(vehicleId)
                .map(vehicle -> {
                    if (command.brand() != null) { vehicle.setBrand(command.brand()); }
                    if (command.model() != null) { vehicle.setModel(command.model()); }
                    return vehicleRepository.save(vehicle);
                });
    }

    /**
     * Assign a carrier to a vehicle.
     * @param vehicleId the vehicle id
     * @param carrierId the carrier id
     * @return an Optional containing the updated vehicle if successful, or empty if not found
     */
    @Override
    public Optional<Vehicle> handle(Long vehicleId, Long carrierId) {
        return vehicleRepository.findById(vehicleId)
                .map(vehicle -> {
                    vehicle.assignCarrier(carrierId);
                    return vehicleRepository.save(vehicle);
                });
    }

    /**
     * Change the status of a vehicle.
     * @param vehicleId the vehicle id
     * @param status the new status of the vehicle
     * @return an Optional containing the updated vehicle if successful, or empty if not found
     */
    @Override
    public Optional<Vehicle> handle(Long vehicleId, VehicleStatus status) {
        return vehicleRepository.findById(vehicleId)
                .map(vehicle -> {
                    vehicle.changeStatus(status);
                    return vehicleRepository.save(vehicle);
                });
    }

    /**
     * Remove the carrier from a vehicle.
     * @param vehicleId the vehicle id
     * @return an Optional containing the updated vehicle if successful, or empty if not found
     */
    @Override
    public Optional<Vehicle> handle(Long vehicleId) {
        return vehicleRepository.findById(vehicleId)
                .map(vehicle -> {
                    vehicle.removeCarrier();
                    return vehicleRepository.save(vehicle);
                });
    }

    /**
     * Check if the manager has reached the vehicle limit.
     * @param managerId the manager id
     * @param isPro true if the user is a PRO user, false otherwise
     * @return true if the manager has reached the vehicle limit, false otherwise
     */
    @Override
    public boolean hasReachedVehicleLimit(Long managerId, boolean isPro) {
        if (isPro) {
            return false;
        } else {
            int vehicleCount = vehicleRepository.countByManagerId(managerId);
            return vehicleCount >= NON_PRO_VEHICLE_LIMIT;
        }
    }
}
