package pl.wsb.domain.client.repository;

import com.capgemini.programowanie.obiektowe.ClientNotFoundException;
import pl.wsb.domain.client.Client;

import java.util.HashMap;
import java.util.Map;

public class ClientRepository {
    private final Map<String, Client> db;
    private int lastId;

    public ClientRepository(){
        this.db = new HashMap<>();
        this.lastId = 0;
    }

    /**
     * Adds Client to database
     * @return Created user's identifier.
     */
    public String addClient(Client client) {
        String newId = this.generateNextId();
        client.setId(newId);
        this.db.put(newId, client);

        return newId;
    }

    /**
     * Returns Client by theirs id
     * @return Client
     */
    public Client findClientById(String id) {
        Client client = this.db.get(id);

        if(client == null){
            throw new ClientNotFoundException();
        }

        return client;
    }

    /**
     * Return all Clients
     * @return Map<String,Client> of all Clients
     */
    public Map<String, Client> findAll() {
        return this.db;
    }

    /**
     * Returns Clients count
     * @return int clients count
     */
    public int count(){
        return this.db.size();
    }

    /**
     * Generate new unique Id for Client
     * @return String Client's id
     */
    private String generateNextId() {
        lastId += 1;
        return String.valueOf(lastId);
    }
}
