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
}
