package model;

import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.IntStream;


public class ElevatorSystem {
    private static final int MAX_ELEVATORS = 16;
    private static final int NUMBER_OF_FLOORS = 100;
    private static final int THREAD_POOL_SIZE = 5;

    private List<Elevator> elevators;
    private PriorityQueue<Call> calls;

    public ElevatorSystem () {
        this(MAX_ELEVATORS);
    }
    public ElevatorSystem (int amountOfElevators){
        IntStream.range(0, amountOfElevators)
                .mapToObj(i -> new Elevator())
                .forEach(elevators::add);
    }

    public void pickup(Call call) {

        // acquire the nearest free lift
        Elevator nearestElevator = Elevator.findNeartestFreeElevation(elevators, call);

        // set the nearest lift
        call.setSelectedElevator(nearestElevator);
    }

    public void step() {
        // 1. button reading
        // TODO

        // 2. opening/closing doors and arrived
        for (Elevator elevator : elevators) {

            // we have arrived !
            if (elevator.isOccupied() &&
                    elevator.getTargetCall().get().getFloor() == elevator.getCurrentFloor()) {
                elevator.arrived();
                System.out.println("DOORS ARE OPEN");
                System.out.println("...");
                System.out.println("...");
                System.out.println("...");
                System.out.println("DOORS ARE CLOSED");
            }
            if (elevator.isOccupied()) {
                elevator.move();
            }
        }


    }

}