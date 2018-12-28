// Group no 1. E/15/280, E/15/350
import java.util.*; 

class VisualServer extends MainServer { 
    private static LinkedList<Boolean> msgs;
    private static LinkedList<String>  p;

    public VisualServer(int socket, StocksDB user) {
	super(socket, user); 
	msgs = new LinkedList<Boolean>();
    }

    @Override 
    public synchronized void postMSG(Boolean obj) { // synchronized methods to connect multiple users

	msgs.add(obj);
    }

    public synchronized Boolean getMSG() { // synchronized methods to connect multiple users
	if(!msgs.isEmpty()) return msgs.remove(); 	    
	return null; 
    }

}
	