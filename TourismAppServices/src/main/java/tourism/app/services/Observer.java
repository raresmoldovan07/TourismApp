package tourism.app.services;

import tourism.app.persistence.data.access.entity.Flight;

public interface Observer {

    void update(Flight[] flights);
}
