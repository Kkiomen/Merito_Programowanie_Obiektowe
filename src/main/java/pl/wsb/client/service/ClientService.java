package pl.wsb.client.service;

import com.capgemini.programowanie.obiektowe.Clients;
import pl.wsb.client.Client;
import pl.wsb.client.ClientStatus;
import pl.wsb.client.repository.ClientRepository;

import java.time.LocalDate;
import java.util.Map;

public class ClientService implements Clients {
    private final ClientRepository clientRepository;

    public ClientService() {
        this.clientRepository = new ClientRepository();
    }

    public String createNewClient(String firstName, String lastName) {
        Client newClient = new Client(firstName, lastName);

        return this.clientRepository.addClient(newClient);
    }

    @Override
    public String activatePremiumAccount(String clientId) {
        Client client = this.clientRepository.findClientById(clientId);
        client.setStatus(ClientStatus.PREMIUM);

        return clientId;
    }

    @Override
    public String getClientFullName(String clientId) {
        Client client = this.clientRepository.findClientById(clientId);

        return client.getFirstName() + " " + client.getLastName();
    }

    @Override
    public LocalDate getClientCreationDate(String clientId) {
        Client client = this.clientRepository.findClientById(clientId);

        return client.getCreatedAt();
    }

    @Override
    public boolean isPremiumClient(String clientId) {
        Client client = this.clientRepository.findClientById(clientId);

        return client.getStatus() == ClientStatus.PREMIUM;
    }

    @Override
    public int getNumberOfClients() {
        return this.clientRepository.count();
    }

    @Override
    public int getNumberOfPremiumClients() {
        Map<String, Client> clients = this.clientRepository.findAll();

        return (int) clients.values()
                .stream()
                .filter(client -> client.getStatus() == ClientStatus.PREMIUM)
                .count();
    }
}
