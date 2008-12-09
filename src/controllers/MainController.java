package controllers;

import java.util.ArrayList;
import java.util.LinkedList;

import strategies.ElevatorStrategy;
import views.graphics.AnimatedElevator;
import views.graphics.AnimatedGroup;
import views.graphics.AnimatedPerson;
import views.graphics.FixedFloor;
import views.graphics.MyFrame;
import factories.SimulatorFactory;
import java.util.Random;
import main.Console;
import models.*;

/**
 *  MainController est construit sur le modele du design pattern Singleton
 *  En effet, il ne peut y avoir qu'une seule instance de ce controleur a la fois.
 *  
 * @author remy
 *
 */
public class MainController {

<<<<<<< HEAD:src/controllers/MainController.java
    private static MainController INSTANCE = null;    // Le point d'acces a tous les modeles (le batiment a acces direct aux elevators et aux passagers)
    public static Building building = null;

    public Building getBuilding() {
        return building;
    }

    /**
     * La presence d'un constructeur prive supprime
     * le constructeur public par defaut
     */
    private MainController() {
    }

    /**
     * Le mot-cl� synchronized sur la m�thode de cr�ation
     * emp�che toute instanciation multiple m�me par
     * diff�rents threads.
     * @return L'unique instance du singleton.
     */
    /**
     * 
     * @return
     */
    public synchronized static MainController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MainController();
        }
        return INSTANCE;
    }

    /**
     * Méthode startSimulation pour lancer la simulation
     * @param floor_count           Nombre d'étages
     * @param elevator_count        Nombe d'ascenseur
     * @param person_per_elevator   personnes par ascenseur
     * @param person_count          nombre de personnes total
     * @param group_count           nombre de groupe total
     * @param elevator_strategy     strategie - comportement de l'ascenseur
     */
    public void startSimulation(int floor_count, int elevator_count, int person_per_elevator, int person_count, int group_count, ElevatorStrategy elevator_strategy) {
        Console.info("Lancement d'une partie avec " + floor_count + " etages, " + elevator_count +
                " ascenseurs, " + person_per_elevator + " max personnes par ascenseur, " + person_count + " individus et " + group_count + " groupes.");

        SimulatorFactory sf = new SimulatorFactory();

        // Constructs the buildings.
        building = sf.getBuilding(floor_count);

        // Constructs the elevators
        ArrayList<Elevator> elevators = new ArrayList<Elevator>(elevator_count);
        Elevator elevator;



        for (int i = 1; i <= elevator_count; i++) {

            


            //			elevator = sf.getElevator("LINEAR_IN_THE_DIRECTION", person_per_elevator);
            elevator = sf.getElevator("LINEAR", person_per_elevator);
            //			elevator = sf.getElevator("NAWAK", 5);
            //			elevator = sf.getElevator("OPERATEWITHBLOCKING", 5);		
            // Avec plugin
            //			elevator = sf.getElevator(elevator_strategy, person_per_elevator);
            elevator.setIdentifier(i);
            //Placement des Elevators
            Random rand = new Random();
            elevator.setCurrentFloor( rand.nextInt(floor_count+1));
            
            elevators.add(elevator);
        }
        // Add elevators to the building
        building.setElevators(elevators);

        // Constructs the passengers (only persons for now)
        LinkedList<Passenger> passengers = new LinkedList<Passenger>();
        int j = 0, group_nb = 0;
        while (j < person_count) {
            if (group_nb < group_count) {
                Group group = sf.getGroup(building.getFloorCountWithGround());
                passengers.add(group);
                j += group.getPersonCount();
                group_nb++;
            } else {
                passengers.add(sf.getPerson(building.getFloorCountWithGround()));
                j++;
            }
        }
        // Add passengers to the building
        building.setPassengers(passengers);


        // Graphics!
        MyFrame frame = new MyFrame(elevator_count, building.getFloorCountWithGround());
        for (int i = 0; i < elevators.size(); i++) {
            AnimatedElevator e = new AnimatedElevator(frame, elevators.get(i), FixedFloor.FLOOR_WIDTH + (AnimatedElevator.ELEVATOR_WIDTH * i), MyFrame.frame_height - (AnimatedElevator.ELEVATOR_HEIGHT*(elevators.get(i).getCurrentFloor()+1)));
            frame.addAnimatedObject(e);
            elevators.get(i).setAnimatedElevator(e);
        }

        for (int i = 0; i <= building.getFloorCountWithGround(); i++) {
            frame.addFixedObject(new FixedFloor(0, MyFrame.frame_height - (AnimatedElevator.ELEVATOR_HEIGHT * i), i));
            frame.addFixedObject(new FixedFloor(FixedFloor.FLOOR_WIDTH + (elevator_count * AnimatedElevator.ELEVATOR_WIDTH), MyFrame.frame_height - (AnimatedElevator.ELEVATOR_HEIGHT * i), i));
        }

        Passenger passenger;
        for (int i = 0; i < building.getPassengers().size(); i++) {
            passenger = building.getPassengers().get(i);
            if (passenger instanceof Person) {
                frame.addAnimatedObject(new AnimatedPerson(frame, (Person) passenger, FixedFloor.FLOOR_WIDTH - AnimatedPerson.PERSON_WIDTH - (AnimatedPerson.PERSON_WIDTH * building.getPassengerIndexAtHisFloor(passenger)), MyFrame.frame_height - (AnimatedElevator.ELEVATOR_HEIGHT * passenger.getCurrentFloor()) - AnimatedPerson.PERSON_HEIGHT));
            } else if (passenger instanceof Group) {
                System.out.println("Dessin d'un groupe de " + passenger.getPersonCount() + " personnes.");
                frame.addAnimatedObject(new AnimatedGroup(frame, (Group) passenger, FixedFloor.FLOOR_WIDTH - AnimatedPerson.PERSON_WIDTH - (AnimatedPerson.PERSON_WIDTH * building.getPassengerIndexAtHisFloor(passenger)), MyFrame.frame_height - (AnimatedElevator.ELEVATOR_HEIGHT * passenger.getCurrentFloor()) - AnimatedPerson.PERSON_HEIGHT));
            }
        }
    }

    /**
     * Méthode affichant les passagers de l'étage passé en paramètre
     * @param floor_count
     */
    private void displayPassengersPerFloor(int floor_count) {
        
        ArrayList<Passenger> passengers_per_floor = new ArrayList<Passenger>();
        ArrayList<Passenger> temp_passengers = new ArrayList<Passenger>();
        temp_passengers = (ArrayList<Passenger>) building.getPassengers().clone();

        Passenger p;
        String mectons = "";
        int floor = 0,  elevator_floor = 0;
        for (int l = floor_count - 1; l >= 0; l--) {
            passengers_per_floor.clear();
            for (int k = 0; k < temp_passengers.size(); k++) {
                p = temp_passengers.get(k);
                if (p.getCurrentFloor() == l && !p.isInTheElevator()) {
                    floor = p.getCurrentFloor();
                    elevator_floor = (int) p.getElevator().getCurrentFloor();
                    passengers_per_floor.add(p);
                    temp_passengers.remove(p);

                }
            }
            mectons = (floor == elevator_floor) ? "|X| " : "|" + Integer.toString(l) + "| ";
            for (Passenger q : passengers_per_floor) {
                mectons += q.isArrived() ? "A " : "W ";
            }
            Console.debug(mectons);
        }
        Console.debug("");

        mectons = "";
        for (Elevator e : building.getElevators()) {
            for (Passenger q : e.getPassengers()) {
                mectons += q.isArrived() ? "A " : "W ";
            }
            Console.debug("|" + mectons + "|(" + e.getCurrentWeight() + " kg) Elevator " + e.getIdentifier());
        }
    /**/
    }
=======
	private static MainController INSTANCE = null;    // Le point d'acces a tous les modeles (le batiment a acces direct aux elevators et aux passagers)
	public static Building building = null;

	public Building getBuilding() {
		return building;
	}

	/**
	 * La présence d'un constructeur privé supprime
	 * le constructeur public par défaut.
	 */
	private MainController() {
	}

	/**
	 * Le mot-clé synchronized sur la méthode de création
	 * empêche toute instanciation multiple même par
	 * différents threads.
	 * @return L'unique instance du singleton.
	 */
	public synchronized static MainController getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MainController();
		}
		return INSTANCE;
	}

	public void startSimulation(int floor_count, int elevator_count, int person_per_elevator, int person_count, int group_count, ElevatorStrategy elevator_strategy) throws InstantiationException, IllegalAccessException {
		Console.info("Lancement d'une partie avec " + floor_count + " etages, " + elevator_count +
				" ascenseurs, " + person_per_elevator + " max personnes par ascenseur, " + person_count + " individus et " + group_count + " groupes.");

		SimulatorFactory sf = new SimulatorFactory();

		// Constructs the buildings.
		building = sf.getBuilding(floor_count);

		// Constructs the elevators
		ArrayList<Elevator> elevators = new ArrayList<Elevator>(elevator_count);
		Elevator elevator;

		for (int i = 1; i <= elevator_count; i++) {		
			// Avec plugin
			elevator = sf.getElevator((ElevatorStrategy)elevator_strategy.getClass().newInstance(), person_per_elevator);
			elevator.setIdentifier(i);
			//Placement des Elevators
			Random rand = new Random();
			elevator.setCurrentFloor( rand.nextInt(floor_count+1));
			elevators.add(elevator);
		}
		// Add elevators to the building
		building.setElevators(elevators);

		// Constructs the passengers (only persons for now)
		LinkedList<Passenger> passengers = new LinkedList<Passenger>();
		int j = 0, group_nb = 0;
		while (j < person_count) {
			if (group_nb < group_count) {
				Group group = sf.getGroup(building.getFloorCountWithGround());
				passengers.add(group);
				j += group.getPersonCount();
				group_nb++;
			} else {
				passengers.add(sf.getPerson(building.getFloorCountWithGround()));
				j++;
			}
		}
		// Add passengers to the building
		building.setPassengers(passengers);


		// Graphics!
		MyFrame frame = new MyFrame(elevator_count, building.getFloorCountWithGround());
		for (int i = 0; i < elevators.size(); i++) {
			AnimatedElevator e = new AnimatedElevator(frame, elevators.get(i), FixedFloor.FLOOR_WIDTH + (AnimatedElevator.ELEVATOR_WIDTH * i), MyFrame.frame_height - (AnimatedElevator.ELEVATOR_HEIGHT*(elevators.get(i).getCurrentFloor()+1)));
			frame.addAnimatedObject(e);
			elevators.get(i).setAnimatedElevator(e);
		}

		for (int i = 0; i < building.getFloorCountWithGround(); i++) {
			frame.addFixedObject(new FixedFloor(0, MyFrame.frame_height - (AnimatedElevator.ELEVATOR_HEIGHT *(i+1)), i));
			frame.addFixedObject(new FixedFloor(FixedFloor.FLOOR_WIDTH + (elevator_count * AnimatedElevator.ELEVATOR_WIDTH), MyFrame.frame_height - (AnimatedElevator.ELEVATOR_HEIGHT *(i+1)), i));
		}

		Passenger passenger;
		for (int i = 0; i < building.getPassengers().size(); i++) {
			passenger = building.getPassengers().get(i);
			if (passenger instanceof Person) {
				frame.addAnimatedObject(new AnimatedPerson(frame, (Person) passenger, FixedFloor.FLOOR_WIDTH - AnimatedPerson.PERSON_WIDTH - (AnimatedPerson.PERSON_WIDTH * building.getPassengerIndexAtHisFloor(passenger)), MyFrame.frame_height - (AnimatedElevator.ELEVATOR_HEIGHT * passenger.getCurrentFloor()) - AnimatedPerson.PERSON_HEIGHT));
			} else if (passenger instanceof Group) {
				System.out.println("Dessin d'un groupe de " + passenger.getPersonCount() + " personnes.");
				frame.addAnimatedObject(new AnimatedGroup(frame, (Group) passenger, FixedFloor.FLOOR_WIDTH - AnimatedPerson.PERSON_WIDTH - (AnimatedPerson.PERSON_WIDTH * building.getPassengerIndexAtHisFloor(passenger)), MyFrame.frame_height - (AnimatedElevator.ELEVATOR_HEIGHT * passenger.getCurrentFloor()) - AnimatedPerson.PERSON_HEIGHT));
			}
		}
	}

	private void displayPassengersPerFloor(int floor_count) {
		/**/
		ArrayList<Passenger> passengers_per_floor = new ArrayList<Passenger>();
		ArrayList<Passenger> temp_passengers = new ArrayList<Passenger>();
		temp_passengers = (ArrayList<Passenger>) building.getPassengers().clone();

		Passenger p;
		String mectons = "";
		int floor = 0,  elevator_floor = 0;
		for (int l = floor_count - 1; l >= 0; l--) {
			passengers_per_floor.clear();
			for (int k = 0; k < temp_passengers.size(); k++) {
				p = temp_passengers.get(k);
				if (p.getCurrentFloor() == l && !p.isInTheElevator()) {
					floor = p.getCurrentFloor();
					elevator_floor = (int) p.getElevator().getCurrentFloor();
					passengers_per_floor.add(p);
					temp_passengers.remove(p);

				}
			}
			mectons = (floor == elevator_floor) ? "|X| " : "|" + Integer.toString(l) + "| ";
			for (Passenger q : passengers_per_floor) {
				mectons += q.isArrived() ? "A " : "W ";
			}
			Console.debug(mectons);
		}
		Console.debug("");

		mectons = "";
		for (Elevator e : building.getElevators()) {
			for (Passenger q : e.getPassengers()) {
				mectons += q.isArrived() ? "A " : "W ";
			}
			Console.debug("|" + mectons + "|(" + e.getCurrentWeight() + " kg) Elevator " + e.getIdentifier());
		}
		/**/
	}
>>>>>>> a3cd17d8f09c921031a95b7dd59d5a3949316c61:src/controllers/MainController.java
}