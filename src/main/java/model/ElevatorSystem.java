package model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.IntStream;


public class ElevatorSystem {
    private static final int MAX_ELEVATORS = 1;
    private static final int NUMBER_OF_FLOORS = 100;

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
        // acquire the nearest free lift
        Elevator nearestElevator = Elevator.findNeartestFreeElevation(elevators, call);

        while (nearestElevator == null) {
            nearestElevator = Elevator.findNeartestFreeElevation(elevators, call);
        }

        // set the nearest lift
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

    public static void main(String[] args) {
        ElevatorSystem elevatorSystem = new ElevatorSystem();

        // each lift call is separate
        Call call1 = new Call(20,ElevatorDirection.UP);
        Call call2 = new Call(2, ElevatorDirection.UP);
        Call call3 = new Call(1, ElevatorDirection.UP);


        // a new thread is created for each lift call
        call1.setFinalFloor(21);
        call2.setFinalFloor(7);
        call3.setFinalFloor(10);


        new Thread(() -> elevatorSystem.pickup(call1)).start();
        new Thread(() -> elevatorSystem.pickup(call2)).start();
        new Thread(() -> elevatorSystem.pickup(call3)).start();


        // at the very end I start the lifts in the main thread
        elevatorSystem.moveElevators();
    }
}
