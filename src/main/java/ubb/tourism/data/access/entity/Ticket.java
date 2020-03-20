package ubb.tourism.data.access.entity;

public class Ticket implements Entity<Integer> {

    private Integer id;
    private Integer availableSpots;
    private String clientName;
    private String clientAddress;
    private String tourists;

    public Ticket(Integer id, Integer availableSpots, String clientName, String clientAddress, String tourists) {
        this.id = id;
        this.availableSpots = availableSpots;
        this.clientName = clientName;
        this.clientAddress = clientAddress;
        this.tourists = tourists;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAvailableSpots() {
        return availableSpots;
    }

    public void setAvailableSpots(Integer availableSpots) {
        this.availableSpots = availableSpots;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getTourists() {
        return tourists;
    }

    public void setTourists(String tourists) {
        this.tourists = tourists;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", availableSpots=" + availableSpots +
                ", clientName='" + clientName + '\'' +
                ", clientAddress='" + clientAddress + '\'' +
                ", tourists='" + tourists + '\'' +
                '}';
    }
}
