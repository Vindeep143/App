package main.java.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import java.util.Map;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = { "classpath:cassandra.properties" })
public class CassandraCluster {

	
	private Session session;
	 private static Cluster cluster;
	 
	 private String contactpoints="172.17.0.2";
	    private int port=Integer.parseInt("9042");
	    private String keySpace="registration";
	 
	 public Session connect()
	 {
		 try {
			 Map<String, String> env = System.getenv();
			 contactpoints = env.get("CASSANDRA_HOST");
			 port = Integer.parseInt(env.get("CASSANDRA_PORT"));
			 
			 
	            cluster = Cluster.builder().addContactPoint(contactpoints).build();
	 
	            session = cluster.connect(keySpace);
	            return session;
		 }
		 catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            
	        }
		 return null;
	 }

	public String getContactpoints() {
		return contactpoints;
	}

	public void setContactpoints(String contactpoints) {
		this.contactpoints = contactpoints;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getKeySpace() {
		return keySpace;
	}

	public void setKeySpace(String keySpace) {
		this.keySpace = keySpace;
	}
	
}
