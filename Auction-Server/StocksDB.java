// Group no 1. E/15/280, E/15/350

import java.io.*;
import java.util.*;

class HistorOfVariation {
    public static HashMap<String, ArrayList<HistorOfVariation>> history = new HashMap<String, ArrayList<HistorOfVariation>>(); // hash map, keys are stock symbols, value is an array list for each symbol

    public String bidder; // name of the client
    public double updatedPrice; // current price enterd
    public String time; // time @ he bids

    HistorOfVariation(String name, double price, String time) { // create history objects
        synchronized (this) {
            this.bidder = name;
            this.time = time;
            this.updatedPrice = price;
        }
    }

    public synchronized boolean checkey(String arg1) { // check if a given symbol included in the hash
        if (HistorOfVariation.history.containsKey(arg1)) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized void addobj(HistorOfVariation obj, String symbol) { // add a new history object to the array list
        obj.history.get(symbol).add(obj);
    }
}


class PeopleDetails { // this class uses synchronized methods to handle exceptions when entering multiple users

    public String name;
    public double price;
    public String symbol;

    public synchronized void setName(String Name) { // name of the current client
        this.name = Name;
    }

    public synchronized void setPrice(double Price) { // enterd price
        this.price = Price;
    }

    public void setSymbol(String Symbol) { // enterd symbol
        synchronized (this) {
            this.symbol = Symbol;
        }
    }

    public String getSymbol() {
        synchronized (this) {
            return symbol;
        }
    }

    public synchronized double getPrice() {
        return price;
    }

    public synchronized String getName() {
        return name;
    }
}


class ItemDetails { // used to create stock items
    public String securityName;
    public double itemPrice;

    public ItemDetails(String SecurityName, Double ItemPrice) { //create a stock item
        synchronized (this) {
            this.itemPrice = ItemPrice;
            this.securityName = SecurityName;
        }
    }
}

class StocksDB {

    public static Map<String, ItemDetails> stockList; // use hash map to create the stocklist. Keys are stock symbols and value is a object of itemdetails class
    private String[] fields;

    public static synchronized void updateStockList(String symbol, Double val) { // update the stocklist with given data
        StocksDB.stockList.get(symbol).itemPrice = val;
    }

    public StocksDB(String cvsFile, String key, String val_1, String val_2) { // create the stocklist using given csv file
        FileReader fileRd = null;
        BufferedReader reader = null;

        try {
            fileRd = new FileReader(cvsFile);
            reader = new BufferedReader(fileRd);

            String header = reader.readLine();
            fields = header.split(",");// keep field names

            int keyIndex = findIndexOf(key); // check whether the correct header included in the csv file
            int val_1_Index = findIndexOf(val_1);
            int val_2_Index = findIndexOf(val_2);
            if (keyIndex == -1 || val_1_Index == -1 || val_2_Index == -1) // if invalid header included in the csv file
                throw new IOException("CVS file does not have data");

            stockList = new HashMap<String, ItemDetails>(); // create the hashmap to the stocklist
            String[] tokens;
            for (String line = reader.readLine();
                 line != null;
                 line = reader.readLine()) {
                tokens = line.split(",");
                stockList.put(tokens[keyIndex], new ItemDetails(tokens[val_1_Index], Double.parseDouble(tokens[val_2_Index]))); // add values to the stocklist
                HistorOfVariation.history.put(tokens[keyIndex], new ArrayList<>()); // create keys of the history map
            }
            if (fileRd != null) fileRd.close();
            if (reader != null) reader.close();

        } catch (IOException e) {
            System.out.println(e);
            System.exit(-1);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Malformed CSV file");
            System.out.println(e);
        }
    }

    private int findIndexOf(String key) { // used to find given fileds inclued in the heading of the csv file
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].equals(key)) {
                return i;
            }
        }
        return -1;
    }
}
	    