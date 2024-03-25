package model;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.Math.abs;
import static model.ElevatorDirection.*;

public class Elevator {
    public static final int NO_TARGET_FLOOR = -1;
    private static final int START_FLOOR = 0;
    private final UUID id;
    private int currentFloor;
    private int targetFloor;
    private List<Call> futureTargets;  // future lift calls
    private ElevatorDirection direction;
    private boolean isOccupied;

    public Elevator() {
        id = UUID.randomUUID();
        currentFloor = START_FLOOR;
        targetFloor = NO_TARGET_FLOOR;
        isOccupied = false;
        direction = UNSELECTED;
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

    public boolean isOccupied() {
        return isOccupied;
    }

    public ElevatorDirection getDirection() {
        return direction;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public void setTargetFloor(int targetFloor) {
        this.targetFloor = targetFloor;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public static Optional<Elevator> getElevatorById(UUID elevatorId, List<Elevator> elevators) {
        return elevators.stream()
                .filter(elevator -> elevator.getId().equals(elevatorId))
                .findAny();
    }
    public static Elevator findNeartestFreeElevation(List<Elevator> elevators, int floor, ElevatorDirection direction) {
        int minimumDistance = Integer.MAX_VALUE;
        int actDistance;
        Elevator nearestElevator = null;

        for (Elevator elevator : elevators) {
            actDistance = abs(floor - elevator.getCurrentFloor());

            if (!elevator.isOccupied() || isSameDirectionAndAlongWay(elevator, floor, direction)) {
                if (actDistance < minimumDistance) {
                    minimumDistance = actDistance;
                    nearestElevator = elevator;
                }
            }
        }

        return nearestElevator;
    }

    private static boolean isSameDirectionAndAlongWay(Elevator elevator, int floor, ElevatorDirection direction) {
        if (elevator.getDirection().equals(direction) && direction.equals(UP) && floor >= elevator.getCurrentFloor()) {
            return true;

        } else if (elevator.getDirection().equals(direction) && direction.equals(DOWN) && floor <= elevator.getCurrentFloor()) {
            return true;

        } return false;
    }

}
