package com.wurrly;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.wurrly.dao.ArtistDao;
import com.wurrly.dao.TrackDao;
import com.wurrly.domain.Artist;
import com.wurrly.domain.Track;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("v1")
public class MusicResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String resourceHeartbeat() {
        return "ACK";
    }

    @Path("artist/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArtist(@PathParam("id") Integer id){
        Artist a = ArtistDao.get().getArtist(id);
        return  Response.status(200).
        entity(a).
        build();
    }
    
    @Path("artist")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateArtist(Artist artist){
        Boolean success = ArtistDao.get().updateArtist(artist);
        return  Response.status(200).
        entity(success).
        build();
    }

    @Path("artist/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteArtist(@PathParam("id") Integer id){
        ArtistDao.get().removeArtist(id);
        return  Response.status(200).
        build();
    }


    @Path("artist/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArtists(){
        return  Response.status(200).
        entity(ArtistDao.get().getAllArtists()).
        build();
    }

    @Path("track/eager/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTrackEagerly(@PathParam("id") Integer id){
        Track t = TrackDao.get().getTrackEager(id);
        return  Response.status(200).
        entity(t).
        build();
    }

    @Path("track/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTrack(@PathParam("id") Integer id){
        Track t = TrackDao.get().getTrack(id);
        return  Response.status(200).
        entity(t).
        build();
    }

    @Path("track/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTrack(@PathParam("id") Integer id){
        TrackDao.get().removeTrack(id);
        return  Response.status(200).
        build();
    }


    @Path("add/artist")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addArtist(Artist artist){
        ArtistDao.get().addArtist(artist);
        return  Response.status(200).
        entity("success").
        type("text/plain").
        build();
    }

    @Path("add/track")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addArtist(Track track){
        TrackDao.get().addTrack(track);
        return  Response.status(200).
        entity("success").
        type("text/plain").
        build();
    }
}
