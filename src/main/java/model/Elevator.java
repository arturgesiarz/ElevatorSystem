package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.Math.abs;
import static model.ElevatorDirection.*;

public class Elevator {
    private static final int START_FLOOR = 0;
    private final UUID id;
    private int currentFloor;
    private Optional<Call> targetCall;
    private List<Call> futureCall;

    public Elevator() {
        id = UUID.randomUUID();
        currentFloor = START_FLOOR;
        futureCall = new ArrayList<Call>();
    }

    public UUID getId() {
        return id;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public Optional<Call> getTargetCall() {
        return targetCall;
    }

    public List<Call> getFutureCall() {
        return futureCall;
    }

    public boolean isOccupied() {
        return targetCall != null ? true : false;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public void setTargetCall(Optional<Call> targetCall) {
        this.targetCall = targetCall;
    }

    public void arrived() {
        targetCall = Optional.ofNullable(futureCall.get(0));
    }

    public void move() {
        if (targetCall.get().getDirection().equals(UP)) {
            currentFloor += 1;
        } else {
            currentFloor -= 1;
        }
    }

    public static Optional<Elevator> getElevatorById(UUID elevatorId, List<Elevator> elevators) {
        return elevators.stream()
                .filter(elevator -> elevator.getId().equals(elevatorId))
                .findAny();
    }

    public static Elevator findNeartestFreeElevation(List<Elevator> elevators, Call call) {
        int minimumDistance = Integer.MAX_VALUE;
        int actDistance;
        Elevator nearestElevator = null;

        for (Elevator elevator : elevators) {
            actDistance = abs(call.getFloor() - elevator.getCurrentFloor());

            if (!elevator.isOccupied() || isSameDirectionAndAlongWay(elevator, call.getFloor(), call.getDirection())) {
                if (actDistance < minimumDistance) {
                    minimumDistance = actDistance;
                    nearestElevator = elevator;
                }
            }
        }
        return nearestElevator;
    }

    private static boolean isSameDirectionAndAlongWay(Elevator elevator, int floor, ElevatorDirection direction) {
        if (elevator.getTargetCall().get().getDirection().equals(direction) && direction.equals(UP) && floor >= elevator.getCurrentFloor()) {
            return true;

        } else if (elevator.getTargetCall().get().getDirection().equals(direction) && direction.equals(DOWN) && floor <= elevator.getCurrentFloor()) {
            return true;

        } return false;
    }

}
