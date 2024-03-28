package model;

public class ConsoleElevatorDisplay implements ElevatorChangeListener {
    @Override
    public void elevatorChange(Elevator elevator, String message) {
        System.out.println("Elevator ID: " + elevator.getId());
        System.out.println(message);
    }
}
