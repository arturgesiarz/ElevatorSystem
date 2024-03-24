package model;

public class Call {
    private int floor;
    private ElevatorDirection direction;

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
}
