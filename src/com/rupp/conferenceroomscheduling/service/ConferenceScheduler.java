package com.rupp.conferenceroomscheduling.service;

import com.rupp.conferenceroomscheduling.model.ConferenceRoom;
import com.rupp.conferenceroomscheduling.model.Reservation;
import com.rupp.conferenceroomscheduling.util.Colors;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

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
                            Table table = new Table(6, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
                            table.setColumnWidth(0,10,20);
                            table.setColumnWidth(1,10,20);
                            table.setColumnWidth(2,10,20);
                            table.setColumnWidth(3,10,20);
                            table.setColumnWidth(4,10,20);
                            table.addCell("Meeting Name");
                            table.addCell("Organizer");
                            table.addCell("Start Date");
                            table.addCell("End Date");
                            table.addCell("Duration");
                            table.addCell("Attendee");

                            // Add reservation details to the table
                            room.getReservations().forEach(reservation -> {
                                table.addCell(reservation.getMeetingName());
                                table.addCell(reservation.getOrganizer());
                                table.addCell(reservation.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                                table.addCell(reservation.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                                table.addCell(reservation.getDurationFormatted());
                                String getAddt = String.valueOf(reservation.getAttendees());
                                table.addCell(getAddt);
                            });
                            // Render the table after adding reservation details
                            System.out.println(table.render());

                        },
                        () -> System.out.println(Colors.ANSI_RED+"ⓘ Conference room not found."+Colors.ANSI_RESET)
                );

    }

}
