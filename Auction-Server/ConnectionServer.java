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

    public void run() { // can not use "throws .." interface is different

        BufferedReader in = null;
        PrintWriter out = null;
        try {

            in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));

            out = new PrintWriter(new OutputStreamWriter(mySocket.getOutputStream()));
            out.print("Enter your name\n");
            out.flush();
            PeopleDetails person = new PeopleDetails();
            Date instant = new Date(CURRENT_TIME_MILLS);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String line, outline;

            for (line = in.readLine(); line != null && !line.equals("quit"); line = in.readLine()) {

                switch (currentState) {
                    case WAIT_AUTH:
                        String x = line.toString();
                        person.setName(x);
                        String welcomeMessage = "Hello " + person.getName() + ", submit your symbol first " + '\n';
                        outline = welcomeMessage;
                        currentState = AUTH_DONE;
                        break;

                    case AUTH_DONE:
                        if (AUTH_DONE_2 == 0) {
                            person.setSymbol(line);
                            if (!mainServer.authorizedSymbol(line)) {
                                outline = "-1 Enter a valid symbol\n";

                            } else {
                                AUTH_DONE_2 = 1;
                                outline = "your Symbol posted. Enter the first bid\n";
                            }
                        } else {
                            try {
                                double bid = Double.parseDouble(line);
                            } catch (Exception e) {
                                outline = "Enter a valid bid\n";
                                break;
                            }


                            String nameOfthePerson = person.getName();
                            String nameOftheSymbol = person.getSymbol();
                            person.setPrice(Double.parseDouble(line));
                            Double enterdbid = person.getPrice();
                            String time = sdf.format(instant);
                            HistorOfVariation obj = new HistorOfVariation(nameOfthePerson, enterdbid, time);
                            if (obj.checkey(nameOftheSymbol)) {
                                obj.addobj(obj, nameOftheSymbol);
                            }
                            Double highestbid = StocksDB.stockList.get(person.getSymbol()).itemPrice;
                            if (highestbid < enterdbid) {
                                StocksDB.updateStockList(nameOftheSymbol, enterdbid);
                            }

                            outline = "Enter the next bid\n";
                            mainServer.postMSG(true);
                        }
                        break;

                    default:
                        System.out.println("Undefined state");
                        return;


                }
                out.print(outline);
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
    

