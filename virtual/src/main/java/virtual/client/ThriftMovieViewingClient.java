package virtual.client;

import fr.univangers.movieservice.thrift.MovieDto;
import fr.univangers.movieservice.thrift.MovieService;
import fr.univangers.movieservice.thrift.ServiceMovieNotFoundException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import virtual.dto.MovieViewingDto;
import virtual.exception.MovieViewingServiceException;

import java.util.Optional;

public class ThriftMovieViewingClient implements MovieViewingClient {

    private final String host;
    private final int port;
    private final int timeoutMs;

    public ThriftMovieViewingClient(String host, int port, int timeoutMs) {
        this.host = host;
        this.port = port;
        this.timeoutMs = timeoutMs;
    }

    @Override
    public Optional<MovieViewingDto> findMovieViewing(String title) {
        // Un client Thrift n'est pas thread-safe : une connexion par requete
        try (TTransport transport = new TSocket(host, port, timeoutMs)) {
            transport.open();
            MovieService.Client client = new MovieService.Client(new TBinaryProtocol(transport));
            MovieDto movie = client.findMovieByTitle(title);
            return Optional.of(new MovieViewingDto(movie.getVisualisationDate(), (int) movie.getPoints()));
        } catch (ServiceMovieNotFoundException e) {
            return Optional.empty();
        } catch (TException e) {
            throw new MovieViewingServiceException("Service Thrift injoignable sur " + host + ":" + port, e);
        }
    }
}
