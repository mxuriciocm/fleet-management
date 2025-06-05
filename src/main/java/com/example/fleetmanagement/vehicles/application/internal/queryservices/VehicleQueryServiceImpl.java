package com.example.fleetmanagement.vehicles.application.internal.queryservices;

import com.example.fleetmanagement.vehicles.domain.model.aggregates.Vehicle;
import com.example.fleetmanagement.vehicles.domain.model.queries.GetVehicleByCarrierIdQuery;
import com.example.fleetmanagement.vehicles.domain.model.queries.GetVehicleByIdQuery;
import com.example.fleetmanagement.vehicles.domain.model.queries.GetVehicleByLicensePlateQuery;
import com.example.fleetmanagement.vehicles.domain.model.queries.GetVehiclesByManagerIdQuery;
import com.example.fleetmanagement.vehicles.domain.services.VehicleQueryService;
import com.example.fleetmanagement.vehicles.infrastructure.persistence.jpa.repositories.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleQueryServiceImpl implements VehicleQueryService {

    private final VehicleRepository vehicleRepository;

    public VehicleQueryServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Optional<Vehicle> handle(GetVehicleByIdQuery query) {
        System.out.println("Searching for vehicle with ID: " + query.vehicleId()); // Log
        return vehicleRepository.findById(query.vehicleId());
    }

    @Override
    public List<Vehicle> handle(GetVehiclesByManagerIdQuery query) {
        System.out.println("Searching for vehicles with manager ID: " + query.managerId()); // Log
        List<Vehicle> vehicles = vehicleRepository.findByManagerId(query.managerId());
        System.out.println("Found " + vehicles.size() + " vehicles for manager"); // Log
        return vehicles;
    }

    @Override
    public Optional<Vehicle> handle(GetVehicleByCarrierIdQuery query) {
        System.out.println("Searching for vehicle assigned to carrier ID: " + query.carrierId()); // Log
        return vehicleRepository.findByCarrierId(query.carrierId());
    }

    @Override
    public Optional<Vehicle> handle(GetVehicleByLicensePlateQuery query) {
        System.out.println("Searching for vehicle with license plate: " + query.licensePlate()); // Log
        return vehicleRepository.findByLicensePlate(query.licensePlate());
    }

    @Override
    public int countVehiclesByManagerId(Long managerId) {
        return vehicleRepository.countByManagerId(managerId);
    }
}
