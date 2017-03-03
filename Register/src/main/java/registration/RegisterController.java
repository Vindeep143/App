package registration;


import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Select.Where;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;

import registration.RegisterDTO;

import main.java.cassandra.CassandraCluster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

@Component
@RestController
@CrossOrigin(origins="*")
public class RegisterController {
	//@Autowired
	//private CassandraConnector client;
	
	private static CassandraCluster client;
	private Session session;
	 private static Cluster cluster;
	private String TABLE_NAME = "user_info";
	@RequestMapping(value="/register/do",method=RequestMethod.POST,consumes="application/json")
	public String register(@RequestBody RegisterDTO registerdto)
	{
		//client.setKeyspace("registration");

       
			client = new CassandraCluster();
            session = client.connect();
 
            PreparedStatement statement = session.prepare("INSERT INTO registration."+TABLE_NAME+" (id,name,password) VALUES (?,?,?);");
    		
    		BoundStatement bs = new BoundStatement(statement);
    		session.execute(bs.bind(registerdto.getId(),registerdto.getName(),registerdto.getPassword()));
    		return "Registered...";
 
        
        /*
		
		session = client.getSession();
		PreparedStatement statement = session.prepare("INSERT INTO "+client.getKeyspace()+"."+TABLE_NAME+" (id,name,password) VALUES (?,?,?);");
		
		BoundStatement bs = new BoundStatement(statement);
		session.execute(bs.bind(registerdto.getId(),registerdto.getName(),registerdto.getPassword()));
		return "Registered...";*/
	}
}
