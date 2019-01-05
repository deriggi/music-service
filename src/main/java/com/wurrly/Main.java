package com.wurrly;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.wurrly.dao.ArtistDao;
import com.wurrly.dao.TrackDao;
import com.wurrly.db.DB;
import com.wurrly.domain.Artist;
import com.wurrly.domain.Track;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/music/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this
     * application.
     * 
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in com.example package
        final ResourceConfig rc = new ResourceConfig().packages("com.wurrly");

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        
        Loader.gatherInput();
        System.in.read();

        
        server.shutdownNow();


    }

    

    public static void jdbc(){
        DB db = DB.get();
        Jdbi jdbi = db.getJdbi();
        Handle handle = db.getHandle();

        // jdbi.useHandle(h->{
        //     handle.execute("CREATE TABLE artist (id INTEGER PRIMARY KEY, name VARCHAR)");
        //     handle.execute("CREATE TABLE track (id INTEGER PRIMARY KEY, title VARCHAR, artist_id INTEGER, foreign key (artist_id) references artist(id) )");
            
        //     // Inline positional parameters
            
        //     // handle.execute("INSERT INTO user(id, name) VALUES (?, ?)", 0, "Alice");
        //     System.out.println("done adding");
        // });

        

        // handle.close();
        Artist rick = new Artist();
        rick.setName("Rick");
        rick.setId(2);

        Artist clarice = new Artist();
        clarice.setId(3);
        clarice.setName("Clarice");

        ArtistDao.get().addArtist(clarice);
        ArtistDao.get().addArtist(rick);

        // remove clarice
        ArtistDao.get().removeArtist(3);

        Track track = new Track();
        track.setImagePath("some path");
        track.setTitle("Stanky leg");
        track.setTrackId(1);
        track.setWurrlyCount(2);
        track.setArtist(rick);
        TrackDao.get().addTrack(track);
        
        // can we get it back?
        Track tout = TrackDao.get().getTrack(1);
        System.out.println(tout.getTitle() + " is the title out");
        Handle handle2  = jdbi.open();
        

        ClientConfig clientConfig = new ClientConfig();
        // clientConfig.getProperties().
        // clientConfig.getProperties().put(
        // JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
 
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget webTarget = client.target(BASE_URI);
        WebTarget employeeWebTarget = webTarget.path("v1");
        Invocation.Builder invocationBuilder = employeeWebTarget.request(MediaType.TEXT_PLAIN);
        Response res = invocationBuilder.get();
        System.out.println("from the server we got " + res.getStatus() + " " + res.readEntity(String.class));
        
        WebTarget addArtist = webTarget.path("v1/add/artist");
        Invocation.Builder artistInvokeBuilder = addArtist.request(MediaType.APPLICATION_JSON_TYPE);
        Response postResponse = artistInvokeBuilder.post(Entity.entity(clarice, MediaType.APPLICATION_JSON_TYPE));
        System.out.println("from the server we got " + postResponse.getStatus() + " " + postResponse.readEntity(String.class));
        
        Artist katy = new Artist();
        katy.setId(4);
        katy.setName("Katy");
        Response postResponse2 = artistInvokeBuilder.post(Entity.entity(katy, MediaType.APPLICATION_JSON_TYPE));

        System.out.println("from the server we got " + postResponse2.getStatus() + " " + postResponse2.readEntity(String.class));
        

        Track fireWorks  =new Track();
        fireWorks.setArtist(katy);
        fireWorks.setTitle("fireworks");
        fireWorks.setTrackId(20);
        fireWorks.setImagePath("https://cdn1.wurrly.co/ea/79/bc/f1/cf2030bc46.jpg");
        fireWorks.setWurrlyCount(3);
        WebTarget adddTrack = webTarget.path("v1/add/track");
        Invocation.Builder trackInvokeBuilder = adddTrack.request(MediaType.APPLICATION_JSON_TYPE);
        Response trackPostResponse = trackInvokeBuilder.post(Entity.entity(fireWorks, MediaType.APPLICATION_JSON_TYPE));
        System.out.println("from the server we got " + trackPostResponse.getStatus() + " " + trackPostResponse.readEntity(String.class));
        

        List<Artist> artists = jdbi.withHandle(h->{
            // handle2.createUpdate("INSERT INTO artist (id, name) VALUES (:id, :name)")
            // .bind("id", 2)
            // .bind("name", "Clarice")
            // .execute();
            // Inline positional parameters
            // handle.execute("INSERT INTO user(id, name) VALUES (?, ?)", 0, "Alice");
            return handle2.createQuery("SELECT * FROM artist ORDER BY name")
            .mapToBean(Artist.class)
            .list();
        });
        System.out.println(artists.size() + " artists");

        System.out.println(artists.get(0).getName() + " is one artist's name");
        
        
        System.out.println(ArtistDao.get().getArtist(2).getName() + " is id 2");

        handle.close();

    }
}

