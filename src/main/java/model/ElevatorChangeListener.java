package model;

@FunctionalInterface
public interface ElevatorChangeListener {
    void elevatorChange(Elevator elevator, String message);
}
