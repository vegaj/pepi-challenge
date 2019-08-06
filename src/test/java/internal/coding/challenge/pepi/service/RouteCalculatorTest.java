/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internal.coding.challenge.pepi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import internal.coding.challenge.pepi.domain.Roadmap;
import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jose
 */
public class RouteCalculatorTest {

    public RouteCalculatorTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of calculateOptimalRoute method, of class RouteCalculator.
     */
    @Test
    public void testCostsGridIsNavigableBothWays() throws IOException {
        System.out.println("Costs is navigable in both ways");
        RoadmapParser parser = new RoadmapParser(new ObjectMapper());
        Roadmap roadmap = parser.parse(exercise1());

        RouteCalculator rc = new RouteCalculator();
        rc.travels = rc.new TravelCosts(roadmap);

        assertTrue(roadmap.getCities().stream()
                .allMatch(c1 -> {
                    return roadmap.getCities().stream().allMatch(c2 -> rc.travels.getTravelCost(c1.getName(), c2.getName()).equals(rc.travels.getTravelCost(c2.getName(), c1.getName())));
                }));
    }

    private File exercise1() {
        return getFileInClasspath("input/exercise1.json");
    }

    private File getFileInClasspath(String filePath) {
        return new File(getClass().getClassLoader().getResource(filePath).getFile());
    }
}
