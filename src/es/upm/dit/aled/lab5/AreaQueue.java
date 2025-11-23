package es.upm.dit.aled.lab5;

import java.util.LinkedList;
import java.util.Queue;

import es.upm.dit.aled.lab5.gui.Position2D;

/**
 * Extensión de Area que mantiene una cola estricta de Patients que esperan
 * para entrar. Después de que un Patient salga, se permite entrar al primero
 * de la cola.
 * 
 * @author rgarciacarmona
 */

public class AreaQueue extends Area {
	// SOLUCION
		private Queue<Patient> waitQueue;

		/**
		 * Builds a new AreaQueue.
		 * 
		 * @param name     The name of the AreaQueue.
		 * @param time     The amount of time (in milliseconds) needed to treat a
		 *                 Patient in this AreaQueue.
		 * @param capacity The number of Patients that can be treated at the same time.
		 * @param position The location of the AreaQueue in the GUI.
		 */
		public AreaQueue(String name, int time, int capacity, Position2D position) {
			super(name, time, capacity, position);
			this.waitQueue = new LinkedList<Patient>(); // Initialize the queue
		}

		/**
		 * Thread safe method that allows a Patient to enter the area. If the Area is
		 * currently full, the Patient thread must wait. The Patients waiting are stored
		 * in a strict queue, so they follow this order to enter after another Patient
		 * has exited.
		 * 
		 * @param p The patient that wants to enter.
		 */
		@Override
		public synchronized void enter(Patient patient) {
			System.out.println("Patient " + patient.getNumber() + " trying to enter " + this.getName());
			this.waiting++;
			this.waitQueue.add(patient); // Add to the end of the queue
			try {
				while (numPatients >= capacity || this.waitQueue.peek() != patient) {
					System.out.println("Patient " + patient.getNumber() + " waiting for " + this.getName()
							+ ". Front of the queue?: " + this.waitQueue.peek().equals(patient));
					wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt(); // Restore interrupted status
			}
			this.waitQueue.remove(); // Dequeue from the front
			this.numPatients++;
			this.waiting--;
			System.out.println("Patient " + patient.getNumber() + " has entered " + this.name);
		}
		// SOLUCION
}
