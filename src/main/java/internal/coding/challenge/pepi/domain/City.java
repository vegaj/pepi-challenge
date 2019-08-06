package internal.coding.challenge.pepi.domain;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class City {

    @NotNull
    private String name;

    @NotNull
    @Min(0)
    private Integer reward;

    private Boolean base = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getReward() {
        return reward;
    }

    public void setReward(Integer reward) {
        this.reward = reward;
    }

    public Boolean getBase() {
        return base;
    }

    public void setBase(Boolean base) {
        this.base = base;
    }
}
