// Group no 1. E/15/280, E/15/350
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {

    public static final int BASE_PORT = 2000;  // Port which clients should connect


    /* local data for the server
     * Every main server is defined in terms of the port it
     * listens and the database of allowed users
     */
    private ServerSocket serverSocket = null;  // server Socket for main server
    private StocksDB allowedStockData = null;     // The hash table of the stock data

    public MainServer(int socket, StocksDB users) {
        this.allowedStockData = users;

        try {
            this.serverSocket = new ServerSocket(socket);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    public void postMSG(Boolean obj) {  // send the data entered by the client to the Server
        System.out.println(obj);
    }

    public synchronized boolean authorizedSymbol(String symbol) { // check whether the user entered symbol is in the stocklist
        boolean val = StocksDB.stockList.containsKey(symbol);
        return val;
    }

    public void server_loop() { // when multiple clients try to bid, for each one we create a socket to establish the connection
        try {
            while (true) {
                Socket socket = this.serverSocket.accept();
                ConnectionServer worker = new ConnectionServer(this);
                worker.handleConnection(socket);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}


	




