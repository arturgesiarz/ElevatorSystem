package model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.IntStream;


public class ElevatorSystem {
    private static final int MAX_ELEVATORS = 16;

    private List<Elevator> elevators = new ArrayList<>();
    private PriorityQueue<Call> calls;

    public ElevatorSystem () {
        this(MAX_ELEVATORS);
    }
    public ElevatorSystem (int amountOfElevators){
        IntStream.range(0, amountOfElevators)
                .mapToObj(i -> new Elevator())
                .forEach(elevator -> {
                    elevators.add(elevator);
                    elevator.addObserver(new ConsoleElevatorDisplay());
                });
    }

    public void pickup(Call call) {
        Elevator nearestElevator = Elevator.findNeartestFreeElevation(elevators, call);

        while (nearestElevator == null) {
            nearestElevator = Elevator.findNeartestFreeElevation(elevators, call);
        }

        call.setSelectedElevator(nearestElevator);
    }

    public void moveElevators() {
        while (true) {
            for (Elevator elevator : elevators) {
                openDoors(elevator);
                move(elevator);
            }
        }
    }

    public void move(Elevator elevator) {

        // same destination, we arrived again
        if (elevator.isOccupied() &&
                elevator.getTargetCall().get().getPressingButtonFloor() == elevator.getCurrentFloor() &&
                !elevator.getTargetCall().get().isPickedUp()) {

            arrivedToPerson(elevator);

        } else if (elevator.isOccupied() &&
                elevator.getTargetCall().get().getFinalFloor() == elevator.getCurrentFloor() &&
                elevator.getTargetCall().get().isPickedUp()) {

            arrivedToDestination(elevator);

        } else if (elevator.isOccupied()) {
            elevator.move();
        }

    }

    private void openDoors(Elevator elevator) {

        if (elevator.isOccupied() &&
                elevator.getTargetCall().get().getPressingButtonFloor() == elevator.getCurrentFloor() &&
                !elevator.getTargetCall().get().isPickedUp()) {

            arrivedToPerson(elevator);
        }
        if (elevator.isOccupied() &&
                elevator.getTargetCall().get().getFinalFloor() == elevator.getCurrentFloor() &&
                elevator.getTargetCall().get().isPickedUp()) {

            arrivedToDestination(elevator);
        }
    }

    private synchronized void arrivedToPerson(Elevator elevator) {
        elevator.arrivedToPerson();

        System.out.println("DOORS ARE OPEN");
        System.out.println("...");
        System.out.println("...");
        System.out.println("...");
        System.out.println("DOORS ARE CLOSED");
    }

    private synchronized void arrivedToDestination(Elevator elevator) {
        elevator.arrivedToDestination();

        System.out.println("DOORS ARE OPEN");
        System.out.println("...");
        System.out.println("...");
        System.out.println("...");
        System.out.println("DOORS ARE CLOSED");
    }
}
