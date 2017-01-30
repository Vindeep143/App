package cassandra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.convert.CassandraConverter;
import org.springframework.data.cassandra.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.stereotype.Component;
@Component
@PropertySource(value = { "classpath:cassandra.properties" })
//@EnableCassandraRepositories(basePackages = { "example" })
public class CassandraProperties extends AbstractCassandraConfiguration {
 
    @Autowired
    private Environment environment;
    
    private String contactpoints=environment.getProperty("cassandra.contactpoints");
    private int port=Integer.parseInt(environment.getProperty("cassandra.port"));
    private String keySpace=environment.getProperty("cassandra.keyspace");
 
    @Bean
    public CassandraClusterFactoryBean cluster() {
        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        cluster.setContactPoints(environment.getProperty("cassandra.contactpoints"));
        cluster.setPort(Integer.parseInt(environment.getProperty("cassandra.port")));
        return cluster;
    }
 
    @Override
    protected String getKeyspaceName() {
        return environment.getProperty("cassandra.keyspace");
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

	public String getKeyspace() {
		return keySpace;
	}

	public void setKeyspace(String keySpace) {
		this.keySpace = keySpace;
	}

	@Bean
    public CassandraMappingContext cassandraMapping() throws ClassNotFoundException {
        return new BasicCassandraMappingContext();
    }
}