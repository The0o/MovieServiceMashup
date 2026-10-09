package fr.univangers.virtual.client;

import fr.univangers.movieservice.thrift.MovieDto;
import fr.univangers.movieservice.thrift.MovieService;
import fr.univangers.movieservice.thrift.ServiceMovieNotFoundException;
import fr.univangers.virtual.exception.MovieCollectionUnavailableException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import java.util.Optional;

public class ThriftMovieCollectionClient implements MovieCollectionClient {

    private final String serviceUrl;

    public ThriftMovieCollectionClient(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    @Override
    public Optional<MovieDto> findMovieByTitle(String title) {
        THttpClient transport = null;
        try {
            transport = new THttpClient(serviceUrl);
            transport.open();
            TProtocol protocol = new TJSONProtocol(transport);
            MovieService.Client client = new MovieService.Client(protocol);
            return Optional.of(client.findMovieByTitle(title));
        } catch (ServiceMovieNotFoundException e) {
            return Optional.empty();
        } catch (TException e) {
            throw new MovieCollectionUnavailableException("Service Thrift injoignable (" + serviceUrl + ")", e);
        } finally {
            if (transport != null) {
                transport.close();
            }
        }
    }
}
