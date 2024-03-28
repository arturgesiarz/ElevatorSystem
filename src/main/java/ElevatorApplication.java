import model.Call;
import model.ElevatorDirection;
import model.ElevatorSystem;

public class ElevatorApplication {

    public static void main(String[] args) {
        ElevatorSystem elevatorSystem = new ElevatorSystem();

        // each lift call is separate
        Call call1 = new Call(20, ElevatorDirection.UP);
        Call call2 = new Call(2, ElevatorDirection.UP);
        Call call3 = new Call(1, ElevatorDirection.UP);
        Call call4 = new Call(10, ElevatorDirection.DOWN);
        Call call5 = new Call(4, ElevatorDirection.DOWN);


        // a new thread is created for each lift call
        call1.setFinalFloor(21);
        call2.setFinalFloor(11);
        call3.setFinalFloor(10);
        call4.setFinalFloor(0);
        call5.setFinalFloor(1);


        new Thread(() -> elevatorSystem.pickup(call1)).start();
        new Thread(() -> elevatorSystem.pickup(call2)).start();
        new Thread(() -> elevatorSystem.pickup(call3)).start();
        new Thread(() -> elevatorSystem.pickup(call4)).start();
        new Thread(() -> elevatorSystem.pickup(call5)).start();


        elevatorSystem.moveElevators();
    }
}
