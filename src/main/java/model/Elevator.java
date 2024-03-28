package model;

import java.util.*;

import static java.lang.Math.*;
import static model.ElevatorDirection.*;

public class Elevator {
    private static final int START_FLOOR = 0;
    private final UUID id;
    private final List<ElevatorChangeListener> observers = new ArrayList<>();
    private int currentFloor;
    private final List<Integer> buttonsPressed = new ArrayList<>();
    private Optional<Call> targetCall;
    private List<Call> futureCall;

    public Elevator() {
        id = UUID.randomUUID();
        currentFloor = START_FLOOR;
        futureCall = new ArrayList<>();
    }

    public void addObserver(ElevatorChangeListener observer) {
        observers.add(observer);
    }
    public void removeObserver(ElevatorChangeListener observer) {
        observers.remove(observer);
    }

    void elevatorChanged(String message){
        observers.forEach((observer) -> observer.elevatorChange(this,message));
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

    public synchronized boolean isOccupied() {
        return targetCall != null ? true : false;
    }


    public void setTargetCall(Optional<Call> targetCall) {
        this.targetCall = targetCall;
    }


    public synchronized void arrivedToPerson() {
        targetCall.get().setPickedUp(true);
        elevatorChanged("Arrived to person " + currentFloor);

    }

    public synchronized void arrivedToDestination() {
        if (futureCall.isEmpty()) {
            targetCall = null;
        } else {
            targetCall = Optional.ofNullable(futureCall.get(0));
            futureCall.remove(0);
        }
        elevatorChanged("Arrived to " + currentFloor);
    }

    public synchronized void move() {
        if (targetCall.get().isPickedUp()) {
            if (targetCall.get().getFinalFloor() > currentFloor) {
                currentFloor += 1;
                elevatorChanged("Going up from:" + (currentFloor - 1) + " to " + currentFloor);

            } else if (targetCall.get().getFinalFloor() < currentFloor) {
                currentFloor -= 1;
                elevatorChanged("Going down from:" + (currentFloor + 1) + " to " + currentFloor);
            }

        } else {
            if (targetCall.get().getPressingButtonFloor() > currentFloor) {
                currentFloor += 1;
                elevatorChanged("Going up from:" + (currentFloor - 1) + " to " + currentFloor);

            } else if (targetCall.get().getPressingButtonFloor() < currentFloor) {
                currentFloor -= 1;
                elevatorChanged("Going down from:" + (currentFloor + 1) + " to " + currentFloor);
            }
        }

    }

    public static Elevator findNeartestFreeElevation(List<Elevator> elevators, Call call) {
        int minimumDistance = Integer.MAX_VALUE;
        int actDistance;
        boolean neareastElevatorOccupired = false;
        Elevator nearestElevator = null;


        for (Elevator elevator : elevators) {
            actDistance = abs(call.getPressingButtonFloor() - elevator.getCurrentFloor());

            if (!elevator.isOccupied()) {

                if (actDistance < minimumDistance) {
                    minimumDistance = actDistance;
                    nearestElevator = elevator;
                }

            } else if (isSameDirectionAndAlongWay(elevator,
                    call.getPressingButtonFloor(), call.getDirection())) {
                if (actDistance < minimumDistance) {
                    minimumDistance = actDistance;
                    nearestElevator = elevator;
                }
            }
        }

        return nearestElevator;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    private static boolean isSameDirectionAndAlongWay(Elevator elevator, int floor, ElevatorDirection direction) {
        if (elevator.getTargetCall().get().getDirection().equals(direction) && direction.equals(UP)
                && floor >= elevator.getCurrentFloor() && floor <= elevator.getTargetCall().get().getFinalFloor()) {
            return true;

        } else if (elevator.getTargetCall().get().getDirection().equals(direction) && direction.equals(DOWN)
                && floor <= elevator.getCurrentFloor() && floor >= elevator.getTargetCall().get().getFinalFloor()) {
            return true;

        } return false;
    }

    @Override
    public String toString() {
        return "Elevator - " + id + ".\n" +
                "It is on floor - " + currentFloor + ".";
    }
}
