### Introduction

The ripple application periodically calls rippled’s server_info command and records the sequence number of the latest validated ledger along with the current time. The retrieved information is then serialized into a "server_info.dat" file, which is then used to construct a plot using GNUPLOT. The application was programmed in Java using the Swing framework to provide a simple user friendly UI. The application supports both JSON-RPC requests over HTTP and WebSockets, and allows to select the frequency as well as the number of polling occurences as shown in the screenshot below:


<p align="center"><img src="/docImages/appUI.png" width="300" height="400"></p>

### Architecture

This application uses different services to function. The below diagram attempts to summarize the technical architecture of this entire system including the app itself, as well as the various services it uses:




*This text will be italic*
_This will also be italic_




**This text will be bold**
__This will also be bold__




_You **can** combine them_




* Item 1
* Item 2
  * Item 2a
  * Item 2b
  

