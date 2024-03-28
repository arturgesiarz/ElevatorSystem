package model;

import java.util.Optional;

public class Call {
    private final int pressingButtonFloor;
    private int finalFloor;
    private boolean isPickedUp;  // whether a lift has come
    private final ElevatorDirection direction;
    private Elevator selectedElevator;

    public Call(int pressingButtonFloor, ElevatorDirection direction) {
        this.pressingButtonFloor = pressingButtonFloor;
        this.direction = direction;
        this.isPickedUp = false;
    }
    public int getPressingButtonFloor() {
        return pressingButtonFloor;
    }

    public ElevatorDirection getDirection() {
        return direction;
    }
    public Elevator getSelectedElevator() {
        return selectedElevator;
    }
    public synchronized boolean isPickedUp() {
        return isPickedUp;
    }
    public int getFinalFloor() {
        return finalFloor;
    }

    public synchronized void setSelectedElevator(Elevator selectedElevator) {
        this.selectedElevator = selectedElevator;

        if (selectedElevator.isOccupied()) {
            Call oldTargetCall = selectedElevator.getTargetCall().get();
            selectedElevator.setTargetCall(Optional.of(this));
            selectedElevator.getFutureCall().add(0, oldTargetCall);
        } else {
            selectedElevator.setTargetCall(Optional.of(this));
        }
    }
    public synchronized void setPickedUp(boolean pickedUp) {
        isPickedUp = pickedUp;
    }

    public void setFinalFloor(int finalFloor) {
        this.finalFloor = finalFloor;
    }
}
