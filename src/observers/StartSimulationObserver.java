package observers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import views.*;
import views.graphics.ConfigView;

import controllers.MainController;

public class StartSimulationObserver implements ActionListener {

	private ConfigView window;
	
	public StartSimulationObserver(ConfigView f) {
		this.window = f;
	}
	
	public void actionPerformed(ActionEvent e) {
		window.setVisible(false);
		MainController.getInstance().startSimulation(window.get_floor_count(), window.get_elevator_count(),
				window.get_person_per_elevator_count(),	window.get_person_count(), window.get_group_count());
	}

}
