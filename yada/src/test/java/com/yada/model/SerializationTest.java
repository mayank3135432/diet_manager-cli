package com.yada.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SerializationTest {

    @Test
    public void testSerializeAndDeserializeUserProfile() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        UserProfile user = new UserProfile(
            1,
            "John Doe",
            "Male",
            180,
            30,
            "Active",
            Arrays.asList(new WeightHistory(LocalDate.now(), 75.0)),
            new CalorieGoal("BMR", 2000)
        );

        String json = mapper.writeValueAsString(user);
        UserProfile deserializedUser = mapper.readValue(json, UserProfile.class);

        assertEquals(user.getName(), deserializedUser.getName());
        assertEquals(user.getWeightHistory().get(0).getDate(), deserializedUser.getWeightHistory().get(0).getDate());
    }

    @Test
    public void testSerializeAndDeserializeDailyLog() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        DailyLog log = new DailyLog(1, LocalDate.now());
        log.addEntry(new LogEntry(1, 2));

        String json = mapper.writeValueAsString(log);
        DailyLog deserializedLog = mapper.readValue(json, DailyLog.class);

        assertEquals(log.getDate(), deserializedLog.getDate());
        assertEquals(log.getEntries().size(), deserializedLog.getEntries().size());
    }
}
