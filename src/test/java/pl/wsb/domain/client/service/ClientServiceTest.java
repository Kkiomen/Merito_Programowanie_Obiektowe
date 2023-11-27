package pl.wsb.domain.client.service;

import com.capgemini.programowanie.obiektowe.ClientNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.wsb.domain.client.Client;
import pl.wsb.domain.client.ClientStatus;
import pl.wsb.domain.client.repository.ClientRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ClientServiceTest {

    ClientRepository clientRepositoryTest = new ClientRepository();
    ClientService clientServiceTest = new ClientService(clientRepositoryTest);

    @Test
    void createNewClient() {

        String clientId = clientServiceTest.createNewClient("Jan", "Kowalski");

        // Create user with id 1
        Assertions.assertEquals(clientId, "1");

        // Get correct data
        Client client = clientRepositoryTest.findClientById(clientId);
        Assertions.assertEquals(clientId, client.getId());
        Assertions.assertEquals("Jan", client.getFirstName());
        Assertions.assertEquals("Kowalski", client.getLastName());

        // Correct count client in database
        Assertions.assertEquals(1, clientRepositoryTest.count());
    }

    @Test
    void activatePremiumAccount() {
        String clientId = clientServiceTest.createNewClient("Jan", "Kowalski");
        clientServiceTest.activatePremiumAccount(clientId);

        Client client = clientRepositoryTest.findClientById(clientId);

        Assertions.assertEquals(ClientStatus.PREMIUM, client.getStatus());
    }

    @Test
    void notActivatePremiumDifferentAccount() {
        String firstClientId = clientServiceTest.createNewClient("Anna", "Nowak");
        String secondClientId = clientServiceTest.createNewClient("Jan", "Kowalski");
        String thirdClientId = clientServiceTest.createNewClient("Jakub", "Owsianka");

        clientServiceTest.activatePremiumAccount(secondClientId);

        Client firstClient = clientRepositoryTest.findClientById(firstClientId);
        Client secondClient = clientRepositoryTest.findClientById(secondClientId);
        Client thirdClient = clientRepositoryTest.findClientById(thirdClientId);

        Assertions.assertEquals(ClientStatus.STANDARD, firstClient.getStatus());
        Assertions.assertEquals(ClientStatus.PREMIUM, secondClient.getStatus());
        Assertions.assertEquals(ClientStatus.STANDARD, thirdClient.getStatus());
    }

    @Test
    void getClientFullName() {
        String clientId = clientServiceTest.createNewClient("Jan", "Kowalski");
        Client client = clientRepositoryTest.findClientById(clientId);
        Assertions.assertEquals(client.getFirstName() + " " + client.getLastName(), clientServiceTest.getClientFullName(clientId));
    }

    @Test
    void getClientCreationDate() {
        String clientId = clientServiceTest.createNewClient("Jan", "Kowalski");
        Client client = clientRepositoryTest.findClientById(clientId);

        Assertions.assertNotNull(clientServiceTest.getClientCreationDate(clientId));
        Assertions.assertEquals(client.getCreatedAt(), clientServiceTest.getClientCreationDate(clientId));
    }

    @Test
    void isPremiumClient() {
        String clientId = clientServiceTest.createNewClient("Jan", "Kowalski");
        clientServiceTest.activatePremiumAccount(clientId);

        Client client = clientRepositoryTest.findClientById(clientId);

        Assertions.assertEquals(client.getStatus() == ClientStatus.PREMIUM, clientServiceTest.isPremiumClient(clientId));
    }

    @Test
    void getNumberOfClients() {
        clientServiceTest.createNewClient("Anna", "Nowak");
        clientServiceTest.createNewClient("Jan", "Kowalski");
        clientServiceTest.createNewClient("Jakub", "Owsianka");

        Assertions.assertEquals(3, clientServiceTest.getNumberOfClients());
        Assertions.assertEquals(clientRepositoryTest.count(), clientServiceTest.getNumberOfClients());
    }

    @Test
    void getNumberOfPremiumClients() {
        String firstClientId = clientServiceTest.createNewClient("Anna", "Nowak");
        String secondClientId = clientServiceTest.createNewClient("Jan", "Kowalski");
        String thirdClientId = clientServiceTest.createNewClient("Jakub", "Owsianka");

        clientServiceTest.activatePremiumAccount(secondClientId);
        clientServiceTest.activatePremiumAccount(thirdClientId);

        Assertions.assertEquals(2, clientServiceTest.getNumberOfPremiumClients());
    }

    @Test
    void activatePremiumAccountThrowExceptionWhenClientNotExists() {
        ClientNotFoundException thrown  = assertThrows(
                ClientNotFoundException.class,
                () -> clientServiceTest.activatePremiumAccount("999")
        );
    }

    @Test
    void getClientFullNameThrowExceptionWhenClientNotExists() {
        ClientNotFoundException thrown  = assertThrows(
                ClientNotFoundException.class,
                () -> clientServiceTest.getClientFullName("999")
        );
    }

    @Test
    void getClientCreationDateThrowExceptionWhenClientNotExists() {
        ClientNotFoundException thrown  = assertThrows(
                ClientNotFoundException.class,
                () -> clientServiceTest.getClientCreationDate("999")
        );
    }

    @Test
    void isPremiumClientThrowExceptionWhenClientNotExists() {
        ClientNotFoundException thrown  = assertThrows(
                ClientNotFoundException.class,
                () -> clientServiceTest.isPremiumClient("999")
        );
    }
}