package internal.coding.challenge.pepi;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import internal.coding.challenge.pepi.domain.Route;
import internal.coding.challenge.pepi.service.RoadmapParser;
import internal.coding.challenge.pepi.service.RouteCalculator;

public class Main {

    private static Logger LOG = Logger.getLogger("Main");

    public static void main(String... args) {

        try {

            if (args.length < 2) {
                throw new IllegalArgumentException("Arguments expected: <input file> <maxDuration>");
            }

            File roadmapFile = new File(args[0]);
            Integer maxDuration = Integer.parseInt(args[1]);

            Route route = getRouteCalculator().calculateOptimalRoute(
                            getRoadmapParser().parse(roadmapFile), maxDuration);

            LOG.info(() -> String.format("Score: %s (%s - %s). Route: %s",
                    route.getTotalScore(), route.getTotalReward(), route.getTotalCost(), route.getCities()));

        } catch (IOException ex) {
            LOG.severe(() -> String.format("ERROR reading input file: [%s] %s", ex.getClass().getSimpleName(), ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            LOG.severe(() -> String.format("ERROR validating input data: %s", ex.getMessage()));
        } catch (Exception ex) {
            LOG.severe(() -> String.format("ERROR unexpected: [%s] %s", ex.getClass().getSimpleName(), ex.getMessage()));
        }
    }

    private static RoadmapParser getRoadmapParser() {
        return new RoadmapParser(new ObjectMapper().disable(FAIL_ON_UNKNOWN_PROPERTIES));
    }

    private static RouteCalculator getRouteCalculator() {
        return new RouteCalculator();
    }
}
