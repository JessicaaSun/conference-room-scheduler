import com.rupp.conferenceroomscheduling.model.ConferenceRoom;
import com.rupp.conferenceroomscheduling.model.Reservation;
import com.rupp.conferenceroomscheduling.service.ConferenceScheduler;
import com.rupp.conferenceroomscheduling.util.Colors;

import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ConferenceScheduler scheduler = new ConferenceScheduler();

    public static void main(String[] args) {

        String display = """
                
                
                ███████╗ ██████╗          ██╗ █████╗ ██╗   ██╗ █████╗\s
                ██╔════╝██╔════╝          ██║██╔══██╗██║   ██║██╔══██╗
                █████╗  ███████╗          ██║███████║██║   ██║███████║
                ██╔══╝  ██╔═══██╗    ██   ██║██╔══██║╚██╗ ██╔╝██╔══██║
                ███████╗╚██████╔╝    ╚█████╔╝██║  ██║ ╚████╔╝ ██║  ██║
                ╚══════╝ ╚═════╝      ╚════╝ ╚═╝  ╚═╝  ╚═══╝  ╚═╝  ╚═╝\s
                """;
        System.out.println(Colors.ANSI_PURPLE_BOLD + display + Colors.ANSI_RESET);

        initializeConferenceRooms();

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("+" + "-".repeat(39) + "+");
            System.out.println(Colors.ANSI_PURPLE_BOLD+"|       Conference Room Scheduler       |"+Colors.ANSI_RESET);
            System.out.println("+" + "-".repeat(39) + "+");
            System.out.println("| 1. Book a conference room             |");
            System.out.println("| 2. Cancel a reservation               |");
            System.out.println("| 3. Display available conference rooms |");
            System.out.println("| 4. Display reservations for a room    |");
            System.out.println("| 5. Exit                               |");
            System.out.println("+" + "-".repeat(39) + "+");
            System.out.print(Colors.ANSI_GREEN + "➜ Choose an option: " + Colors.ANSI_RESET);

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> bookConferenceRoom();
                case 2 -> cancelReservation();
                case 3 -> scheduler.displayConferenceRooms();
                case 4 -> displayReservationsForRoom();
                case 5 -> running = false;
                default -> System.out.println(Colors.ANSI_RED + "ⓘ Invalid option, please try again."+ Colors.ANSI_RESET);
            }
        }
        scanner.close();
    }

    private static void initializeConferenceRooms() {
        scheduler.addConferenceRoom(new ConferenceRoom("Room 1", 10));
        scheduler.addConferenceRoom(new ConferenceRoom("Room 2", 15));
        scheduler.addConferenceRoom(new ConferenceRoom("Room 3", 20));
        scheduler.addConferenceRoom(new ConferenceRoom("Room 4", 30));
    }

    private static void bookConferenceRoom() {
        System.out.println();
        scheduler.displayConferenceRooms();
        System.out.println();
        System.out.print("➜ Enter room name: ");
        String roomName = scanner.nextLine();

        ConferenceRoom room = scheduler.getRoomByName(roomName);
        if (room == null) {
            System.out.println(Colors.ANSI_RED + "ⓘ The room '" + roomName + "' does not exist. Please enter a valid room name." + Colors.ANSI_RESET);
            return;
        }

        System.out.print("➜ Enter meeting name: ");
        String meetingName = scanner.nextLine();

        System.out.print("➜ Enter organizer's name: ");
        String organizer = scanner.nextLine();

        System.out.print("➜ Enter number of attendees: ");
        int attendees = scanner.nextInt();
        scanner.nextLine();

        if (attendees > room.getCapacity()) {
            System.out.println(Colors.ANSI_RED + "ⓘ Cannot book '" + roomName + "': number of attendees (" + attendees + ") exceeds room capacity (" + room.getCapacity() + ")." + Colors.ANSI_RESET);
            return;
        } else if (attendees <= 0) {
            System.out.println(Colors.ANSI_YELLOW + "ⓘ Number of attendees must be greater than 0."+ Colors.ANSI_RESET);
            return;
        }

        Reservation reservation = null;
        while (reservation == null) {
            try {
                System.out.print("➜ Enter start time (yyyy-MM-dd HH:mm): ");
                String startTime = scanner.nextLine();

                System.out.print("➜ Enter end time (yyyy-MM-dd HH:mm): ");
                String endTime = scanner.nextLine();

                reservation = new Reservation(startTime, endTime, meetingName, organizer, attendees);
                boolean booked = scheduler.bookConferenceRoom(roomName, reservation);
                if (!booked) {

                    System.out.println(Colors.ANSI_RED + "ⓘ Failed to book the room." + Colors.ANSI_RESET);
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());

            }
        }


    }

    private static void displayReservationsForRoom() {
        System.out.println();
        scheduler.displayConferenceRooms();
        System.out.print("➜ Enter room name to display reservations: ");


        String roomName = scanner.nextLine();

        scheduler.displayReservations(roomName);
    }
    private static void cancelReservation() {
        System.out.print("➜ Enter room name: ");
        String roomName = scanner.nextLine();

        System.out.print("➜ Enter meeting name to cancel: ");
        String meetingName = scanner.nextLine();

        System.out.print("➜ Enter organizer's name: ");
        String organizer = scanner.nextLine();

        scheduler.cancelReservation(roomName, meetingName, organizer);
    }
}
