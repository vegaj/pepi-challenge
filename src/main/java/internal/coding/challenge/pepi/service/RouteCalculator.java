package internal.coding.challenge.pepi.service;

import internal.coding.challenge.pepi.domain.City;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import internal.coding.challenge.pepi.domain.Roadmap;
import internal.coding.challenge.pepi.domain.Route;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class RouteCalculator {

    //Peek for the most valuable
    PriorityQueue<Route> openRoutes = new PriorityQueue<>(
            (a, b) -> b.getTotalScore() - a.getTotalScore()
    );

    PriorityQueue<Route> solutions = new PriorityQueue<>(
            (a, b) -> b.getTotalScore() - a.getTotalScore()
    );

    TravelCosts costs = null;
    RouteHeuristic heuristic = null;

    public Route calculateOptimalRoute(@NotNull Roadmap roadmap, @NotNull @Min(2) Integer maxRouteLength) {

        //Reset in case of being called multiple times.
        openRoutes.clear();

        this.costs = new TravelCosts(roadmap);
        this.heuristic = new RouteHeuristic(costs, maxRouteLength);

        City base = getBase(roadmap.getCities());
        if (base == null) {
            throw new RuntimeException("Could not find the base city in the given roadmap");
        }

        //Sets the openRoutes to contain only the base
        openRoutes.add(Route.initial(base.getName()));
        while (!openRoutes.isEmpty()) {
            Route current = openRoutes.poll();

            if (isSolution(current, maxRouteLength)) {
                solutions.add(current);
                //Retain only those pending routes that can still improve the score.
                //openRoutes.removeIf(r -> r.compareTo(current) < 0);
            } else {
                //Explore new routes.
                Set<Route> newRoutes = heuristic.getPossibleNextSteps(roadmap, current);
                openRoutes.addAll(newRoutes);
            }
        }

        if (solutions.isEmpty()) { //No solution found
            return null;
        }

        final Route top = solutions.element();
        return solutions.stream()
                //We consider only the solutions with the highest score.
                .filter(r -> r.getTotalScore() == top.getTotalScore())
                //Then we sort them attending to the route length
                .sorted((a, b) -> a.getLength() - b.getLength())
                //Take the shortest
                .findFirst()
                //Or none found
                .orElse(null);
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

    public class TravelCosts {

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

        public Integer getCost(String a, String b) {
            return costs.get(a).getOrDefault(b, Integer.MAX_VALUE);
        }
        
        public boolean canTravel(String a, String b) {
            return costs.get(a).containsKey(b);
        }
    }
}
