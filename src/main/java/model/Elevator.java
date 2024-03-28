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

    public List<Integer> getButtonsPressed() {
        return buttonsPressed;
    }

    public void addFloorToButtonList(int buttonFloor) {
        buttonsPressed.add(buttonFloor);
    }

    public void deleteLastFloorInButtonList() {
        buttonsPressed.remove(0);
    }


    public synchronized void arrived() {

        // we have arrived
        targetCall.get().setPickedUp(true);
        targetCall = futureCall.isEmpty() ? null : Optional.ofNullable(futureCall.get(0));

        elevatorChanged("Arrived to " + currentFloor);
    }

    public synchronized void move() {
        if (targetCall.get().getPressingButtonFloor() > currentFloor) {
            currentFloor += 1;
            elevatorChanged("Going up from:" + (currentFloor - 1) + " to " + currentFloor);

        } else if (targetCall.get().getPressingButtonFloor() < currentFloor) {
            currentFloor -= 1;
            elevatorChanged("Going down from:" + (currentFloor - 1) + " to " + currentFloor);
        }

    }

    public static Optional<Elevator> getElevatorById(UUID elevatorId, List<Elevator> elevators) {
        return elevators.stream()
                .filter(elevator -> elevator.getId().equals(elevatorId))
                .findAny();
    }


    public static synchronized Elevator findNeartestFreeElevation(List<Elevator> elevators, Call call) {
        /*
            This function finds the nearest lift for people who have pressed the lift call button

     */
        int minimumDistance = Integer.MAX_VALUE;
        int actDistance;
        Elevator nearestElevator = null;

        for (Elevator elevator : elevators) {
            actDistance = abs(call.getPressingButtonFloor() - elevator.getCurrentFloor());

            if (!elevator.isOccupied() || isSameDirectionAndAlongWay(elevator, call.getPressingButtonFloor(), call.getDirection())) {
                if (actDistance < minimumDistance) {
                    minimumDistance = actDistance;
                    nearestElevator = elevator;
                }
            }
        }
        return nearestElevator;
    }


    private static boolean isSameDirectionAndAlongWay(Elevator elevator, int floor, ElevatorDirection direction) {
        /*
            I check whether, when the lift is currently going up/down,
            it will just have me in the way so that it can stop and I can get in.
     */

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
