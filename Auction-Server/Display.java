// Group no 1. E/15/280, E/15/350
import java.awt.*;
import javax.swing.Timer; //for timer
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class Display extends JPanel implements ActionListener {
    JTextArea textArea;
    VisualServer server;

    public Display(VisualServer server) {
        super( new GridBagLayout() );
        textArea = new JTextArea( 20, 50 );
        textArea.setEditable( false );
        textArea.setCaretColor( Color.BLACK );
        JScrollPane scrollPane = new JScrollPane( textArea );

        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;

        c.fill = GridBagConstraints.HORIZONTAL;

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;

        add( scrollPane, c );
        JLabel labelstockSelect=new JLabel( "Enter the Symbol below for Bidding history",SwingConstants.CENTER );
        c.weighty=0.03;
        c.weightx=0.1;
        add(labelstockSelect,c);
        JTextField txtstockSelect = new JTextField( "" );//Create a TextFlied to get user inputs(Stock symbol)
        c.weighty=0.03;
        add( txtstockSelect, c ); //Add textField to gridBag

        JButton btnEnter = new JButton( "Enter" ); //Button to enter the data to search history

        add( btnEnter, c ); //Add button to the gridBag

        DecimalFormat df = new DecimalFormat(); //To get the bid price only to 4 decimal places
        df.setMaximumFractionDigits( 4 );

        // What happens when clicked on the 'Enter' button
        btnEnter.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String input = txtstockSelect.getText(); //get the input text and assign to a String

                //loop through the HistoryofVariation HashMap
                for (Map.Entry<String, ArrayList<HistorOfVariation>> entry : HistorOfVariation.history.entrySet()) {
                    if (entry.getKey().equals( input )) { //Check if the input String equals
                        ArrayList test = entry.getValue(); //Get the value of the input key
                        Iterator<HistorOfVariation> historyItr = test.iterator(); //to iterate through the history of stocks list
                        textArea.append( "History of " + input + "\n" ); //Label the headers
                        textArea.append( "Time\tBid\tBidder\n" );
                        while (historyItr.hasNext()) {
                            HistorOfVariation historyItem = historyItr.next(); //assign the iterator value to a variable
                            textArea.append( "@ " + historyItem.time + "\t" + df.format( historyItem.updatedPrice ) + "\t" + historyItem.bidder + " \n" ); //print the item in the class
                        }
                        textArea.append( "\n\n" );
                    }

                }
            }

        } );

        Timer timer = new Timer( 500, this );
        timer.start();

        this.server = server;
    }

    public void actionPerformed(ActionEvent e) {
        Boolean newline = server.getMSG();

        if (newline != null) {
            textArea.setText( null );
            textArea.append( "SYMBOl" + "\t" + "PRICE" + "\t" + "SECURITY NAME" + "\n\n" );


            for (Map.Entry<String, ItemDetails> entry : StocksDB.stockList.entrySet()) {

                // Get the stock prices of the given stocks
                if (entry.getKey().equals( "FB" ) || entry.getKey().equals( "VRTU" ) || entry.getKey().equals( "MSFT" ) || entry.getKey().equals( "GOOGL" ) || entry.getKey().equals( "YAHOO" ) || entry.getKey().equals( "XLNX" ) || entry.getKey().equals( "TSLA" ) || entry.getKey().equals( "TXN" )) {

                    textArea.append( entry.getKey() + "\t" + entry.getValue().itemPrice + "\t" + entry.getValue().securityName + "\n" );
                }
            }
            textArea.append( "\n" );


            textArea.setCaretPosition( textArea.getDocument().getLength() );


        }
    }


    public static void main(String[] args) throws IOException {
        //Create and set up the window.
        JFrame frame = new JFrame( "TextDemo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        StocksDB allowedUsers = new StocksDB( "stocks.csv", "Symbol", "Security Name", "Price " );

        VisualServer server = new VisualServer( MainServer.BASE_PORT, allowedUsers );
        //Add contents to the window.
        frame.add( new Display( server ) );

        //Display the window.
        frame.pack();
        frame.setVisible( true );

        server.server_loop();
    }
}