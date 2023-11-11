package pl.wsb;

import com.capgemini.programowanie.obiektowe.SupportedMetalType;
import pl.wsb.domain.client.repository.ClientRepository;
import pl.wsb.domain.client.service.ClientService;
import pl.wsb.domain.warehouse.repository.WarehouseRepository;
import pl.wsb.domain.warehouse.service.WarehouseService;

public class Main {
    public static void main(String[] args) {
        ClientRepository clientRepository = new ClientRepository();
        ClientService clientService = new ClientService(clientRepository);
        WarehouseRepository warehouseRepository = new WarehouseRepository();
        WarehouseService warehouseService = new WarehouseService(warehouseRepository, clientRepository);



        String clientId = clientService.createNewClient("Jakub", "Owsianka");
        warehouseService.addMetalIngot(clientId, SupportedMetalType.IRON, 600);
        warehouseService.addMetalIngot(clientId, SupportedMetalType.GOLD, 8000);
        warehouseService.addMetalIngot(clientId, SupportedMetalType.IRON, 500);

        warehouseService.getStoredMetalTypesByClient(clientId);
        warehouseService.getTotalVolumeOccupiedByClient(clientId);
        warehouseService.getMetalTypesToMassStoredByClient(clientId);


        String clientId2 = clientService.createNewClient("Jan", "Kowalski");
        warehouseService.addMetalIngot(clientId2, SupportedMetalType.COPPER, 600);
        warehouseService.addMetalIngot(clientId2, SupportedMetalType.SILVER, 8000);
        warehouseService.addMetalIngot(clientId2, SupportedMetalType.PLATINUM, 500);


        warehouseService.getStoredMetalTypesByClient(clientId2);
        warehouseService.getTotalVolumeOccupiedByClient(clientId2);
        warehouseService.getMetalTypesToMassStoredByClient(clientId2);


        clientService.activatePremiumAccount(clientId);
        System.out.println(clientService.getNumberOfPremiumClients());
    }
}