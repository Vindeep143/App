package feed;

import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {CassandraDataAutoConfiguration.class})
public class FeedApp {

    public static void main(String[] args) {
        SpringApplication.run(FeedApp.class, args);
    }
}
