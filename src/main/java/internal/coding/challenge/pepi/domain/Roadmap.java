package internal.coding.challenge.pepi.domain;

import java.util.Set;

import javax.validation.constraints.NotNull;

public class Roadmap {

    @NotNull
    Set<City> cities;

    @NotNull
    Set<Connection> connections;

    public Set<City> getCities() {
        return cities;
    }

    public void setCities(Set<City> cities) {
        this.cities = cities;
    }

    public Set<Connection> getConnections() {
        return connections;
    }

    public void setConnections(Set<Connection> connections) {
        this.connections = connections;
    }
}
