package fr.univangers.movieservice.thrift;

import jakarta.servlet.annotation.WebServlet;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.server.TServlet;

/**
 * Servlet HTTP permettant d'exposer le service Thrift des films sur le réseau.
 * Cette classe gère la réception des requêtes HTTP et utilise le format JSON
 * pour la sérialisation et désérialisation des données.
 */
//on utilise web.xml
//@WebServlet("/MovieService")
public class MovieServiceServlet extends TServlet {

    /**
     * Initialise la servlet en configurant le processeur métier et le protocole de communication.
     * Le processeur est lié à l'implémentation concrète du service (MovieServiceThriftImpl)
     * et le protocole est défini sur JSON.
     */
    public MovieServiceServlet() {
        super(
                new MovieService.Processor<>(new MovieServiceThriftImpl()),
                new TJSONProtocol.Factory()
        );
    }
}