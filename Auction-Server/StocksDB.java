import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

class HistorOfVariation {
    public static HashMap<String, ArrayList<HistorOfVariation>> history = new HashMap<String, ArrayList<HistorOfVariation>>();

    public String bidder;

    public double updatedPrice;
    public String time;

    HistorOfVariation(String name, double price, String time) {
        synchronized (this) {
            this.bidder = name;
            this.time = time;
            this.updatedPrice = price;
        }
    }

    public synchronized boolean checkey(String arg1) {
        if (HistorOfVariation.history.containsKey(arg1)) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized void addobj(HistorOfVariation obj, String symbol) {
        obj.history.get(symbol).add(obj);

    }



}


class PeopleDetails {

    public String name;
    public double price;
    public String symbol;

    public synchronized void setName(String Name) {

        this.name = Name;

    }

    public synchronized void setPrice(double Price) {

        this.price = Price;

    }

    public void setSymbol(String Symbol) {
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


class ItemDetails {
    public String securityName;
    public double itemPrice;

    public ItemDetails(String SecurityName, Double ItemPrice) {
        synchronized (this) {
            this.itemPrice = ItemPrice;
            this.securityName = SecurityName;
        }

    }
}


class StocksDB {

    public static Map<String, ItemDetails> stockList;

    private String[] fields;

    public static synchronized void updateStockList(String symbol, Double val){
        StocksDB.stockList.get(symbol).itemPrice=val;
    }
    public StocksDB(String cvsFile, String key, String val_1, String val_2) {
        FileReader fileRd = null;
        BufferedReader reader = null;

        try {
            fileRd = new FileReader(cvsFile);
            reader = new BufferedReader(fileRd);

            String header = reader.readLine();
            fields = header.split(",");// keep field names

            int keyIndex = findIndexOf(key);
            int val_1_Index = findIndexOf(val_1);
            int val_2_Index = findIndexOf(val_2);
            if (keyIndex == -1 || val_1_Index == -1 || val_2_Index == -1)
                throw new IOException("CVS file does not have data");

            stockList = new HashMap<String, ItemDetails>();
            String[] tokens;
            for (String line = reader.readLine();
                 line != null;
                 line = reader.readLine()) {
                tokens = line.split(",");
                stockList.put(tokens[keyIndex], new ItemDetails(tokens[val_1_Index], Double.parseDouble(tokens[val_2_Index])));
                HistorOfVariation.history.put(tokens[keyIndex], new ArrayList<>());
            }

            if (fileRd != null) fileRd.close();
            if (reader != null) reader.close();

            // I can catch more than one exceptions
        } catch (IOException e) {
            System.out.println(e);
            System.exit(-1);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Malformed CSV file");
            System.out.println(e);
        }
    }

    private int findIndexOf(String key) {
        for (int i = 0; i < fields.length; i++) {

            if (fields[i].equals(key)) {
                //System.out.println("hello"+" "+fields[i]+" "+key);
                return i;
            }
        }
        return -1;
    }

    public static void displayStock() {
        for (Map.Entry<String, ItemDetails> entry : stockList.entrySet()) {
            if (entry.getKey().equals("FB") || entry.getKey().equals("VRTU") || entry.getKey().equals("MSFT") || entry.getKey().equals("GOOGL") || entry.getKey().equals("YAHOO") || entry.getKey().equals("XLNX") || entry.getKey().equals("TSLA") || entry.getKey().equals("TXN")) {
                System.out.println(entry.getKey() + "\t" + entry.getValue().securityName + "\t" + entry.getValue().itemPrice);
            }
        }
    }

    // public interface 
    public ItemDetails findName(String key) {
        return stockList.get(key);
    }

    /*
    public static void main(String args[]){
    	StocksDB x= new StocksDB("stocks.csv","Symbol","Security Name","Price ");
    	System.out.println(stockList);

	}*/


}
	    