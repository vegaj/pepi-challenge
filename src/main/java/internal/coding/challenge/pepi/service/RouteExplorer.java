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
import java.util.TreeSet;
import static java.util.stream.Collectors.toSet;

/**
 *
 * @author jose
 */
public class RouteExplorer {

    private TravelCosts travels;
    private int maxDuration;
    private final City base;

    /**
     * Creates a RouteExplorer to be used to give a set of possible destinations
     * to the RouteCalculator
     * 
     * @param costs a TravelCosts that determines the penalty for moving between two cities.
     * @param maxDuration the maximum number of cities that can be visited.
     * @param base the city every route must end at.
     */
    public RouteExplorer(TravelCosts costs, int maxDuration, City base) {
        this.travels = costs;
        this.maxDuration = maxDuration;
        this.base = base;
    }

    public TravelCosts getCosts() {
        return travels;
    }

    public void setCosts(TravelCosts costs) {
        this.travels = costs;
    }

    public int getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(int maxDuration) {
        this.maxDuration = maxDuration;
    }

    /**
     * This function will perform the decision making when expanding the next
     * routes from a given one, based on the heuristic of awards - costs,
     * discarding those routes that don't meet the maximum travel time criteria.
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

        //If the route is about to exceed the maximum duration, only consider 
        //the one that returns to base in one travel, if any.
        if (r.getLength() + 1 == maxDuration) {
            Set<Route> res = new TreeSet<>();
            if (travels.canTravelBetween(current, base.getName())) {
                res.add(r.withNextStep(base.getName(), travels.getTravelCost(current, base.getName()), base.getReward()));
            }
            return res;
        }

        return map.getCities().stream()
                //Consider the pending to visit and the base
                .filter((City c) -> !r.getCities().contains(c.getName()) || c.getBase())
                //Remove the ones that are not connected to the current city
                .filter((City c) -> travels.canTravelBetween(current, c.getName()))
                //Create a new rute per candidate city with the costs and the awards
                .map((City c) -> r.withNextStep(c.getName(), travels.getTravelCost(current, c.getName()), c.getReward()))
                .collect(toSet());
    }
}
