package strategies;

import models.Elevator;
import models.Passenger;

/**
 * Classe Abstraite ElevatorStrategy
 * classe de base pour les différents comportements d'ascenseur
 * @author x_nem
 */
public abstract class ElevatorStrategy {

	protected Elevator elevator;

	public ElevatorStrategy() {
	}
	
	public ElevatorStrategy(Elevator elevator) {
		this.elevator = elevator;
	}

	public abstract void acts();

	public abstract boolean takePassenger(Passenger passenger);

	public abstract void releasePassenger(Passenger passenger);
	
	public abstract void leaveThisFloor();
	
	public Elevator getElevator() {
		return elevator;
	}
	
	public void setElevator(Elevator elevator) {
		this.elevator = elevator;
	}
	
	public synchronized void releaseAllArrivedPassengers() {
		for (int i = 0; i < elevator.getPassengerCount(); i++) {
			if(elevator.getPassengers().get(i).getWantedFloor() == elevator.getCurrentFloor()) {
				elevator.releasePassenger(elevator.getPassengers().get(i));
			}
		}
		
//		for (Passenger passenger : elevator.getPassengers()) {
//			if(passenger.getWantedFloor() == elevator.getCurrentFloor()) {
//				elevator.releasePassenger(passenger);
//			}
//		}
	}
	
}