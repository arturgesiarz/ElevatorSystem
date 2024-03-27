package model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.IntStream;


public class ElevatorSystem {
    private static final int MAX_ELEVATORS = 10;
    private static final int NUMBER_OF_FLOORS = 100;

    private List<Elevator> elevators = new ArrayList<>();
    private PriorityQueue<Call> calls;

    public ElevatorSystem () {
        this(MAX_ELEVATORS);
    }
    public ElevatorSystem (int amountOfElevators){
        IntStream.range(0, amountOfElevators)
                .mapToObj(i -> new Elevator())
                .forEach(elevators::add);
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
            synchronized (elevators) {
                for (Elevator elevator : elevators) {
                    openDoors(elevator);
                    move(elevator);
                }
            }

        }
    }

    public synchronized void move(Elevator elevator) {
        if (elevator.isOccupied()) {
            elevator.move();
            System.out.println(elevator);
        }
    }

    private synchronized void openDoors(Elevator elevator) {

        // elevator came for the user
        if (elevator.isOccupied() &&
                elevator.getTargetCall().get().getPressingButtonFloor() == elevator.getCurrentFloor()) {

            elevator.arrived();

            System.out.println("DOORS ARE OPEN");
            System.out.println("...");
            System.out.println("...");
            System.out.println("...");
            System.out.println("DOORS ARE CLOSED");


            notifyAll();
        }
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
        Call call3 = new Call(3, ElevatorDirection.UP);
        Call call4 = new Call(10, ElevatorDirection.DOWN);

        // a new thread is created for each lift call
        // TODO


        new Thread(() -> elevatorSystem.pickup(call1)).start();
        new Thread(() -> elevatorSystem.pickup(call2)).start();
        new Thread(() -> elevatorSystem.pickup(call3)).start();
        new Thread(() -> elevatorSystem.pickup(call4)).start();

        // at the very end I start the lifts in the main thread
        elevatorSystem.moveElevators();
    }

}