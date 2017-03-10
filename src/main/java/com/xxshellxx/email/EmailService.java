package com.xxshellxx.email;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vassilischazapis on 15/07/15.
 */
@Service
public class EmailService {
//    private static final String API_PRIVATE_KEY="key-ea5952510dac9891554b2fdd85f9b856";
//    private static final String EMAIL_DOMAIN="app2a3def9fa2ce471c900a614d724aada7.mailgun.org";
	
	public final static String NOTIFICATIONS_FROM_ADDRESS = "notifications@xxshellxx.com";
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final Boolean enabled;
    private final String apiPrivateKey;
    private final String emailDomain;
    private final String internalNotificationsSendTo;
    
    public EmailService() {
    	this.enabled = "Y".equals(System.getenv("MAILGUN_NOTIFICATIONS_ENABLED_YN"));
		this.apiPrivateKey = System.getenv("MAILGUN_API_KEY");
		this.emailDomain = System.getenv("MAILGUN_DOMAIN");
		this.internalNotificationsSendTo = System.getenv("MAILGUN_INTERNAL_SEND_TO");
	}
    
    public boolean sendInternalNotification(String subject, String message) {
    	return sendMessage(NOTIFICATIONS_FROM_ADDRESS, internalNotificationsSendTo, subject, message);
    }

	public boolean sendMessage(String from, String to, String subject, String message) {
        ClientResponse response = sendSimpleMessage(from, to, subject, message);
        int status = response.getStatus(); // HTTP status code
        return status < 400;
    }

    public boolean sendMessage(String from, List<String> to, String subject, String message) {
        ClientResponse response = sendSimpleMessage(from, to, subject, message);
        int status = response.getStatus(); // HTTP status code
        return status < 400;
    }

    public ClientResponse sendSimpleMessage(String from, String to, String subject, String message) {
        ArrayList<String> toList = new ArrayList<>();
        toList.add(to);
        return sendSimpleMessage(from, toList, subject, message);
    }

    public ClientResponse sendSimpleMessage(String from, List<String> toList, String subject, String message) {
    	if (enabled) {
    		logger.debug(String.format("Sending email. From=%s, To=%s, subject=%s, message=%s", from, toList, subject, message));
	        Client client = Client.create();
	        client.addFilter(new HTTPBasicAuthFilter("api", apiPrivateKey));
	        WebResource webResource =
	                client.resource("https://api.mailgun.net/v3/" + emailDomain + "/messages");
	        MultivaluedMapImpl formData = new MultivaluedMapImpl();
	
	        formData.add("from", from);
	        formData.add("subject", subject);
	        formData.add("text", message);
	
	        for(String to : toList) {
	            formData.add("to", to);
	        }
	
	        return webResource.type(MediaType.APPLICATION_FORM_URLENCODED).
	                post(ClientResponse.class, formData);
    	} else {
    		logger.debug("Not sending notification email because notifications are not enabled.");
    		return new ClientResponse(201, null, null, null);
    	}
    }
}
