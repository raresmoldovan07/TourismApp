package tourism.app.persistence.data.access.entity;

import java.io.Serializable;

public interface Entity<ID> extends Serializable {

    ID getId();

    void setId(ID id);
}
