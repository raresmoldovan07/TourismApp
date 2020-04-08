package tourism.app.persistence.data.access.entity;

public interface Entity<ID> {

    ID getId();

    void setId(ID id);
}
