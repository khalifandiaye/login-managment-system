package de.uplinkgmbh.lms.utils;

import java.net.MalformedURLException;
import java.net.URL;

import de.axone.wash.client.WashClient;

public class MyWashClient extends WashClient {
		
		public MyWashClient() throws MalformedURLException {
			
			super( new URL( "http://localhost:8080/LMS/WashServices" ) );
		}

}
