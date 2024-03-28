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

    public static synchronized Elevator findNeartestFreeElevation(List<Elevator> elevators, Call call) {
        int minimumDistance = Integer.MAX_VALUE;
        int actDistance;
        boolean isChangedElevatorWay;
        boolean neareastElevatorOccupired = false;
        Elevator nearestElevator = null;


        for (Elevator elevator : elevators) {
            actDistance = abs(call.getPressingButtonFloor() - elevator.getCurrentFloor());

            if (!elevator.isOccupied() || isSameDirectionAndAlongWay(elevator,
                    call.getPressingButtonFloor(), call.getDirection())) {

                isChangedElevatorWay = isSameDirectionAndAlongWay(elevator,
                        call.getPressingButtonFloor(), call.getDirection());

                if (actDistance < minimumDistance) {
                    minimumDistance = actDistance;
                    nearestElevator = elevator;

                    if (isChangedElevatorWay) {
                        neareastElevatorOccupired = true;
                    }
                }
            }
        }

        // 1.
        // A -> 0 - 10, B -> 4 - 11
        // A' -> 0 - 4, B' -> 4 - 10, C' -> 10 - 11

        // 2.
        // A -> 0 - 10, B -> 4 -> 9
        // A' -> 0 - 4, B' -> 4 - 9, C' -> 9 - 10

        // 3.
        // A -> 0 - 10, B -> 10 - 11
        // A' -> 0 - 10, B' -> 10 - 11

        // 3.
        // A -> 11 - 4, B -> 9 - 0
        // A' -> 11 - 9, B' -> 9 - 4, C' -> 4 - 0

        // 4.
        // A -> 11 - 4, B -> 10 - 3
        // A' -> 11 - 10, B' -> 10 - 4, C' -> 4 - 3

        if (neareastElevatorOccupired) {
            if (call.getDirection().equals(UP)) {
                if (nearestElevator.targetCall.get().getFinalFloor() != call.getPressingButtonFloor() &&
                        nearestElevator.targetCall.get().getFinalFloor() != call.getFinalFloor()) {

                    Call call1 = new Call(nearestElevator.targetCall.get().getPressingButtonFloor(),
                            call.getPressingButtonFloor(),
                            UP,
                            nearestElevator,
                            true);

                    Call call2 = new Call(call.getPressingButtonFloor(),
                            min(call.getFinalFloor(),nearestElevator.targetCall.get().getFinalFloor()),
                            UP,
                            nearestElevator,
                            false);

                    Call call3 = new Call(min(call.getFinalFloor(),nearestElevator.targetCall.get().getFinalFloor()),
                            max(call.getFinalFloor(),nearestElevator.targetCall.get().getFinalFloor()),
                            UP,
                            nearestElevator,
                            false);

                } else if (nearestElevator.targetCall.get().getFinalFloor() != call.getPressingButtonFloor() &&
                            nearestElevator.targetCall.get().getFinalFloor() == call.getFinalFloor()){

                    Call call1 = new Call(nearestElevator.targetCall.get().getPressingButtonFloor(),
                            call.getPressingButtonFloor(),
                            call.getDirection(),
                            nearestElevator,
                            true);

                    Call call2 = new Call(call.getPressingButtonFloor(),
                            call.getFinalFloor(),
                            call.getDirection(),
                            nearestElevator,
                            false);
                }
            } else {
                if (nearestElevator.targetCall.get().getFinalFloor() != call.getPressingButtonFloor()) {  // case A -> 11 - 4, B -> 4 - 0
                    Call call1;
                    Call call2;
                    Call call3;

                }
            }
        }
        return nearestElevator;

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
