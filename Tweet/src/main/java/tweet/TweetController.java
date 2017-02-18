package tweet;

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

import tweet.TweetDTO;

import main.java.cassandra.CassandraCluster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
@RestController
public class TweetController {

	private static CassandraCluster client;
	private Session session;
	 private static Cluster cluster;
	private String TABLE_NAME = "tweets";
	@RequestMapping(value="/tweet/do",method=RequestMethod.POST,consumes="application/json")
	public String register(@RequestBody TweetDTO tweetdto)
	{
  
			client = new CassandraCluster();
            session = client.connect();
 
            PreparedStatement statement = session.prepare("INSERT INTO tweet."+TABLE_NAME+" (name,message,date,time) VALUES (?,?,?,?);");
    		
    		BoundStatement bs = new BoundStatement(statement);
    		session.execute(bs.bind(tweetdto.getName(),tweetdto.getMessage(),LocalDate.now().toString(),LocalTime.now().toString()));
    		return "Tweet Successful";      
        
	}
	
}
