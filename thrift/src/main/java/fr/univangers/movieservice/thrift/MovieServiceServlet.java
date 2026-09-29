package fr.univangers.movieservice.thrift;

import jakarta.servlet.annotation.WebServlet;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.server.TServlet;

//on utilise web.xml
//@WebServlet("/MovieService")
public class MovieServiceServlet extends TServlet {

    public MovieServiceServlet() {
        super(
                new MovieService.Processor<>(new MovieServiceThriftImpl()),
                new TJSONProtocol.Factory()
        );
    }
}