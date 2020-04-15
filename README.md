### Introduction

The ripple application periodically calls rippled’s server_info command and records the sequence number of the latest validated ledger along with the current time. The retrieved information is then serialized into a "server_info.dat" file, which is then used to construct a plot using GNUPLOT. The application was programmed in Java using the Swing framework to provide a simple user friendly UI. The application supports both JSON-RPC requests over HTTP and WebSockets, and allows to select the frequency as well as the number of polling occurences as shown in the screenshot below:


<p align="center"><img src="/docImages/appUI.png" width="300" height="400"></p>

### Architecture

This application was implemented following a simplified version of the Model View Controller (MVC) design pattern. The below diagram attempts to summarize the technical architecture of the Java package and the various components:

<p align="center"><img src="/docImages/architecture.png" width="650" height="400"></p>

While the *view* layer only contains Swing panels and other user facing components, the *model* contains most of the logic. Specifically the HTTP Client  as well as the WebSocket Client objects responsible for establishing the communication between the app and the XRP ledger's public endpoints during the polling phases. The *model* also contains the logic to serialize the file as well as the Javaplot objects that are used to configure and build the plot with GNUPLOT. This is also where the logic for the  min, max and average features was implemented.

### Challenges

##### JSON-RPC
As HTTP is a synchronous protocol, JSON RPC polling calls were pretty straight forward to implement. For this, I decided to use the native Java *HttpsURLConnection* object, which can be a bit slow but is easy to implement and is largely sufficient for small messages.

##### WebSockets
Implementing WeSocket messages however, was much more complex. Thisis  mainly due to the fact that WS messages are asynchronous and therefore require a "listening" thread to intercept messages produced by the server. Additional logic was therefore required to pass the received message back to the main thread so the content of each request could be serialized into the plot file.

##### Maths functions
As part of the bonus question, calculating the min, max and average were also challenging parts considering that the first and last transactions might not be complete. It was therefore necessary to remove them when calculating these figures in order to have more accurate calculations.
