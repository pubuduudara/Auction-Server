import java.util.*; 

class VisualServer extends MainServer { 
    private static LinkedList<Boolean> msgs;
    private static LinkedList<String>  p;

    public VisualServer(int socket, StocksDB user) {
	super(socket, user); 
	msgs = new LinkedList<Boolean>();
    }

    @Override 
    public synchronized void postMSG(Boolean obj) {
	// I can override and make function synchronized 
	msgs.add(obj);
    }

    public synchronized Boolean getMSG() {
	if(!msgs.isEmpty()) return msgs.remove(); 	    
	return null; 
    }

}
	