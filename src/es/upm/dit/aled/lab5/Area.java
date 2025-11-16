package es.upm.dit.aled.lab5;

import java.awt.Color;
import java.util.Objects;

import es.upm.dit.aled.lab5.gui.Position2D;

/**
 * Models an area in a hospital ER. Each Area is characterized by its name
 * (which must be unique), the time a patient must spend there to be treated (in
 * milliseconds), the capacity (maximum number of patients that can be treated
 * at the same time), the number of patients being treated at a given time, and
 * the number of patients currently waiting.
 * 
 * This class is a monitor that ensures that there is a thread safe way to enter
 * and exit the area.
 * 
 * Each Area is represented graphically by a square of side 120 px, centered in
 * a given position and with a custom color.
 * 
 * @author rgarciacarmona
 */
public class Area {

	protected String name;
	private int time;
	private Position2D position;
	private Color color;
	protected int capacity;	
	protected int numPatients;
	protected int waiting;

	/**
	 * Builds a new Area.
	 * 
	 * @param name     The name of the Area.
	 * @param time     The amount of time (in milliseconds) needed to treat a
	 *                 Patient in this Area.
	 * @param capacity The number of Patients that can be treated at the same time.
	 * @param position The location of the Area in the GUI.
	 */
	public Area(String name, int time, int capacity, Position2D position) {
		this.name = name;
		this.time = time;
		this.capacity = capacity;
		this.position = position;
		this.numPatients = 0;
		this.waiting = 0;
		this.color = Color.GRAY; // Default color
	}

	/**
	 * Returns the name of the Area.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the time (in milliseconds) that a Patient must spend in the Area to
	 * be treated.
	 * 
	 * @return The time.
	 */
	public int getTime() {
		return time;
	}

	/**
	 * Returns the position of the center of the Area in the GUI.
	 * 
	 * @return The position.
	 */
	public Position2D getPosition() {
		return position;
	}

	/**
	 * Returns the color of Area in the GUI.
	 * 
	 * @return The color.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Changes the color of the Area in the GUI.
	 * 
	 * @param color The new color.
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Thread safe method that allows a Patient to enter the area. If the Area is
	 * currently full, the Patient thread must wait.
	 * 
	 * @param p The patient that wants to enter.
	 */
	public synchronized void enter(Patient p) {
		while(numPatients >= capacity) {
			try {
				waiting++;
	            System.out.println("Paciente " + p.getNumber() + " esperando para entrar. Pacientes en espera: " + waiting);

				//Hago que se bloquee el thread actual en el Monitor (área); no tengo que hacer que espere el paciente
				wait(); //esto lanza excepcion verificada (InterruptedException) --> try catch pq no hay throw en definición del meth
				waiting--;
				
			}catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	        }
		}
		numPatients++;
	    System.out.println("Paciente " + p.getNumber() + " está siendo atendido. /nPacientes en área: " + numPatients);

	}
	
	
	/**
	 * Thread safe method that allows a Patient to exit the area. After the Patient
	 * has left, this method notifies all waiting Patients.
	 * 
	 * @param p The patient that wants to enter.
	 */
	public synchronized void exit(Patient p) { 
		numPatients--;
	    System.out.println("Paciente " + p.getNumber() + " ha salido. /nPacientes en área: " + numPatients);

		
		//Despertar a todos los threads que esperan en enter()
		notifyAll();
		
		System.out.println("Pacientes en espera: " + waiting);
	}
	
	/**
	 * Returns the capacity of the Area. This method must be thread safe.
	 * 
	 * @return The capacity.
	 */
	public synchronized int getCapacity() {  		//esto es region critica ????
		return capacity;						//no cambia con ningun metodo ??? --> no necesita synchronized ????
	}
	
	
	/**
	 * Returns the current number of Patients being treated at the Area. This method must be thread safe.
	 * 
	 * @return The number of Patients being treated.
	 */
	public synchronized int getNumPatients() {
		return numPatients;
	}
	

	/**
	 * Returns the current number of Patients waiting to be treated at the Area. This method must be thread safe.
	 * 
	 * @return The number of Patients waiting to be treated.
	 */
	public synchronized int getWaiting() {
		return waiting;
	}

	
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Area other = (Area) obj;
		return Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return name;
	}
}