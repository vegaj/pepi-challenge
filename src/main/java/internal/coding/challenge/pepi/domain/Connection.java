package internal.coding.challenge.pepi.domain;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Connection {

    @NotNull
    String from;

    @NotNull
    String to;

    @NotNull
    @Min(0)
    Integer cost;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }
}
