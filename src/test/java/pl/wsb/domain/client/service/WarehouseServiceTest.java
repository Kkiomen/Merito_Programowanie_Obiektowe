package pl.wsb.domain.client.service;

import com.capgemini.programowanie.obiektowe.ClientNotFoundException;
import com.capgemini.programowanie.obiektowe.FullWarehouseException;
import com.capgemini.programowanie.obiektowe.ProhibitedMetalTypeException;
import com.capgemini.programowanie.obiektowe.SupportedMetalType;
import org.junit.jupiter.api.Test;
import pl.wsb.domain.client.repository.ClientRepository;
import pl.wsb.domain.warehouse.Warehouse;
import pl.wsb.domain.warehouse.repository.WarehouseRepository;
import pl.wsb.domain.warehouse.service.WarehouseService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class WarehouseServiceTest {

    ClientRepository clientRepositoryTest = new ClientRepository();
    WarehouseRepository warehouseRepositoryTest = new WarehouseRepository();
    ClientService clientServiceTest = new ClientService(clientRepositoryTest);
    WarehouseService warehouseServiceTest = new WarehouseService(warehouseRepositoryTest, clientRepositoryTest);

    @Test
    public void addMetalIngotWithValidData() {
        SupportedMetalType metalType = SupportedMetalType.COPPER;
        double mass = 100.0;

        String clientId = clientServiceTest.createNewClient("Jan", "Kowalski");
        Warehouse warehouse = warehouseRepositoryTest.getWarehouse();

        assertDoesNotThrow(() -> warehouseServiceTest.addMetalIngot(clientId, metalType, mass));
    }

    @Test
    public void addMetalIngotWithNotExistClient() {
        SupportedMetalType metalType = SupportedMetalType.COPPER;
        double mass = 100.0;

        String clientId = "999";

        assertThrows(ClientNotFoundException.class, () -> warehouseServiceTest.addMetalIngot(clientId, metalType, mass));
    }

    @Test
    public void getMetalTypesToMassWithValidClient() {
        String clientId = clientServiceTest.createNewClient("Jan", "Kowalski");

        Map<SupportedMetalType, Double> result = warehouseServiceTest.getMetalTypesToMassStoredByClient(clientId);
        assertNotNull(result);
    }

    @Test
    public void getTotalVolumeWithValidClient() {
        String clientId = clientServiceTest.createNewClient("Jan", "Kowalski");

        double totalVolume = warehouseServiceTest.getTotalVolumeOccupiedByClient(clientId);
        assertEquals(0.0, totalVolume);
    }

    @Test
    public void addMetalIngotAndVerifyInventory() throws Exception {
        String clientId = clientServiceTest.createNewClient("Jan", "Kowalski");
        SupportedMetalType metalType = SupportedMetalType.COPPER;
        double mass = 100.0;

        Warehouse warehouse = warehouseRepositoryTest.getWarehouse();

        warehouseServiceTest.addMetalIngot(clientId, metalType, mass);

        Map<String, Map<SupportedMetalType, Double>> inventory = warehouse.getInventory();
        assertTrue(inventory.containsKey(clientId));

        Map<SupportedMetalType, Double> clientInventory = inventory.get(clientId);
        assertTrue(clientInventory.containsKey(metalType));
        assertEquals(mass, clientInventory.get(metalType));
    }

    @Test
    public void addMetalIngotAndVerifyQuantities() throws Exception {
        String clientId = clientServiceTest.createNewClient("Jan", "Kowalski");
        SupportedMetalType metalType = SupportedMetalType.COPPER;
        double firstMass = 100.0;
        double secondMass = 200.0;
        double expectedTotalMass = firstMass + secondMass;

        Warehouse warehouse = warehouseRepositoryTest.getWarehouse();

        warehouseServiceTest.addMetalIngot(clientId, metalType, firstMass);
        warehouseServiceTest.addMetalIngot(clientId, metalType, secondMass);

        Map<String, Map<SupportedMetalType, Double>> inventory = warehouse.getInventory();
        assertTrue(inventory.containsKey(clientId));

        Map<SupportedMetalType, Double> clientInventory = inventory.get(clientId);
        assertTrue(clientInventory.containsKey(metalType));
        assertEquals(expectedTotalMass, clientInventory.get(metalType));
    }

    @Test
    public void getMetalTypesToMassWithExistingClient() {
        String clientId = clientServiceTest.createNewClient("Jan", "Kowalski");
        SupportedMetalType metalType = SupportedMetalType.COPPER;
        double mass = 100.0;

        Map<SupportedMetalType, Double> expectedInventory = new HashMap<>();
        expectedInventory.put(metalType, mass);

        Warehouse warehouse = warehouseRepositoryTest.getWarehouse();
        warehouse.getInventory().put(clientId, expectedInventory);

        Map<SupportedMetalType, Double> actualInventory = warehouseServiceTest.getMetalTypesToMassStoredByClient(clientId);
        assertEquals(expectedInventory, actualInventory);
    }

    @Test
    public void getMetalTypesToMassWithEmptyInventory() {
        String clientId = clientServiceTest.createNewClient("Jan", "Kowalski");

        Map<SupportedMetalType, Double> actualInventory = warehouseServiceTest.getMetalTypesToMassStoredByClient(clientId);
        assertTrue(actualInventory.isEmpty());
    }

    @Test
    public void getMetalTypesToMassAfterAddingMetal() {
        String clientId = clientServiceTest.createNewClient("Jan", "Kowalski");
        SupportedMetalType metalType = SupportedMetalType.COPPER;
        double mass = 100.0;

        warehouseServiceTest.addMetalIngot(clientId, metalType, mass);

        Map<SupportedMetalType, Double> inventoryAfterAdding = warehouseServiceTest.getMetalTypesToMassStoredByClient(clientId);

        assertNotNull(inventoryAfterAdding);
        assertTrue(inventoryAfterAdding.containsKey(metalType));
        assertEquals(mass, inventoryAfterAdding.get(metalType));
    }

    @Test
    public void getTotalVolumeForClientWithMetals() {
        String clientId = clientServiceTest.createNewClient("Jan", "Kowalski");
        SupportedMetalType metalType1 = SupportedMetalType.COPPER;
        double mass1 = 100.0;
        SupportedMetalType metalType2 = SupportedMetalType.IRON;
        double mass2 = 50.0;

        double expectedVolume = (mass1 / metalType1.getDensity()) + (mass2 / metalType2.getDensity());

        Map<SupportedMetalType, Double> clientInventory = new HashMap<>();
        clientInventory.put(metalType1, mass1);
        clientInventory.put(metalType2, mass2);

        Warehouse warehouse = warehouseRepositoryTest.getWarehouse();
        warehouse.getInventory().put(clientId, clientInventory);

        double totalVolume = warehouseServiceTest.getTotalVolumeOccupiedByClient(clientId);
        assertEquals(expectedVolume, totalVolume);
    }

    @Test
    public void getTotalVolumeForClientWithNoMetals() {
        String clientId = clientServiceTest.createNewClient("Jan", "Kowalski");

        double totalVolume = warehouseServiceTest.getTotalVolumeOccupiedByClient(clientId);
        assertEquals(0.0, totalVolume);
    }

    @Test
    public void getStoredMetalTypesWithMetals() {
        String clientId = clientServiceTest.createNewClient("Jan", "Kowalski");
        Set<SupportedMetalType> expectedMetalTypes = new HashSet<>(Arrays.asList(SupportedMetalType.COPPER, SupportedMetalType.IRON));

        Map<SupportedMetalType, Double> clientInventory = new HashMap<>();
        for (SupportedMetalType metalType : expectedMetalTypes) {
            clientInventory.put(metalType, 100.0);
        }

        Warehouse warehouse = warehouseRepositoryTest.getWarehouse();
        warehouse.getInventory().put(clientId, clientInventory);

        List<SupportedMetalType> storedMetalTypes = warehouseServiceTest.getStoredMetalTypesByClient(clientId);
        assertEquals(expectedMetalTypes.size(), storedMetalTypes.size());
        assertTrue(storedMetalTypes.containsAll(expectedMetalTypes));
    }

    @Test
    public void getStoredMetalTypesWithNoMetals() {
        String clientId = clientServiceTest.createNewClient("Jan", "Kowalski");

        List<SupportedMetalType> storedMetalTypes = warehouseServiceTest.getStoredMetalTypesByClient(clientId);
        assertTrue(storedMetalTypes.isEmpty());
    }

    @Test
    public void addMetalIngotThrowsFullWarehouseException() throws Exception {
        String clientId = clientServiceTest.createNewClient("Jan", "Kowalski");
        SupportedMetalType metalType = SupportedMetalType.COPPER;
        double massToAdd = 100.0;

        Map<SupportedMetalType, Double> nearlyFullInventory = new HashMap<>();
        nearlyFullInventory.put(SupportedMetalType.IRON, Warehouse.MAX_VOLUME * SupportedMetalType.IRON.getDensity() - 1.0);

        Warehouse warehouse = warehouseRepositoryTest.getWarehouse();
        warehouse.getInventory().put(clientId, nearlyFullInventory);

        assertThrows(FullWarehouseException.class, () -> warehouseServiceTest.addMetalIngot(clientId, metalType, massToAdd));
    }

    @Test
    public void addMetalIngotThrowsProhibitedMetalTypeException() throws Exception {
        String clientId = clientServiceTest.createNewClient("Jan", "Kowalski");
        SupportedMetalType prohibitedMetalType = null;
        double mass = 100.0;

        assertThrows(ProhibitedMetalTypeException.class, () -> warehouseServiceTest.addMetalIngot(clientId, prohibitedMetalType, mass));
    }
}
