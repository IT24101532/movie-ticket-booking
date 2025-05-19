package com.movieticket.movie_ticket_booking.service;

import com.movieticket.movie_ticket_booking.model.Activity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ActivityService {
    private static final String ACTIVITY_FILE = "data/activities.txt";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ActivityService() {
        try {
            Files.createDirectories(Paths.get(ACTIVITY_FILE).getParent());
            if (!Files.exists(Paths.get(ACTIVITY_FILE))) {
                Files.createFile(Paths.get(ACTIVITY_FILE));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logActivity(String action) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(ACTIVITY_FILE),
                StandardOpenOption.APPEND)) {
            writer.write(LocalDateTime.now().format(DATE_TIME_FORMATTER) + "|" + action);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Activity> getRecentActivities(int limit) {
        List<Activity> activities = new ArrayList<>();

        try {
            if (!Files.exists(Paths.get(ACTIVITY_FILE))) {
                return activities;
            }

            List<String> lines = Files.readAllLines(Paths.get(ACTIVITY_FILE));
            Collections.reverse(lines); // Most recent first

            int count = 0;
            for (String line : lines) {
                if (count >= limit) break;

                String[] parts = line.split("\\|", 2);
                if (parts.length == 2) {
                    Activity activity = new Activity();
                    activity.setTimestamp(LocalDateTime.parse(parts[0], DATE_TIME_FORMATTER));
                    activity.setAction(parts[1]);
                    activities.add(activity);
                    count++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return activities;
    }
}
