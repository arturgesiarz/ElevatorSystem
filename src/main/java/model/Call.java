package model;

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

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public void setDirection(ElevatorDirection direction) {
        this.direction = direction;
    }

    public Elevator getSelectedElevator() {
        return selectedElevator;
    }

    public void setSelectedElevator(Elevator selectedElevator) {
        this.selectedElevator = selectedElevator;
    }
}
