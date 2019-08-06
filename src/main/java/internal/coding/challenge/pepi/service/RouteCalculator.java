package internal.coding.challenge.pepi.service;

import internal.coding.challenge.pepi.domain.City;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import internal.coding.challenge.pepi.domain.Roadmap;
import internal.coding.challenge.pepi.domain.Route;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;

public class RouteCalculator {

    //Defined in the rules
    private static final int MAX_ROUTE_LENGTH = 7;

    //Peek for the most valuable
    private final PriorityQueue<Route> openRoutes = new PriorityQueue<>(
            Comparator.reverseOrder()
    );

    private final PriorityQueue<Route> solutions = new PriorityQueue<>(
            Comparator.reverseOrder()
    );

    private TravelCosts travels;
    private RouteExplorer explorer;

    public Optional<Route> calculateOptimalRoute(@NotNull Roadmap roadmap, @NotNull @Min(2) Integer maxRouteLength) {

        //Reset in case of being called multiple times.
        openRoutes.clear();
        solutions.clear();

        if (maxRouteLength > MAX_ROUTE_LENGTH) {
            throw new IllegalArgumentException("the route length can't exceed the maximum number of days (" + MAX_ROUTE_LENGTH + ")");
        }

        //Find the base city from roadmap.
        City base = getBase(roadmap.getCities());
        if (base == null) {
            throw new IllegalArgumentException("Could not find the base city in the given roadmap");
        }

        this.travels = new TravelCosts(roadmap);
        this.explorer = new RouteExplorer(travels, maxRouteLength, base);

        //Consider the base node to begin with
        openRoutes.add(Route.initial(base.getName()));

        while (!openRoutes.isEmpty()) {

            //Consider the best route so far 
            Route current = openRoutes.poll();

            if (isSolution(current, maxRouteLength)) {
                solutions.add(current);
            } else {
                //Explore new routes.
                Set<Route> newRoutes = explorer.getPossibleNextSteps(roadmap, current);
                openRoutes.addAll(newRoutes);
            }

        }

        //No solution found
        if (solutions.isEmpty()) {
            return Optional.empty();
        }

        final Route top = solutions.element();
        return solutions.stream()
                //We consider only the solutions with the highest score.
                .filter(r -> r.getTotalScore() == top.getTotalScore())
                //Then we sort them attending to the route length and take the smallest
                .min(Comparator.comparingInt(Route::getLength));
    }

    private boolean isSolution(Route r, int maxTravel) {
        List<String> cityNames = r.getCities();
        String first = cityNames.get(0), last = cityNames.get(cityNames.size() - 1);
        return r.getLength() > 1 && r.getLength() <= maxTravel && first.equals(last);
    }

    private City getBase(Collection<City> cities) {
        for (City c : cities) {
            if (c.getBase()) {
                return c;
            }
        }
        return null;
    }

    /**
     * This class is associated with the Route Calculator and assigns to each
     * displacement a cost
     */
    public static class TravelCosts {

        private Map<String, Map<String, Integer>> costs;

        public TravelCosts(Roadmap roadmap) {

            costs = new HashMap(roadmap.getCities().size());
            roadmap.getCities().forEach((c) -> {
                costs.put(c.getName(), new HashMap<>());
            });

            roadmap.getConnections().forEach((c) -> {
                Map<String, Integer> map = costs.get(c.getFrom());
                map.put(c.getTo(), c.getCost());
                map = costs.get(c.getTo());
                map.put(c.getFrom(), c.getCost());
            });
        }

        /**
         * Returns the cost for moving from a to b
         *
         * @param a the name of the ingoing city
         * @param b the name of the outgoing city
         * @return if the two cities are connected, returns the actual cost, if
         * there is no possible connection, returns the INTEGER.MAX_VALUE. It is
         * encouraged to call this function after canTravel
         */
        public Integer getTravelCost(String a, String b) {
            return costs.get(a).getOrDefault(b, Integer.MAX_VALUE);
        }

        /**
         * Checks if there is a connection between a and b
         *
         * @param a the name of a city
         * @param b the name of the other city
         * @return if a and b are connected.
         */
        public boolean canTravelBetween(String a, String b) {
            return costs.get(a).containsKey(b);
        }
    }
}
