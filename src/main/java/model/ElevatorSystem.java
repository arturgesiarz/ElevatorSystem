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

    void pickup() {
        // TODO
    }


}