package model;

import java.util.Optional;

public class Call {
    private int floor;
    private ElevatorDirection direction;
    private Elevator selectedElevator;

    public Call(int floor, ElevatorDirection direction) {
        this.floor = floor;
        this.direction = direction;
    }
    public int getFloor() {
        return floor;
    }

    public ElevatorDirection getDirection() {
        return direction;
    }

    public Elevator getSelectedElevator() {
        return selectedElevator;
    }
    public void setSelectedElevator(Elevator selectedElevator) {
        this.selectedElevator = selectedElevator;

        if (selectedElevator.isOccupied()) {
            Call oldTargetCall = selectedElevator.getTargetCall().get();
            selectedElevator.setTargetCall(Optional.of(this));
            selectedElevator.getFutureCall().add(0, oldTargetCall);
        } else {
            selectedElevator.setTargetCall(Optional.of(this));
        }
    }
}
