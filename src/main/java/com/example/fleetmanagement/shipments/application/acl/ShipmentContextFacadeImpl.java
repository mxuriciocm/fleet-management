package com.example.fleetmanagement.shipments.application.acl;

import com.example.fleetmanagement.shipments.domain.model.aggregates.Shipment;
import com.example.fleetmanagement.shipments.domain.model.queries.GetShipmentByIdQuery;
import com.example.fleetmanagement.shipments.domain.model.queries.GetShipmentsByCarrierIdQuery;
import com.example.fleetmanagement.shipments.domain.model.queries.GetShipmentsByManagerIdQuery;
import com.example.fleetmanagement.shipments.domain.model.queries.GetShipmentsByStatusQuery;
import com.example.fleetmanagement.shipments.domain.model.valueobjects.ShipmentStatus;
import com.example.fleetmanagement.shipments.domain.services.ShipmentQueryService;
import com.example.fleetmanagement.shipments.interfaces.acl.ShipmentContextFacade;
import com.example.fleetmanagement.shipments.interfaces.acl.dto.ShipmentDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShipmentContextFacadeImpl implements ShipmentContextFacade {
    private final ShipmentQueryService shipmentQueryService;

    public ShipmentContextFacadeImpl(ShipmentQueryService shipmentQueryService) {
        this.shipmentQueryService = shipmentQueryService;
    }


    /**
     * Fetches a shipment by its ID.
     * @param shipmentId the ID of the shipment to fetch
     * @return an Optional containing the ShipmentDto if found, or an empty Optional if not found
     */
    @Override
    public Optional<ShipmentDto> fetchShipmentById(Long shipmentId){
        return shipmentQueryService.handle(new GetShipmentByIdQuery(shipmentId))
                .map(this::mapToDto);
    }


    /**
     * Fetches all shipments associated with a specific manager ID.
     * @param managerId the ID of the manager whose shipments are to be fetched
     * @return a list of ShipmentDto objects associated with the specified manager ID
     */
    @Override
    public List<ShipmentDto> fetchShipmentsByManagerId(Long managerId) {
        return shipmentQueryService.handle(new GetShipmentsByManagerIdQuery(managerId))
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    /**
     * Fetches all shipments associated with a specific carrier ID.
     * @param carrierId the ID of the carrier whose shipments are to be fetched
     * @return a list of ShipmentDto objects associated with the specified carrier ID
     */
    @Override
    public List<ShipmentDto> fetchShipmentsByCarrierId(Long carrierId) {
        return shipmentQueryService.handle(new GetShipmentsByCarrierIdQuery(carrierId))
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    /**
     * Fetches all shipments with a specific status.
     * @param status the status of the shipments to fetch
     * @return a list of ShipmentDto objects with the specified status
     */
    @Override
    public List<ShipmentDto> fetchShipmentsByStatus(ShipmentStatus status){
        return shipmentQueryService.handle(new GetShipmentsByStatusQuery(status))
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    /**
     * Counts the number of active shipments for a specific manager.
     * Active shipments are those that are either pending or in progress.
     * @param managerId the ID of the manager whose active shipments are to be counted
     * @return the count of active shipments for the specified manager
     */
    @Override
    public int countActiveShipmentsByManagerId(Long managerId) {
        List<Shipment> managerShipments = shipmentQueryService.handle(new GetShipmentsByManagerIdQuery(managerId));
        return (int) managerShipments.stream()
                .filter(shipment -> shipment.getStatus() == ShipmentStatus.PENDING || shipment.getStatus() == ShipmentStatus.IN_PROGRESS)
                .count();
    }


    /**
     * Maps a Shipment entity to a ShipmentDto.
     * @param shipment the Shipment entity to map
     * @return a ShipmentDto containing the mapped data
     */
    private ShipmentDto mapToDto(Shipment shipment){
        return new ShipmentDto(shipment.getId(),
                shipment.getDestination(),
                shipment.getDescription(),
                shipment.getStatus(),
                shipment.getScheduledDate(),
                shipment.getStartedDate(),
                shipment.getCompletedDate(),
                shipment.getManagerId(),
                shipment.getManagerId());
    }
}
