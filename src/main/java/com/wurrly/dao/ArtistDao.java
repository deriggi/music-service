package com.wurrly.dao;

import java.util.List;
import com.wurrly.db.DB;
import com.wurrly.domain.Artist;

import org.jdbi.v3.core.Handle;

public class ArtistDao{
    private static ArtistDao dao = new ArtistDao();

    public static ArtistDao get(){
        return dao;
    }


    public Boolean updateArtist(Artist artist){
        Boolean success = true;
        Handle h= null;
        
        try{
            h = DB.get().getHandle();
            h.createUpdate("UPDATE artist set name = :name where id = :id")
            .bind("id", artist.getId())
            .bind("name", artist.getName())
            .execute();
        } catch (Exception e){
            success = false;
            e.printStackTrace();
        } finally{
            h.close();
        }

        return success;
    }
    
    public Boolean addArtist(Artist artist){
        Boolean success = true;
        Handle h= null;
        
        try{
            h = DB.get().getHandle();
            h.createUpdate("INSERT INTO artist (id, name) VALUES (:id, :name)")
            .bind("id", artist.getId())
            .bind("name", artist.getName())
            .execute();
        } catch (Exception e){
            success = false;
            e.printStackTrace();
        } finally{
            h.close();
        }

        return success;
    }

    public Artist getArtist(Integer id){
        Handle h = null;
        List<Artist> listy = null;
        try{
            h = DB.get().getHandle();
            listy=  h.createQuery("SELECT ID, NAME FROM ARTIST WHERE ID = :id")
            .bind("id", id)
            .mapToBean(Artist.class)
            .list();

        } catch (Exception e){
            e.printStackTrace();
        } finally{
            h.close();
        }
        if(listy != null &&  listy.size()>0){
            return listy.get(0);
        }
        return null;
    }

    public List<Artist> getAllArtists(){
        Handle handle = DB.get().getHandle();
        List<Artist> artists = DB.get().getJdbi().withHandle(h->{
            return handle.createQuery("SELECT * FROM artist ORDER BY name")
            .mapToBean(Artist.class)
            .list();
        });
        handle.close();
        return artists;
    }

    public Boolean removeArtist(Integer id){
        if(id == null){
            return false;
        }
        Boolean success = true;
        Handle h= null;
        
        try{
            h = DB.get().getHandle();
            h.createUpdate("DELETE FROM artist WHERE ID = :id")
            .bind("id", id)
            .execute();
        } catch (Exception e){
            success = false;
        } finally{
            h.close();
        }

        return success;
    }


}