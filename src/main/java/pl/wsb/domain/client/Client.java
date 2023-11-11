package pl.wsb.domain.client;

import java.time.LocalDate;

public class Client {

    private String id;
    private String firstName;
    private String lastName;
    private ClientStatus status = ClientStatus.STANDARD;
    private final LocalDate createdAt;

    public Client(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = LocalDate.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ClientStatus getStatus() {
        return status;
    }

    public void setStatus(ClientStatus status) {
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }
}
