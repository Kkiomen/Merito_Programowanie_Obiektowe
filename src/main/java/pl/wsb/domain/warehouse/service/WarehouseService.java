package pl.wsb.domain.warehouse.service;

import com.capgemini.programowanie.obiektowe.*;
import pl.wsb.domain.client.Client;
import pl.wsb.domain.client.repository.ClientRepository;
import pl.wsb.domain.warehouse.repository.WarehouseRepository;

import java.util.*;

public class WarehouseService implements Warehouse {

    private WarehouseRepository warehouseRepository;
    private ClientRepository clientRepository;

    public WarehouseService(WarehouseRepository warehouseRepository, ClientRepository clientRepository) {
        this.warehouseRepository = warehouseRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public void addMetalIngot(String clientId, SupportedMetalType metalType, double mass) throws ClientNotFoundException, ProhibitedMetalTypeException, FullWarehouseException {
        Client client = this.clientRepository.findClientById(clientId);
        pl.wsb.domain.warehouse.Warehouse warehouse = this.warehouseRepository.getWarehouse();

        if (!EnumSet.allOf(SupportedMetalType.class).contains(metalType)) {
            throw new ProhibitedMetalTypeException();
        }

        double density = metalType.getDensity();
        double volume = mass / density;

        Map<SupportedMetalType, Double> clientInventory = warehouse.getInventory().getOrDefault(clientId, new HashMap<>());

        double totalVolume = clientInventory.values().stream().mapToDouble(Double::doubleValue).sum() + volume;
        if (totalVolume > pl.wsb.domain.warehouse.Warehouse.MAX_VOLUME) {
            throw new FullWarehouseException();
        }

        clientInventory.put(metalType, clientInventory.getOrDefault(metalType, 0.0) + mass);

        warehouse.getInventory().put(client.getId(), clientInventory);
    }

    @Override
    public Map<SupportedMetalType, Double> getMetalTypesToMassStoredByClient(String clientId) {

        pl.wsb.domain.warehouse.Warehouse warehouse = this.warehouseRepository.getWarehouse();

        Map<SupportedMetalType, Double> clientInventory = warehouse.getInventory().get(clientId);

        if (clientInventory == null) {
            return new HashMap<>();
        }

        return new HashMap<>(clientInventory);
    }

    @Override
    public double getTotalVolumeOccupiedByClient(String clientId) {
        pl.wsb.domain.warehouse.Warehouse warehouse = this.warehouseRepository.getWarehouse();

        Map<SupportedMetalType, Double> clientInventory = warehouse.getInventory().get(clientId);

        if (clientInventory == null) {
            return 0.0;
        }

        double totalVolume = 0.0;
        for (Map.Entry<SupportedMetalType, Double> entry : clientInventory.entrySet()) {
            SupportedMetalType metalType = entry.getKey();
            double mass = entry.getValue();
            double density = metalType.getDensity();
            totalVolume += mass / density;
        }

        return totalVolume;
    }

    @Override
    public List<SupportedMetalType> getStoredMetalTypesByClient(String clientId) {
        pl.wsb.domain.warehouse.Warehouse warehouse = this.warehouseRepository.getWarehouse();

        Map<SupportedMetalType, Double> clientInventory = warehouse.getInventory().get(clientId);

        if (clientInventory == null || clientInventory.isEmpty()) {
            return Collections.emptyList();
        }

        return new ArrayList<>(clientInventory.keySet());
    }
}
