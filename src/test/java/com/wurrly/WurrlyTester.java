package com.wurrly;

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
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.List;

public class WurrlyTester {

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        // start the server
        server = Main.startServer();
        // create the client
        Client c = ClientBuilder.newClient();

        // uncomment the following line if you want to enable
        // support for JSON in the client (you also have to uncomment
        // dependency on jersey-media-json module in pom.xml and Main.startServer())
        // --
        // c.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());

        target = c.target(Main.BASE_URI);

        
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Test
    public void testGetIt() {
        String responseMsg = target.path("v1").request().get(String.class);
        assertEquals("ACK", responseMsg);
    }

    @Test
    public void testArtistDao(){

        Artist jamesBrown = new Artist();
        jamesBrown.setId(3);
        jamesBrown.setName("James Brown");

        Artist yoyoMa = new Artist();
        yoyoMa.setId(4);
        yoyoMa.setName("yoyoma");

        ArtistDao.get().addArtist(jamesBrown);
        ArtistDao.get().addArtist(yoyoMa);

        // remove clarice
        ArtistDao.get().removeArtist(3);
        assertEquals( 1 , ArtistDao.get().getAllArtists().size() );

        ClientConfig clientConfig = new ClientConfig();
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget webTarget = client.target(Main.BASE_URI);

        WebTarget getArtistsTarget = webTarget.path("v1/artist/list");
        Invocation.Builder artistInvokeBuilder = getArtistsTarget.request(MediaType.APPLICATION_JSON_TYPE);
        List<Artist> artists = artistInvokeBuilder.get(List.class);
        
        assertEquals(artists.size(), 1);

        WebTarget updateArtistTarget = webTarget.path("v1/artist");
        Invocation.Builder updateInvoker = updateArtistTarget.request(MediaType.APPLICATION_JSON_TYPE);
        
        yoyoMa.setId(4);
        yoyoMa.setName("yoyoma");
        yoyoMa.setName("Yo Yo Ma");
        Response updateResponse = updateInvoker.put(Entity.entity(yoyoMa, MediaType.APPLICATION_JSON_TYPE));
        assert(updateResponse.readEntity(Boolean.class));

        WebTarget getUpdatedArtistTarget = webTarget.path("v1/artist/4");
        Invocation.Builder getInvoker = getUpdatedArtistTarget.request(MediaType.APPLICATION_JSON_TYPE);
        Response getResponse = getInvoker.get();
        Artist s= getResponse.readEntity(Artist.class);
        System.out.println(s.getName());
        assertEquals(s.getName(), "Yo Yo Ma");

    }

    @Test
    public void testTracks(){
        
       
    }

    
    public void testAll(){
        DB db = DB.get();
        Jdbi jdbi = db.getJdbi();
        Handle handle = db.getHandle();
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
 
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget webTarget = client.target(Main.BASE_URI);
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
