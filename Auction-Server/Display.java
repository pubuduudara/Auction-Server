import java.awt.*;
import javax.swing.Timer; //for timer

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.html.HTMLDocument;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class Display extends JPanel implements ActionListener {
    JTextArea textArea;
    VisualServer server;

    public Display(VisualServer server) {
        super(new GridBagLayout());

        textArea = new JTextArea(20, 20);
        textArea.setEditable(false);
        textArea.setCaretColor(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(textArea);

        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;

        c.fill = GridBagConstraints.HORIZONTAL;

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;

        add(scrollPane, c);

        JTextField txtstockSelect = new JTextField("");
        // txtstockSelect.setPlaceHolderTextField("Search");

        add(txtstockSelect, c);

        JButton btnEnter = new JButton("Enter");

        add(btnEnter, c);

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(4);
        btnEnter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String input = txtstockSelect.getText();
                for (Map.Entry<String, ArrayList<HistorOfVariation>> entry : HistorOfVariation.history.entrySet()) {
                    if (entry.getKey().equals(input)) {
                        ArrayList test = entry.getValue();
                        Iterator<HistorOfVariation> historyItr = test.iterator();
                        textArea.append("History of " + input + "\n");
                        textArea.append("Time\tBid\tBidder\n");
                        while (historyItr.hasNext()) {
                            HistorOfVariation historyItem = historyItr.next();
                            textArea.append("@ " + historyItem.time + "\t" + df.format(historyItem.updatedPrice) + "\t" + historyItem.bidder + " \n");
                        }
                        textArea.append("\n\n");
                    }

                }
            }

        });

        Timer timer = new Timer(500, this);
        timer.start();

        this.server = server;
    }

    public void actionPerformed(ActionEvent e) {
        Boolean newline = server.getMSG();

        if (newline != null) {
            textArea.setText(null);
            textArea.append("SYMBOl"+"\t"+"PRICE"+"\t"+"SECURITY NAME"+"\n\n");

            //System.out.println(HistorOfVariation.history);
            for (Map.Entry<String, ItemDetails> entry : StocksDB.stockList.entrySet()) {
                if (entry.getKey().equals("FB") || entry.getKey().equals("VRTU") || entry.getKey().equals("MSFT") || entry.getKey().equals("GOOGL") || entry.getKey().equals("YAHOO") || entry.getKey().equals("XLNX") || entry.getKey().equals("TSLA") || entry.getKey().equals("TXN")) {

                    textArea.append(entry.getKey() + "\t" + entry.getValue().itemPrice + "\t" + entry.getValue().securityName + "\n");
                }
            }
            textArea.append("\n");


            textArea.setCaretPosition(textArea.getDocument().getLength());


        }
    }


    public static void main(String[] args) throws IOException {
        //Create and set up the window.
        JFrame frame = new JFrame("TextDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        StocksDB allowedUsers = new StocksDB("stocks.csv", "Symbol", "Security Name", "Price ");
        //System.out.println(HistorOfVariation.history);
        VisualServer server = new VisualServer(MainServer.BASE_PORT, allowedUsers);
        //Add contents to the window.
        frame.add(new Display(server));

        //Display the window.
        frame.pack();
        frame.setVisible(true);

        server.server_loop();
    }
}

	