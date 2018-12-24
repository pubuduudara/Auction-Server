Instructions for the Server
---------------------------

 01) How to start the server

 		* Compile all the .java files.
 		* Run the compiled file of Display.java.

 02) GUI controls

 		* The GUI displays the current stock prices of TSLA, MSFT, VRTU, TXN, XLNX, FB and GOOGL.
 		* Also GUI can be used to search for a bidding history of any given stock during runtime.
 		* To do that the Server user should enter the stock's symbol name and click 'Enter'.
 		* GUI can display, time of the bids, clients's name and stock symbol that they bid on. The history is sorted according to the time they enter the bid.
 		* The price of stocks changes to the highest bid that a client entered during runtime.

 Instrucitons for the Clients
 ----------------------------

 01) How to connect to the auction server

 		* The client can use the command line to connect to the server.
 		* To connect type 'nc <SERVER IP ADRESS> 2000' in the command line and press enter.

 02) To start bidding

 		* As the first input enter the Client's name.
 		* Then enter the symbol of the stock that client intends to bid on.
 		* Only if a client enter a valid symbol he/she can start bidding.
 		* If the client succesfully connected to the server the command line will display the current highest bid of the corresponding stock.
 		* You can only enter valid bids (integers or floats).
 		* The stock price will change only if the client's bid is larger than the stock's current price.
