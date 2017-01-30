package cassandra;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//import com.cisco.skyfall.vaultcore.service.VaultCoreServiceFactory;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;


/**
 * 
 * @author miahmad
 *
 */

@Component
public class CassandraConnector {
	@Autowired
	private static CassandraProperties prop;

//	@Autowired
//	private VaultCoreServiceFactory vaultCoreServiceFactory;
	
	@Autowired
	private CassandraProperties cassandraProperties;
	


	@PostConstruct
	public void init() {
		CassandraConnector.prop = cassandraProperties;
	}

	private static Cluster cluster;
	private static Session session;

	public CassandraConnector() {

	}

	public static void connect() {
		if (cluster == null) { // Single Checked
			synchronized (CassandraConnector.class) {
				if (cluster == null) { // Double checked
					if (cluster == null) {
						cluster = Cluster.builder().addContactPoint(prop.getContactpoints()).withPort(prop.getPort())
								.build();
						cluster.getConfiguration().getSocketOptions().setReadTimeoutMillis(2000);
					}
					if (session == null) {
						session = cluster.connect(prop.getKeyspace());
					}
				}
			}
		}
	}

	public Session getSession() {
		connect();
		return session;
	}
	public void setKeyspace(String name)
	{
		prop.setKeyspace(name);
	}
	public String getKeyspace() {
		return prop.getKeyspaceName();
	}

	@PreDestroy
	public void destroy() {
		//logger.info("******************  CLOSING CASSANDRA CLUSTER REFERENCE ****************************");
		cluster.close();
	}
}
