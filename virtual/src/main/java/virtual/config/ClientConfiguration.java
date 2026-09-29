package virtual.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tmdb.client.MovieInformationClient;
import tmdb.client.MovieInformationClientFactory;
import virtual.client.MovieViewingClient;
import virtual.client.ThriftMovieViewingClient;

// Fabrique des clients utilises par le service : c'est le seul endroit qui connait les implementations
@Configuration
public class ClientConfiguration {

    @Bean
    public MovieInformationClient movieInformationClient() {
        return MovieInformationClientFactory.getClient();
    }

    @Bean
    public MovieViewingClient movieViewingClient(@Value("${thrift.host}") String host,
                                                 @Value("${thrift.port}") int port,
                                                 @Value("${thrift.timeout-ms}") int timeoutMs) {
        return new ThriftMovieViewingClient(host, port, timeoutMs);
    }
}
