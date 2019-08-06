/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internal.coding.challenge.pepi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import internal.coding.challenge.pepi.domain.Route;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import org.junit.Test;

/**
 *
 * @author jose
 */
public class RouteCalculatorTest {

    private RouteCalculator routeCalculator = new RouteCalculator();

    private RoadmapParser roadmapParser = new RoadmapParser(new ObjectMapper());

    public RouteCalculatorTest() {
    }

    /**
     * Test of calculateOptimalRoute method, of class RouteCalculator.
     */
    @Test
    public void test_highest_score_is_zero() throws IOException {
        Route solution = routeCalculator.calculateOptimalRoute(roadmapParser.parse(zeroScore()), 7).get();

        assertThat(solution.getCities()).contains("Base", "Dest").endsWith("Base");

        assertThat(solution.getTotalCost()).isEqualTo(10);
        assertThat(solution.getTotalReward()).isEqualTo(5);
        assertThat(solution.getTotalScore()).isEqualTo(-5);
    }

    @Test(expected = NoSuchElementException.class)
    public void test_no_solution() throws IOException {
        routeCalculator.calculateOptimalRoute(roadmapParser.parse(zeroScore()), 1).get();
        fail("it was expected not to find a solution");
    }

    @Test
    public void test_simple_triangle_small_route() throws IOException {
        Route solution = routeCalculator.calculateOptimalRoute(roadmapParser.parse(simpleTriangle()), 3).get();
        assertThat(solution.getCities()).contains("A", "B").endsWith("A");
        assertThat(solution.getTotalCost()).isEqualTo(3);
        assertThat(solution.getTotalReward()).isEqualTo(4);
        assertThat(solution.getTotalScore()).isEqualTo(1);
    }

    @Test
    public void test_simple_triangle_medium_route() throws IOException {
        Route solution = routeCalculator.calculateOptimalRoute(roadmapParser.parse(simpleTriangle()), 4).get();
        assertThat(solution.getCities()).isIn(
                List.of("A", "B", "D", "C", "A"),
                List.of("A", "C", "D", "B", "A")
        );
        assertThat(solution.getTotalCost()).isEqualTo(4);
        assertThat(solution.getTotalReward()).isEqualTo(6);
        assertThat(solution.getTotalScore()).isEqualTo(2);
    }

    @Test
    public void test_simple_triangle_large_route() throws IOException {
        Route solution = routeCalculator.calculateOptimalRoute(roadmapParser.parse(simpleTriangle()), 5).get();
        assertThat(solution.getCities()).isIn(
                List.of("A", "B", "D", "E", "C", "A"),
                List.of("A", "C", "E", "D", "B", "A")
        );
        assertThat(solution.getTotalCost()).isEqualTo(5);
        assertThat(solution.getTotalReward()).isEqualTo(8);
        assertThat(solution.getTotalScore()).isEqualTo(3);
    }

    @Test
    public void test_steps_over_A_multiple_times() throws IOException {
        Route solution = routeCalculator.calculateOptimalRoute(roadmapParser.parse(getStar()), 6).get();
        assertThat(solution.getCities()).isIn(
                List.of("X", "A", "B", "A", "C", "A", "X"),
                List.of("X", "A", "C", "A", "B", "A", "X")
        );
        
        assertThat(solution.getTotalCost()).isEqualTo(6);
        assertThat(solution.getTotalReward()).isEqualTo(30);
        assertThat(solution.getTotalScore()).isEqualTo(24);
    }

    private File getStar() {
        return getFileInClasspath("input/star.json");
    }

    private File zeroScore() {
        return getFileInClasspath("input/zero_score.json");
    }

    private File simpleTriangle() {
        return getFileInClasspath("input/simple_triangle.json");
    }

    private File exercise1() {
        return getFileInClasspath("input/exercise1.json");
    }

    private File getFileInClasspath(String filePath) {
        return new File(getClass().getClassLoader().getResource(filePath).getFile());
    }
}
