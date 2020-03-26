package ubb.tourism.data.access.entity;

public class Ticket implements Entity<Integer> {

    private Integer id;
    private Integer flightId;
    private Integer spots;
    private String clientName;
    private String clientAddress;
    private String tourists;

    public Ticket(Integer id, Integer flightId, Integer spots, String clientName, String clientAddress, String tourists) {
        this.id = id;
        this.flightId = flightId;
        this.spots = spots;
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

    public Integer getFlightId() {
        return flightId;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    public Integer getSpots() {
        return spots;
    }

    public void setSpots(Integer spots) {
        this.spots = spots;
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
                ", flightId=" + flightId +
                ", spots=" + spots +
                ", clientName='" + clientName + '\'' +
                ", clientAddress='" + clientAddress + '\'' +
                ", tourists='" + tourists + '\'' +
                '}';
    }
}
