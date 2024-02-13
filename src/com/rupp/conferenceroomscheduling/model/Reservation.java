package com.rupp.conferenceroomscheduling.model;

import com.rupp.conferenceroomscheduling.util.Colors;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Reservation {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String meetingName;
    private String organizer;
    private int attendees;

    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Reservation(String startTime, String endTime, String meetingName, String organizer, int attendees) {

        try {
            LocalDateTime parsedStartTime = LocalDateTime.parse(startTime, dateTimeFormat);
            LocalDateTime parsedEndTime = LocalDateTime.parse(endTime, dateTimeFormat);


            LocalDateTime now = LocalDateTime.now();
            if (parsedStartTime.isBefore(now)) {
                throw new IllegalArgumentException(Colors.ANSI_YELLOW + "ⓘ Start time cannot be in the past."+ Colors.ANSI_RESET);
            }
            if (parsedEndTime.isBefore(now)) {
                throw new IllegalArgumentException(Colors.ANSI_YELLOW + "ⓘ End time cannot be in the past."+ Colors.ANSI_RESET);
            }
            if (parsedEndTime.isBefore(parsedStartTime)) {
                throw new IllegalArgumentException(Colors.ANSI_YELLOW + "ⓘ End time must be after the start time."+ Colors.ANSI_RESET);
            }

            this.startTime = parsedStartTime;
            this.endTime = parsedEndTime;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(Colors.ANSI_YELLOW + "ⓘ Invalid date and time format. Please use 'yyyy-MM-dd HH:mm'."+ Colors.ANSI_RESET, e);
        }


        this.attendees = attendees;
        this.meetingName = meetingName;
        this.organizer = organizer;

    }


    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public int getAttendees() {
        return attendees;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public String getOrganizer() {
        return organizer;
    }

    public boolean overlapsWith(Reservation other) {
        return !(this.endTime.isBefore(other.startTime) || this.startTime.isAfter(other.endTime));
    }

    public String getDurationFormatted() {
        long durationInMinutes = java.time.Duration.between(startTime, endTime).toMinutes();
        long hours = durationInMinutes / 60;
        long minutes = durationInMinutes % 60;
        return String.format("%d:%02d hours", hours, minutes);
    }
}
