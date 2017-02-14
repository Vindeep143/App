package login;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Select.Where;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import java.util.List;
import login.LoginApp;

import main.java.cassandra.CassandraCluster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.stereotype.Component;

@Component
@RestController
@CrossOrigin(origins="*")
public class LoginController {

	
	private static CassandraCluster client;
	private Session session;
	// private static Cluster cluster;
	private String TABLE_NAME = "user_info";
	@RequestMapping(value="/login",method=RequestMethod.POST,consumes="application/json")
	public String register(@RequestBody LoginDTO registerdto)
	{
		

       
			client = new CassandraCluster();
            session = client.connect();
 
            PreparedStatement statement = session.prepare("SELECT * FROM registration."+TABLE_NAME+" WHERE id = ? ");
    		
    		BoundStatement bs = new BoundStatement(statement);
    		ResultSet rs = session.execute(bs.bind(registerdto.getId()));
    		if (rs == null || !isValid(rs,registerdto))
    			return "Invalid...";
    		else
    		{
    			
    			return "Valid";
    		}

	}
	@RequestMapping("/ll")
	public String ll()
	{
		return "working hdshgdshs";
	}
	private boolean isValid(ResultSet rs,LoginDTO login)
	{
		List<Row> row = rs.all();
		for(Row rw:row)
		{
			if(rw.getString("password").equals(login.getPassword()))
				return true;
		}
		return false;
	}
}
