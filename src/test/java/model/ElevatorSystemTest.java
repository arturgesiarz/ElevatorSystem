package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorSystemTest {

    private ElevatorSystem elevatorSystem;

    @BeforeEach
    public void setUp() {
        elevatorSystem = new ElevatorSystem();
    }

    @Test
    void pickup() {
        Call call = new Call(5, ElevatorDirection.UP);
        elevatorSystem.pickup(call);
        assertNotNull(call.getSelectedElevator());
    }

    @Test
    void move() {
        Elevator elevator = new Elevator();
        Call call = new Call(5, ElevatorDirection.UP);
        call.setSelectedElevator(elevator);
        elevator.setTargetCall(Optional.of(call));
        elevator.setCurrentFloor(3);

        elevatorSystem.move(elevator);
        assertEquals(4, elevator.getCurrentFloor());

        elevatorSystem.move(elevator);
        assertEquals(5, elevator.getCurrentFloor());
    }
}