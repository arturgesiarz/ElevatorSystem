package model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.stream.IntStream;


public class ElevatorSystem {
    private static final int MAX_ELEVATORS = 3;
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

    public synchronized void pickup(Call call) {

        // acquire the nearest free lift
        Elevator nearestElevator = Elevator.findNeartestFreeElevation(elevators, call);

        while (nearestElevator == null) {
            try {
                wait();
                System.out.println(call.getPressingButtonFloor());
                nearestElevator = Elevator.findNeartestFreeElevation(elevators, call);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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

    public synchronized void move(Elevator elevator) {

        // same destination, we arrived again
        if (elevator.isOccupied() &&
                elevator.getTargetCall().get().getPressingButtonFloor() == elevator.getCurrentFloor()) {
            destinationReached(elevator);

        } else if (elevator.isOccupied()) {
            elevator.move();
        }

    }

    private synchronized void openDoors(Elevator elevator) {

        // elevator came for the user
        if (elevator.isOccupied() &&
                elevator.getTargetCall().get().getPressingButtonFloor() == elevator.getCurrentFloor()) {

            destinationReached(elevator);
        }
    }

    private synchronized void destinationReached(Elevator elevator) {
        elevator.arrived();

        System.out.println("DOORS ARE OPEN");
        System.out.println("...");
        System.out.println("...");
        System.out.println("...");
        System.out.println("DOORS ARE CLOSED");

        notifyAll();
    }


    public void status() {
        // displays up-to-date information on all lifts
        elevators.forEach(elevator -> {
            if (elevator.isOccupied()) {
                System.out.println(elevator);
            }
        });
    }

    public void followTheLift(Call actCall) {
        // function to follow the lift
        Elevator elevator = actCall.getSelectedElevator();

        while (elevator.isOccupied()) {
            System.out.println(elevator);
        }
    }

    public static void main(String[] args) {
        ElevatorSystem elevatorSystem = new ElevatorSystem();

        // each lift call is separate
        Call call1 = new Call(20,ElevatorDirection.UP);
        Call call2 = new Call(2, ElevatorDirection.UP);
        Call call3 = new Call(2, ElevatorDirection.UP);
        Call call4 = new Call(10, ElevatorDirection.DOWN);

        // a new thread is created for each lift call
        call1.setFinalFloor(21);
        call2.setFinalFloor(10);
        call3.setFinalFloor(10);
        call4.setFinalFloor(2);

        new Thread(() -> elevatorSystem.pickup(call1)).start();
        new Thread(() -> elevatorSystem.pickup(call2)).start();
        new Thread(() -> elevatorSystem.pickup(call3)).start();
        new Thread(() -> elevatorSystem.pickup(call4)).start();

        // at the very end I start the lifts in the main thread
        elevatorSystem.moveElevators();
    }

}

//  TODO DODAC MOZLIWOSC WYBIERANIA PIETRA ABY TO DZIALALO DOBRZE