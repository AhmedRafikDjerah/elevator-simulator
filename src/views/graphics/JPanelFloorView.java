package views.graphics;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;

import views.FloorView;
import models.Building;
import models.Elevator;

public class JPanelFloorView extends FloorView implements InterfaceSwing {
	
	private JPanel floorView = null;
	private JLabel passengerView = null;
	
	public JPanelFloorView(Building building, int floor_index, int floor_kind, int x, int y, int w, int h, Color c) {
		super(building, floor_index, floor_kind);
		floorView = new JPanel();
		floorView.setBackground(c);
		floorView.setBounds(x, y, w, h);
//		System.out.println(floorView.getBounds().toString());
//		floorView.setSize(w, h);
		passengerView = new JLabel();
		displayPassengerAtTheFloor();
		floorView.add(passengerView);
	}

	public void close() {
		floorView.setVisible(false);
	}

	public void display() {
		floorView.setVisible(true);
	}

	public void refresh() {
		displayPassengerAtTheFloor();
	}

	private void displayPassengerAtTheFloor() {
		int x = 0;
		if(floorKind == FloorView.START_FLOOR)
			x = building.getWaitingPassengerCountAtFloor(floorIndex);
		else
			x = building.getArrivedPassengerCountAtFloor(floorIndex);
		
		String t = "";
		for (int i = 0; i < x; i++) {
			t += "|";
		}
		if(t == "") t = "___";
		passengerView.setText(t);
	}

	public Component getComponent() {
		return floorView;
	}

}