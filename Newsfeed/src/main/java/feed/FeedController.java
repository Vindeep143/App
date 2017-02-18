package feed;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Select.Where;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
//import com.google.code.gson.Gson;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.*;
import feed.TweetDTO;

import java.io.IOException;

import main.java.cassandra.CassandraCluster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.stereotype.Component;


import java.time.LocalDate;
import java.time.LocalTime;

@Component
@RestController
@CrossOrigin(origins="*")
public class FeedController {

	private static CassandraCluster client;
	private Session session;
	 private static Cluster cluster;
	private String TABLE_NAME = "tweets";
	@RequestMapping("/feeds/do")
	public String register() throws IOException
	{
	 	ObjectMapper objectMapper = new ObjectMapper();
    	//Set pretty printing of json
    	objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
  
			client = new CassandraCluster();
            session = client.connect();
 

            PreparedStatement statement = session.prepare("SELECT * FROM tweet."+TABLE_NAME);
    		
    		BoundStatement bs = new BoundStatement(statement);
    		ResultSet rs = session.execute(bs);
    		List<Row> ls = rs.all();
    		List<TweetDTO> list=new ArrayList<TweetDTO>(ls.size());
    		for(Row rw : ls)
    		{
    			TweetDTO dto = new TweetDTO();
    			dto.setName(rw.getString("name"));
    			dto.setMessage(rw.getString("message"));
    			dto.setDate(rw.getString("date"));
    			dto.setTime(rw.getString("time"));
    			list.add(dto);
    		}
    		String array = objectMapper.writeValueAsString(list);
    		return array;
        
	}
	
}
