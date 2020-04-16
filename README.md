### Introduction:

The ripple application periodically calls rippled’s server_info command and records the sequence number of the latest validated ledger along with the current time. The retrieved information is then serialized into a "server_info.dat" file, which is then used to construct a plot using GNUPLOT. The application was programmed in Java using the Swing framework to provide a simple user friendly UI. The application supports both JSON-RPC requests over HTTP as well as WebSockets requests, and allows to select the frequency as well as the number of polling occurences as shown in the screenshot below:


<p align="center"><img src="/docImages/appUI.png" width="350" height="400"></p>

### Preqrequisites:

It is important to note that the package was only tested on a windows machine. Nevertheless, since it is a Java application, this should run on any platform as long as the two following prerequisites are met:

 - JRE needs to be installed on the computer
 - GNUPLOT executable is required to generate the plots
 
### Install and run:

It is very easy to install and run the application. It can either be run directly from any IDE supporting Java such as Eclipse, or by generating and running the JAR. As per the prerequisites, it is important to make sure that GNUPLOT is installed on the client's computer. Minor configuration is also required in the config.properties file located in the resource folder. This file holds the configuration of the JSON-RPC and WebSockets endpoints, as well the file path for both the GNUPLOt executable and the data file in which the polling results will be stored.

Once this is done, running the application is very simple and the user only needs to select the network they want to poll, their preferred polling protocol, the frequency and the number of measures. Then when clicking on "Start New Polling" the polling will start and the start button will turn grey (the rest of the UI will be frozen). There is no progress bar to track the progress as it would have caused delays to implement it. The only way to know that the polling has been completed is when the button turns back blue again.

### Architecture:

This application was implemented following a simplified version of the Model View Controller (MVC) design pattern. The below diagram attempts to summarize the technical architecture of the Java package and the various components:

<p align="center"><img src="/docImages/architecture.png" width="650" height="400"></p>

While the *view* layer only contains Swing panels and other user facing components, the *model* contains most of the logic. Specifically the HTTP Client  as well as the WebSocket Client objects responsible for establishing the communication between the app and the XRP ledger's public endpoints during the polling phases. The *model* also contains the logic to serialize the file as well as the Javaplot objects that are used to configure and build the plot with GNUPLOT. This is also where the logic for the  min, max and average features was implemented.

### Challenges:

##### JSON-RPC
As HTTP is a synchronous protocol, JSON RPC polling calls were pretty straight forward to implement. For this, I decided to use the native Java *HttpsURLConnection* object, which can be a bit slow but is easy to implement and is largely sufficient for small messages.

##### WebSockets
Implementing WeSocket messages however, was much more complex. Thisis  mainly due to the fact that WS messages are asynchronous and therefore require a "listening" thread to intercept messages produced by the server. Additional logic was therefore required to pass the received message back to the main thread so the content of each request could be serialized into the plot file.

##### Maths functions
As part of the bonus question, calculating the min, max and average were also challenging parts considering that the first and last transactions might not be complete. It was therefore necessary to remove them when calculating these figures in order to have more accurate calculations.

##### Polling interval:

The polling interval was made configurable in order to easily be able to compare the results. As new validaated legers take generally between 3 and 6 seconds, the polling intervals options are 1, 3 and 6 in the application. The frequencies 1 and 3 seem to offer better results when calculating the average/min/max as they are quick enough to mitigate the risk of missing a transaction.

### Questions:

### What do the results tell you?
The results suggest that time required to validate a new ledger can vary.

### What might explain the variation in time between new ledgers?
It is exaplained that in order to avoid double spend, transactions must be processed in a specific order. A consensus has to be reached by all the participants in order to ensure that this transaction order/grouping is agreed upon. As a consensus round can fail if no supermajority is clear from the received validation, the consensus round is "wasted" and a new round is required. This would result in losing a few seconds when generating that new ledger.

### Bonus question #1
See the code (Object GNUPlotBuilder, methods setAverage/setMin/SetMax)

### Bonus question #2
Both the ledger and server_state methods have a "closing_time" attribute. This attribute could be used and compared to the previous closure in order to determine how long it took to close.
