	package es.upm.dit.aled.lab5;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import es.upm.dit.aled.lab5.gui.EmergencyRoomGUI;
import es.upm.dit.aled.lab5.gui.Position2D;

/**
 * Models a patient in a hospital ER. Each Patient is characterized by its
 * number (which must be unique), its current location and a protocol. The
 * protocol is a List of Transfers. Each Patient also has an index to indicate
 * at which point of the protocol they are at the current time.
 * 
 * Patients are Threads, and therefore must implement the run() method.
 * 
 * Each Patient is represented graphically by a dot of diameter 10 px, centered
 * in a given position and with a custom color.
 * 
 * @author rgarciacarmona
 */
public class Patient extends Thread {

	private int number;
	private List<Transfer> protocol;
	private int indexProtocol;
	private Area location;
	private Position2D position;
	private Color color;

	/**
	 * Builds a new Patient.
	 * 
	 * @param numbre          The number of the Patient.
	 * @param initialLocation The initial location of the Patient.
	 */
	public Patient(int number, Area initialLocation) {
		this.number = number;
		this.protocol = new ArrayList<Transfer>();
		this.indexProtocol = 0;
		this.location = initialLocation;
		this.position = initialLocation.getPosition();
		this.color = Color.GRAY; // Default color
	}

	/**
	 * Returns the number of the Patient.
	 * 
	 * @return The number.
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Returns the protocol of the Patient.
	 * 
	 * @return The protocol.
	 */
	public List<Transfer> getProtocol() {
		return protocol;
	}

	/**
	 * Returns the current location of the Patient.
	 * 
	 * @return The current location.
	 */
	public Area getLocation() {
		return location;
	}

	/**
	 * Changes the current location of the Patient.
	 * 
	 * @param location The new location.
	 */
	public void setLocation(Area location) {
		this.location = location;
	}

	/**
	 * Returns the position of the Patient in the GUI.
	 * 
	 * @return The position.
	 */
	public Position2D getPosition() {
		return position;
	}

	/**
	 * Changes the position of the Patient in the GUI.
	 * 
	 * @param position The new position.
	 */
	public void setPosition(Position2D position) {
		this.position = position;
	}

	/**
	 * Returns the color of Patient in the GUI.
	 * 
	 * @return The color.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Changes the color of the Patient in the GUI.
	 * 
	 * @param color The new color.
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Adds a new Transfer at the end of the Patient's protocol.
	 * 
	 * @param transfer The new Transfer.
	 */
	public void addToProtocol(Transfer transfer) {
		this.protocol.add(transfer);
	}

	/**
	 * Advances the Patient's protocol. The Patient is moved to the new Area, the
	 * movement is animated by the GUI and the index is increased by one.
	 */
	private void advanceProtocol() {
		Transfer transfer = protocol.get(indexProtocol);
		indexProtocol++;
		System.out.println("Patient " + this.number + " is moving from " + this.location + " to " + transfer.getTo());
		EmergencyRoomGUI.getInstance().animateTransfer(this, transfer);
		this.location = transfer.getTo();
	}

	/**
	 * Simulates the treatment of the Patient at its current location. Therefore,
	 * the Patient must spend at this method the amount of time specified in such
	 * Area.
	 */
	private void attendedAtLocation() {
		try {
			System.out.println("Patient " + this.number + " is being attended at " + this.location);
			sleep(this.location.getTime());
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt(); // Restore interrupted status
		}
	}

	/**
	 * Ejecuta el comportamiento del Patient. Sigue su protocolo siendo atendido
	 * en la ubicación actual y luego avanzando a la siguiente, hasta que se
	 * alcanza el último paso del protocolo. En ese momento, el Patient se
	 * elimina de la GUI.
	 * 
	 * Es importante destacar que: 
	 * - Antes de ser atendido en una ubicación, el Patient debe entrar primero en ella. 
	 *   Si está llena, permanecerá esperando.
	 * - Después de ser atendido en una ubicación, el Patient debe salir de ella. 
	 *   De esta manera, el Area sabe que debe permitir el acceso a otro Patient que estaba esperando.
	 */

	@Override
	public void run() {
		// TODO
		 
		//Entra al area, le atienden y sale: no lo meto en el while por si acaso está en el último paso del protocolo 
		location.enter(this);
		attendedAtLocation();
		location.exit(this);
			
		//Siguiente area del protocolo, entra, le atienden sale hasta completar protocolo
		while (indexProtocol < protocol.size()) { 
			advanceProtocol();
			location.enter(this);
			attendedAtLocation();
			location.exit(this);
		}
		
		System.out.println("El paciente " + number + " ya no tiene más tranferencias pendientes, por lo que se elimina de la GUI"); 
		EmergencyRoomGUI.getInstance().removePatient(this);
		
	}
	/*
	public void run() {
		// TODO
		 
		
		
		//tb puedo meterlo todo en el while si primero entra-le atienden-sale + avanza y se repite	
		//Siguiente area del protocolo, entra, le atienden sale hasta completar protocolo
		while (indexProtocol < protocol.size()) { 
			location.enter(this);
			attendedAtLocation();
			location.exit(this);
			advanceProtocol();
			
		}
		
		System.out.println("El paciente " + number + " ya no tiene más tranferencias pendientes, por lo que se elimina de la GUI"); 
		EmergencyRoomGUI.getInstance().removePatient(this);
		
	}

	*/

}
