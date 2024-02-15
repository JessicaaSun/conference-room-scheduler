package com.rupp.conferenceroomscheduling.service;

import com.rupp.conferenceroomscheduling.model.ConferenceRoom;
import com.rupp.conferenceroomscheduling.model.Reservation;
import com.rupp.conferenceroomscheduling.util.Colors;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ConferenceScheduler {
    private List<ConferenceRoom> conferenceRooms;

    public ConferenceScheduler() {
        conferenceRooms = new ArrayList<>();
    }

    public void addConferenceRoom(ConferenceRoom room) {
        conferenceRooms.add(room);
    }

    public boolean bookConferenceRoom(String roomName, Reservation reservation) {
        for (ConferenceRoom room : conferenceRooms) {
            if (room.getRoomName().equals(roomName)) {
                return room.bookRoom(reservation);
            }
        }
        System.out.println(Colors.ANSI_RED + "ⓘ Conference room not found."+ Colors.ANSI_RESET);
        return false;
    }

    public ConferenceRoom getRoomByName(String roomName) {
        for (ConferenceRoom room : conferenceRooms) {
            if (room.getRoomName().equalsIgnoreCase(roomName)) {
                return room;
            }
        }
        return null;
    }


    public boolean cancelReservation(String roomName, String meetingName, String organizer) {
        for (ConferenceRoom room : conferenceRooms) {
            if (room.getRoomName().equals(roomName) && room.cancelReservation(meetingName, organizer)) {
                System.out.println(Colors.ANSI_GREEN + "✓ " + roomName + " with meeting name '" + meetingName+ "'"+" is cancelled successfully."+Colors.ANSI_RESET);
                return true;
            }
        }
        System.out.println(Colors.ANSI_RED+"ⓘ Conference room or reservation not found."+ Colors.ANSI_RESET);
        return false;
    }

    public void displayConferenceRooms() {
        System.out.println("Available Conference Rooms:");
        conferenceRooms.forEach(room -> System.out.println(room.getRoomName() + " - Capacity: " + room.getCapacity()));
    }

    public void displayReservations(String roomName) {
        conferenceRooms.stream()
                .filter(room -> room.getRoomName().equals(roomName))
                .findFirst()
                .ifPresentOrElse(
                        room -> {
                            System.out.println(Colors.ANSI_PURPLE_BOLD + "Reservations for " + roomName + ":" + Colors.ANSI_RESET);
                            System.out.printf("+-----------------+----------------+------------------+-------------------+----------------+------------------+%n");
                            System.out.printf("| %-15s | %-14s | %-17s | %-17s | %-14s | %-15s |%n", "Meeting Name", "Organizer", "Start Date", "End Date", "Duration", "Attendees");
                            System.out.printf("+-----------------+----------------+----------------+-------------------+----------------+------------------+%n");

                            room.getReservations().forEach(reservation -> {
                                System.out.printf("| %-15s | %-14s | %-17s | %-17s | %-14s | %-15d |%n",
                                        reservation.getMeetingName(),
                                        reservation.getOrganizer(),
                                        reservation.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                                        reservation.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                                        reservation.getDurationFormatted(),
                                        reservation.getAttendees());
                            });

                            System.out.printf("+-----------------+----------------+------------------+-------------------+----------------+-----------------+%n");
                        },
                        () -> System.out.println(Colors.ANSI_RED+"ⓘ Conference room not found."+Colors.ANSI_RESET)
                );
    }
}
