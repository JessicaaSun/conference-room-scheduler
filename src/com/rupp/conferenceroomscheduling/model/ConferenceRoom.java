package com.rupp.conferenceroomscheduling.model;

import com.rupp.conferenceroomscheduling.util.Colors;

import java.util.ArrayList;
import java.util.List;

public class ConferenceRoom {
    private String roomName;
    private int capacity;
    private List<Reservation> reservations;

    public ConferenceRoom(String roomName, int capacity) {
        this.roomName = roomName;
        this.capacity = capacity;
        this.reservations = new ArrayList<>();
    }

    public boolean bookRoom(Reservation reservation) {

        for (Reservation existingReservation : reservations) {
            if (existingReservation.overlapsWith(reservation)) {
                System.out.println(Colors.ANSI_RED + "ⓘ Room " + roomName + " is already booked for the given time."+ Colors.ANSI_RESET);
                return false;
            }
        }
        if (reservation.getAttendees() > this.capacity) {
            System.out.println(Colors.ANSI_RED + "ⓘ Cannot book " + roomName + ": number of attendees (" + reservation.getAttendees() + ") exceeds room capacity (" + capacity + ")." + Colors.ANSI_RESET);
            return false;
        }
        reservations.add(reservation);
        System.out.println(Colors.ANSI_GREEN +"✓ Room " + roomName + " booked successfully for " + reservation.getMeetingName() + "."+ Colors.ANSI_RESET);
        return true;
    }

    public boolean cancelReservation(String meetingName, String organizer) {
        return reservations.removeIf(reservation ->
                reservation.getMeetingName().equals(meetingName) && reservation.getOrganizer().equals(organizer));
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public String getRoomName() {
        return roomName;
    }

    public int getCapacity() {
        return capacity;
    }
}
