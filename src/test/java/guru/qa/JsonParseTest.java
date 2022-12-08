package guru.qa;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.model.Planner;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonParseTest {
    ClassLoader cl = ZipParseTest.class.getClassLoader();
    ObjectMapper objectMapper = new ObjectMapper();

    String jsonFileName = "planner.json";

    @Test
    void parseJson() throws Exception {
        try (InputStream is = cl.getResourceAsStream(jsonFileName)
        ) {
            Planner planner = objectMapper.readValue(is, Planner.class);

            assertThat(planner.todaysDate.dayOfWeek).isEqualTo("Wednesday");
            assertThat(planner.todaysDate.month).isEqualTo("December");
            assertThat(planner.todaysActivities.work).containsAll(Arrays.asList("code review", "create 3 test-cases"));
            assertThat(planner.todaysActivities.fitness).isEmpty();
            assertThat(planner.isAHoliday).isFalse();
            assertThat(planner.numberOfPlannedActivities).isEqualTo(6);
        }
    }
}
