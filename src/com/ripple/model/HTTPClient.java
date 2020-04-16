package com.ripple.model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.ripple.beans.ServerInfo;

public class HTTPClient {
		
	private String url;
	
	public HTTPClient (String url) {
		this.url = url;
	}
	
	public List <ServerInfo> pollHTTP (String message, Integer frequency, Integer numOccurrences) throws ParseException, IOException {
	  	ServerInfo si;
	  	//Define the JSON-RPC pay load
	  	String httpRequestParams = message;
	  	List <ServerInfo> siList = new ArrayList<ServerInfo>();		  
	  	URL server_url= new URL(url);
				
		//prepare the body of the HTTP request
		byte[] postData = httpRequestParams.getBytes(StandardCharsets.UTF_8);
		
		//Polling loop that will break after reaching the number of iterations
		for (int i=0; i<numOccurrences; i++) {
			//Using JAVA native URLConnection object to initiate the HTTP request
			HttpsURLConnection connection = (HttpsURLConnection) server_url.openConnection(); 
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			
			//Add httpHeaders to the HTTP request
			connection.setRequestProperty("Content-Type", "application/json");
			try( DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
			   wr.write(postData);
			}
			
			//Try to get the API JSON response
			try {
				si = convertHTTPResponseToServerInfo(connection.getInputStream());
				siList.add(si);
				
			    //Wait for the frequency selected in the ComboBox
			    Thread.sleep(frequency);
			} catch (IOException | InterruptedException e){
				System.out.println(e.getMessage());
			}
			
			//Force the disconnection
			connection.disconnect();
        }
		
		return siList;
	}
  
    //This method converts the HTTP JSON response into a ServerInfo object
	private ServerInfo convertHTTPResponseToServerInfo(InputStream response) throws IOException {
		//Initiate the reader buffer to read the JSON response
		BufferedReader reader = new BufferedReader(new InputStreamReader(response));
		
		//For each line, concatenate into a single String
		StringBuilder result = new StringBuilder();
		String line;
		while((line = reader.readLine()) != null) {
		    result.append(line);
		}
		
		//Serialise the JSON response into the ServerInfo objects using GSon library
		Gson json = new Gson();
		ServerInfo si = json.fromJson(result.toString(), ServerInfo.class);
		
		return si;
	}
}
