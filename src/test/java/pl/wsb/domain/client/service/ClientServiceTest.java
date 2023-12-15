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
    void testShouldCreateNewClientAndReturnClientId() {
        String clientId = clientServiceTest.createNewClient("Jan", "Kowalski");

        // Create user with id 1
        Assertions.assertEquals(clientId, "1");
    }

    @Test
    void testShouldCreateNewClientAndReturnCorrectName() {
        String givenClientId = clientServiceTest.createNewClient("Jan", "Kowalski");

        Client whenClient = clientRepositoryTest.findClientById(givenClientId);

        Assertions.assertEquals("Jan", whenClient.getFirstName());
        Assertions.assertEquals("Kowalski", whenClient.getLastName());
    }

    @Test
    void testShouldCreateNewClientAndReturnCorrectCountClient() {
        clientServiceTest.createNewClient("Jan", "Kowalski");

        Assertions.assertEquals(1, clientRepositoryTest.count());
    }

    @Test
    void testShouldActivePremiumAccount() {
        String clientId = clientServiceTest.createNewClient("Jan", "Kowalski");
        clientServiceTest.activatePremiumAccount(clientId);

        Client client = clientRepositoryTest.findClientById(clientId);

        Assertions.assertEquals(ClientStatus.PREMIUM, client.getStatus());
    }

    @Test
    void testShouldActivePremiumAccountOnlyUsedActivatePremiumAccountMethod() {
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
    void testShouldReturnClientFullNameWhenUsedGetClientFullNameMethod() {
        String clientId = clientServiceTest.createNewClient("Jan", "Kowalski");
        Client client = clientRepositoryTest.findClientById(clientId);
        Assertions.assertEquals(client.getFirstName() + " " + client.getLastName(), clientServiceTest.getClientFullName(clientId));
    }

    @Test
    void testShouldReturnClientCreationDateWhenUsedGetClientCreationDate() {
        String clientId = clientServiceTest.createNewClient("Jan", "Kowalski");
        Client client = clientRepositoryTest.findClientById(clientId);

        Assertions.assertNotNull(clientServiceTest.getClientCreationDate(clientId));
        Assertions.assertEquals(client.getCreatedAt(), clientServiceTest.getClientCreationDate(clientId));
    }

    @Test
    void testShouldReturnPremiumStatusWhenUsedMethodIsPremiumClient() {
        String clientId = clientServiceTest.createNewClient("Jan", "Kowalski");
        clientServiceTest.activatePremiumAccount(clientId);

        Client client = clientRepositoryTest.findClientById(clientId);

        Assertions.assertEquals(client.getStatus() == ClientStatus.PREMIUM, clientServiceTest.isPremiumClient(clientId));
    }

    @Test
    void testShouldReturnCountOfClientWhenUsedMethodGetNumberOfClients() {
        clientServiceTest.createNewClient("Anna", "Nowak");
        clientServiceTest.createNewClient("Jan", "Kowalski");
        clientServiceTest.createNewClient("Jakub", "Owsianka");

        Assertions.assertEquals(3, clientServiceTest.getNumberOfClients());
        Assertions.assertEquals(clientRepositoryTest.count(), clientServiceTest.getNumberOfClients());
    }

    @Test
    void testShouldReturnCountOfPremiumClientWhenUsedMethodGetNumberOfPremiumClients() {
        String firstClientId = clientServiceTest.createNewClient("Anna", "Nowak");
        String secondClientId = clientServiceTest.createNewClient("Jan", "Kowalski");
        String thirdClientId = clientServiceTest.createNewClient("Jakub", "Owsianka");

        clientServiceTest.activatePremiumAccount(secondClientId);
        clientServiceTest.activatePremiumAccount(thirdClientId);

        Assertions.assertEquals(2, clientServiceTest.getNumberOfPremiumClients());
    }

    @Test
    void testShouldThrowExceptionWhenClientNotExistsUsedActivatePremiumAccountMethod() {
        ClientNotFoundException thrown  = assertThrows(
                ClientNotFoundException.class,
                () -> clientServiceTest.activatePremiumAccount("999")
        );
    }

    @Test
    void testShouldThrowExceptionWhenClientNotExistsUsedGetClientFullNameMethod() {
        ClientNotFoundException thrown  = assertThrows(
                ClientNotFoundException.class,
                () -> clientServiceTest.getClientFullName("999")
        );
    }

    @Test
    void testShouldThrowExceptionWhenClientNotExistsUsedGetClientCreationDateMethod() {
        ClientNotFoundException thrown  = assertThrows(
                ClientNotFoundException.class,
                () -> clientServiceTest.getClientCreationDate("999")
        );
    }

    @Test
    void testShouldThrowExceptionWhenClientNotExistsUsedGetIsPremiumClientMethod() {
        ClientNotFoundException thrown  = assertThrows(
                ClientNotFoundException.class,
                () -> clientServiceTest.isPremiumClient("999")
        );
    }
}