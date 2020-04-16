package com.ripple.model;

import java.io.InputStream;
import java.util.Properties;

public class ConfigFactory {

	private String httpmainnet;
	private String httptestnet;
	private String wsmainnet;
	private String wstestnet;
	private String gnuplotfilepath;
	private String gnuplotexepath;
	
	ConfigFactory (String httpmainnet, String httptestnet, String wsmainnet, String wstestnet, String gnuplotfilepath, String gnuplotexepath ) {
		this.httpmainnet = httpmainnet;
		this.httptestnet = httptestnet;
		this.wsmainnet = wsmainnet;
		this.wstestnet = wstestnet;
		this.gnuplotfilepath = gnuplotfilepath;
		this.gnuplotexepath = gnuplotexepath;
	}
	
	//DAOFActory Singleton Design Pattern
	public static ConfigFactory getInstance() {
		ConfigFactory instance;
	    Properties prop = new Properties();

		try {
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				InputStream configFile = classLoader.getResourceAsStream("resources/config.properties");
				
				//load  properties file
				prop.load(configFile);
				
				//These attributes are loaded from the config file
				String httpmainnet = prop.getProperty("httpmainnet");
				String httptestnet = prop.getProperty("httptestnet");
				String wsmainnet = prop.getProperty("wsmainnet");
				String wstestnet = prop.getProperty("wstestnet");
				String gnuplotfilepath = prop.getProperty("gnuplotfilepath");
				String gnuplotexepath = prop.getProperty("gnuplotexepath");
				instance = new ConfigFactory(httpmainnet, httptestnet, wsmainnet, wstestnet, gnuplotfilepath, gnuplotexepath);
		} catch (Exception e) {
			instance = null;
			System.console().printf(e.getMessage());
		}
		return instance;
	}

	public String getHttpmainnet() {
		return httpmainnet;
	}

	public String getHttptestnet() {
		return httptestnet;
	}

	public String getWsmainnet() {
		return wsmainnet;
	}

	public String getWstestnet() {
		return wstestnet;
	}

	public String getGnuplotfilepath() {
		return gnuplotfilepath;
	}

	public String getGnuplotexepath() {
		return gnuplotexepath;
	}

}