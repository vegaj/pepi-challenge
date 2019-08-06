package internal.coding.challenge.pepi;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import internal.coding.challenge.pepi.domain.Route;
import internal.coding.challenge.pepi.service.RoadmapParser;
import internal.coding.challenge.pepi.service.RouteCalculator;

public class AppTest {

    private RouteCalculator routeCalculator = new RouteCalculator();

    private RoadmapParser roadmapParser = new RoadmapParser(new ObjectMapper());

    @Test
    public void test_givenMaxLength5_whenCalculate_thenOptimalRouteIsFound() throws IOException {
        Route solution = routeCalculator.calculateOptimalRoute(roadmapParser.parse(exercise1()), 5);

        assertThat(solution.getCities()).isIn(
                List.of("Madrid", "Rome", "Berlin", "Paris", "London", "Madrid"),
                List.of("Madrid", "London", "Paris", "Berlin", "Rome", "Madrid"));

        assertThat(solution.getTotalCost()).isEqualTo(55);
        assertThat(solution.getTotalReward()).isEqualTo(152);
        assertThat(solution.getTotalScore()).isEqualTo(97);
    }

    @Test
    public void test_givenMaxLength8_whenCalculate_thenOptimalRouteIsFound() throws IOException {
        Route solution = routeCalculator.calculateOptimalRoute(roadmapParser.parse(exercise1()), 8);

        assertThat(solution.getCities()).isIn(
                List.of("Madrid", "Rome", "Berlin", "Warsaw", "Kyiv", "Moscow", "London", "Paris", "Madrid"),
                List.of("Madrid", "Paris", "London", "Moscow", "Kyiv", "Warsaw", "Berlin", "Rome", "Madrid"));

        assertThat(solution.getTotalCost()).isEqualTo(108);
        assertThat(solution.getTotalReward()).isEqualTo(233);
        assertThat(solution.getTotalScore()).isEqualTo(125);
    }

    private File exercise1() {
        return getFileInClasspath("input/exercise1.json");
    }

    private File getFileInClasspath(String filePath) {
        return new File(getClass().getClassLoader().getResource(filePath).getFile());
    }
}
