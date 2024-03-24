package model;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Elevator {
    private static final int NO_TARGET_FLOOR = -1;
    private static final int START_FLOOR = 0;
    private final UUID id;
    private int currentFloor;
    private int targetFloor;

    public Elevator() {
        id = UUID.randomUUID();
        currentFloor = START_FLOOR;
        targetFloor = NO_TARGET_FLOOR;
    }

    public UUID getId() {
        return id;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public int getTargetFloor() {
        return targetFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public void setTargetFloor(int targetFloor) {
        this.targetFloor = targetFloor;
    }
    public static Optional<Elevator> getElevatorById(UUID elevatorId, List<Elevator> elevators) {
        return elevators.stream()
                .filter(elevator -> elevator.getId().equals(elevatorId))
                .findAny();
    }

}
