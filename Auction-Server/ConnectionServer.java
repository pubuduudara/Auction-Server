// Group no 1. E/15/280, E/15/350
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

class ConnectionServer implements Runnable { // multithread to connect multiple client at the same time

    public static final long CURRENT_TIME_MILLS = System.currentTimeMillis(); // used to get the bidding time
    public static final int WAIT_AUTH = 0; // inital state before client enters the name
    public static final int AUTH_DONE = 1; // state after client entered the name
    public int AUTH_DONE_2 = 0; // state when clinet entered a correct symbol


    private Socket mySocket; // connection socket per thread
    private int currentState; // used to check the state between WAIT_AUTH or AUTH_DONE
    private MainServer mainServer; // when new clinet is connected new socket created for the client

    public ConnectionServer(MainServer mainServer) { // when a new clinet is connected initialize a socket, state
        this.mySocket = null;
        this.currentState = WAIT_AUTH;
        this.mainServer = mainServer;
    }

    public boolean handleConnection(Socket socket) {// create a new theread to the new client and execute
        this.mySocket = socket;
        Thread newThread = new Thread(this);
        newThread.start();
        return true;
    }

    public void run() {

        BufferedReader in = null; // get input stream enterd by the client via the command line
        PrintWriter out = null; // send the output steam to the clinet to dispaly in the coommand line
        try {

            in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));

            out = new PrintWriter(new OutputStreamWriter(mySocket.getOutputStream()));
            out.print("Enter your name\n"); // welome message to the client
            out.flush();
            PeopleDetails person = new PeopleDetails(); // create a new class to each and every clinet connected to the server
            Date instant = new Date(CURRENT_TIME_MILLS); // used to get the bidding time
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); // used to get the bidding time
            String line, outline;

            for (line = in.readLine(); line != null && !line.equals("quit"); line = in.readLine()) {

                switch (currentState) {
                    case WAIT_AUTH: // before the clinet name is entered
                        String x = line.toString();
                        person.setName(x); // set the name of the clinet to the created object
                        String welcomeMessage = "Hello " + person.getName() + ", submit the symbol you wish to bid " + '\n';
                        outline = welcomeMessage;
                        currentState = AUTH_DONE;
                        break;

                    case AUTH_DONE: // after client entred the name
                        if (AUTH_DONE_2 == 0) { // check the validity of the symbol
                            person.setSymbol(line); // set the symbol
                            if (!mainServer.authorizedSymbol(line)) { // if the enterd symbol is invalid
                                outline = "-1 Enter a valid symbol\n";

                            } else {
                                AUTH_DONE_2 = 1; // if the enterd symbol is valid

                                outline = "your Symbol posted. The current price of " + person.getSymbol() + " is " + StocksDB.stockList.get(person.getSymbol()).itemPrice + ". Enter your first bid\n";
                            }
                        } else {
                            try {
                                double bid = Double.parseDouble(line);
                            } catch (Exception e) {
                                outline = "Enter a valid bid\n"; // if the clinet enterd non numerical value
                                break;
                            }

                            String nameOfthePerson = person.getName();
                            String nameOftheSymbol = person.getSymbol();
                            person.setPrice(Double.parseDouble(line));
                            Double enterdbid = person.getPrice();

                            String time = sdf.format(instant);
                            HistorOfVariation obj = new HistorOfVariation(nameOfthePerson, enterdbid, time); // each item bid history is stored as an object of the HistorOfVariation class

                            if (obj.checkey(nameOftheSymbol)) {
                                obj.addobj(obj, nameOftheSymbol);
                            }
                            Double highestbid = StocksDB.stockList.get(person.getSymbol()).itemPrice; // get the curret highest bid of the symbol
                            if (highestbid < enterdbid) {
                                StocksDB.updateStockList(nameOftheSymbol, enterdbid); // update the current value as the highest value only if user enters an amount grater than the curretn prie
                            }

                            outline = "Current highest bid is "+StocksDB.stockList.get(person.getSymbol()).itemPrice+". Enter the next bid\n";
                            mainServer.postMSG(true);
                        }
                        break;

                    default:
                        System.out.println("Undefined state");
                        return;


                }
                out.print(outline); // output send to the server
                out.flush();
            }
            out.close();
            in.close();
            this.mySocket.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
    

