package tweet;

import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {CassandraDataAutoConfiguration.class})
public class TweetApp {

    public static void main(String[] args) {
        SpringApplication.run(TweetApp.class, args);
    }
}
