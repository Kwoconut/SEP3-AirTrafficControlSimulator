package server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ThreadSafeServer implements ServerAccess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int readers;
	private int writers;
	private int waitingWriters;
	private Server server;

	public ThreadSafeServer(Server server) throws RemoteException {
		readers = 0;
		writers = 0;
		waitingWriters = 0;
		this.server = server;
		UnicastRemoteObject.exportObject(this, 0);
	}

	@Override
	public synchronized GroundRIServerRead acquireGroundRead() throws RemoteException {
		while (writers > 0 || waitingWriters > 0) {
			try {
				wait();
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		readers++;
		return server;
	}
	
	@Override
	public synchronized AirRIServerRead acquireAirRead() throws RemoteException {
		while (writers > 0 || waitingWriters > 0) {
			try {
				wait();
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		readers++;
		return server;
	}

	@Override
	public synchronized void releaseRead() throws RemoteException {
		readers--;
		if (readers == 0) {
			notify();
		}
	}

	@Override
	public synchronized GroundRIServerWrite acquireGroundWrite() throws RemoteException {
		waitingWriters++;
		while (readers > 0 || writers > 0) {
			try {
				wait();
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		waitingWriters--;
		writers++;
		return server;
	}
	
	@Override
	public synchronized AirRIServerWrite acquireAirWrite() throws RemoteException {
		waitingWriters++;
		while (readers > 0 || writers > 0) {
			try {
				wait();
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		waitingWriters--;
		writers++;
		return server;
	}

	@Override
	public synchronized void releaseWrite() throws RemoteException {
		writers--;
		notifyAll();
	}
}
