# This is an <h1> tag
## This is an <h2> tag
###### This is an <h6> tag




*This text will be italic*
_This will also be italic_




**This text will be bold**
__This will also be bold__




_You **can** combine them_




* Item 1
* Item 2
  * Item 2a
  * Item 2b
  
  
  
  ![GitHub Logo](/images/logo.png)
Format: ![Alt Text](url)
  


![GitHub Logo](/images/logo.png)



http://github.com - automatic!
[GitHub](http://github.com)


###### Introduction

The ripple application periodically calls rippled’s server_info command and records the sequence number of the latest validated ledger along with the current time. The retrieved information is then serialized into a "server_info.dat" file, which is then used to construct a plot using GNUPLOT. The application was programmed in Java using the Swing framework to provide a simple user friendly UI. The application supports both JSON-RPC requests over HTTP and WebSockets, and allow to select the frequency as well as the number of polling occurences.




periodically calls rippled’s server_info command and records the sequence number of the latest validated ledger along with the current time. Record this data in a file. Then, use this data to construct a plot (time on the x-axis, sequence number on the y-axis) that visualizes how frequently the ledger sequence is incremented over time (i.e. how often new ledgers are validated). Choose a time span and polling interval that can effectively capture and depict this information.
