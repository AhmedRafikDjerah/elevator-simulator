package views;

import models.Building;
import models.Elevator;
import controllers.MainController;

public abstract class ElevatorView {
	private MainController controller = null;
	private Elevator elevator;
	private Building building;
	protected int identifier;
	
	public ElevatorView(MainController controller, Elevator elevator, Building building, int identifier){		
		this.controller = controller;
		this.elevator = elevator;
		this.building = building;
		this.identifier = identifier;
	}
	
	public final MainController getController(){
		return controller;
	}
	
	public abstract void display();
	public abstract void close();
	
	public abstract Object getElevator();

	public abstract void moveBy(int x, int y);
	
}