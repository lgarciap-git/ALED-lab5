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
<<<<<<< HEAD
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
=======
// TODO
	private Queue<Patient> waitQueue;
	
	public AreaQueue (String name, int time, int capacity, Position2D position) {
		super(name, time, capacity, position);
		this.waitQueue = new LinkedList<Patient>();
		
		
	}
	
	@Override
	public synchronized void enter(Patient p) {
		
		System.out.println("Patient " + p.getNumber() + " trying to enter " + this.name);
		
		this.waiting++;
		this.waitQueue.add(p);
		
		try {
			while(numPatients >= capacity) {
				System.out.println(getName() + " está lleno. Se añade " + p.getNumber() + " a la cola.");
				System.out.println("En primer lugar de a cola está " + waitQueue.peek().getNumber());
				
				wait();
				
			}
			
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		//Cuando hay un hueco para ser atendido, disminuye la espera, aumenta numero pacientes  
		waiting--;
		numPatients++;
		System.out.println("El paciente " + waitQueue.peek().getNumber() + " está siendo atendido ya en " + getName());
		waitQueue.remove();
		
		
	}
>>>>>>> f56069286b3a758548c65708467d693074a821d0
}
