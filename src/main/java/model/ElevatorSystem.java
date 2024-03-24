package model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;
import java.util.stream.IntStream;

public class ElevatorSystem {
    private static final int MAX_ELEVATORS = 16;
    private static final int NUMBER_OF_FLOORS = 100;

    private List<Elevator> elevators = new ArrayList<>();
    private PriorityQueue<Call> calls = new PriorityQueue<>((Call a, Call b) -> {
        int distanceA = Math.abs(a.getFloor() - elevators.get(0).getCurrentFloor());
        int distanceB = Math.abs(b.getFloor() - elevators.get(0).getCurrentFloor());
        return Integer.compare(distanceA, distanceB);
    });;

    public ElevatorSystem () {
        this(MAX_ELEVATORS);
    }
    public ElevatorSystem (int amountOfElevators){
        IntStream.range(0, amountOfElevators)
                .mapToObj(i -> new Elevator())
                .forEach(elevators::add);
    }

    void pickup(int floor, ElevatorDirection direction) {
        calls.offer(new Call(floor, direction));
    }

    void selectFloor(UUID elevatorId, int floor) {
        Elevator.getElevatorById(elevatorId, elevators).ifPresent(elevator -> {
            elevator.setTargetFloor(floor);

        });
    }

    void update(UUID elevatorId, int currentFloor, int targetFloor) {
        Elevator.getElevatorById(elevatorId, elevators).ifPresent(elevator -> {
                        elevator.setCurrentFloor(currentFloor);
                        elevator.setTargetFloor(targetFloor);

                });
    }

}
