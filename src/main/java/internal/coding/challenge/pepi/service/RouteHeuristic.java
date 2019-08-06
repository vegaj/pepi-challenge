/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internal.coding.challenge.pepi.service;

import internal.coding.challenge.pepi.domain.City;
import internal.coding.challenge.pepi.domain.Roadmap;
import internal.coding.challenge.pepi.domain.Route;
import internal.coding.challenge.pepi.service.RouteCalculator.TravelCosts;
import java.util.Set;
import static java.util.stream.Collectors.toSet;

/**
 *
 * @author jose
 */
public class RouteHeuristic {

    private TravelCosts costs;
    private int maxDuration;

    public RouteHeuristic(TravelCosts costs, int maxDuration) {
        this.costs = costs;
        this.maxDuration = maxDuration;
    }

    public TravelCosts getCosts() {
        return costs;
    }

    public void setCosts(TravelCosts costs) {
        this.costs = costs;
    }

    public int getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(int maxDuration) {
        this.maxDuration = maxDuration;
    }

    /**
     *
     * @param map the globe context.
     * @param r the rute from which start to explore.
     * @return A set of routes whose next step is a city connected with the last
     * city in the route, that has not been visited yet (or the base) and then
     * retains only the routes that has a duration below or equals the travel
     * time limit.
     */
    public Set<Route> getPossibleNextSteps(Roadmap map, Route r) {
        String current = r.getCities().get(r.getCities().size() - 1);
        return map.getCities().stream()
                .filter((City c) -> !r.getCities().contains(c.getName()) || c.getBase())
                .filter((City c) -> {
                    int cost = costs.getCost(current, c.getName());
                    return cost != Integer.MAX_VALUE;
                })
                .map((City c) -> r.withNextStep(c.getName(), costs.getCost(current, c.getName()), c.getReward()))
                .filter((Route route) -> route.getLength() <= this.maxDuration)
                .collect(toSet());

    }
}
