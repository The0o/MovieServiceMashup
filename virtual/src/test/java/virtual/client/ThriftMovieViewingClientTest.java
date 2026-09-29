package virtual.client;

import fr.univangers.movieservice.thrift.MovieDto;
import fr.univangers.movieservice.thrift.MovieService;
import fr.univangers.movieservice.thrift.ServiceMovieNotFoundException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import virtual.dto.MovieViewingDto;
import virtual.exception.MovieViewingServiceException;

import java.net.ServerSocket;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ThriftMovieViewingClientTest {

    // Faux serveur Thrift qui respecte l'IDL du module thrift
    private static class FakeMovieService implements MovieService.Iface {
        @Override
        public void addMovie(MovieDto movie) {
        }

        @Override
        public MovieDto findMovieByTitle(String title) throws ServiceMovieNotFoundException {
            if (title.equals("The Matrix")) {
                return new MovieDto("The Matrix", (short) 1999, "2025-03-01", (short) 8);
            }
            throw new ServiceMovieNotFoundException("introuvable");
        }

        @Override
        public List<MovieDto> findMoviesByYear(short year) {
            return List.of();
        }
    }

    private TServer server;
    private int port;

    @BeforeEach
    void startServer() throws Exception {
        TServerSocket socket = new TServerSocket(new ServerSocket(0));
        port = socket.getServerSocket().getLocalPort();
        server = new TSimpleServer(new TServer.Args(socket).processor(new MovieService.Processor<>(new FakeMovieService())));
        new Thread(server::serve).start();
        while (!server.isServing()) {
            Thread.sleep(10);
        }
    }

    @AfterEach
    void stopServer() {
        server.stop();
    }

    @Test
    void returnsViewingWhenMovieExists() {
        Optional<MovieViewingDto> viewing = new ThriftMovieViewingClient("localhost", port, 2000).findMovieViewing("The Matrix");

        assertTrue(viewing.isPresent());
        assertEquals("2025-03-01", viewing.get().getVisualisationDate());
        assertEquals(8, viewing.get().getPoints());
    }

    @Test
    void returnsEmptyWhenMovieNotSeen() {
        assertTrue(new ThriftMovieViewingClient("localhost", port, 2000).findMovieViewing("Inconnu").isEmpty());
    }

    @Test
    void throwsWhenServerIsDown() throws Exception {
        int freePort;
        try (ServerSocket s = new ServerSocket(0)) {
            freePort = s.getLocalPort();
        }
        assertThrows(MovieViewingServiceException.class,
                () -> new ThriftMovieViewingClient("localhost", freePort, 2000).findMovieViewing("The Matrix"));
    }
}
