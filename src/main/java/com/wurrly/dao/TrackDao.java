package com.wurrly.dao;

import java.util.ArrayList;
import java.util.List;
import com.wurrly.db.*;
import com.wurrly.domain.Track;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.mapper.reflect.FieldMapper;

public class TrackDao{
    private static TrackDao dao = new TrackDao();

    public static TrackDao get(){
        return dao;
    }

    public Boolean addTrack(Track track){
        Boolean success = true;
        Handle h= null;
        
        try{
            h = DB.get().getHandle();
            h.createUpdate("INSERT INTO track (track_id, title,image_path, wurrly_count, artist_id) VALUES (:id, :title, :imagePath, :wurrlyCount, :artistId)")
            .bind("id", track.getTrackId())
            .bind("title", track.getTitle())
            .bind("imagePath", track.getImagePath())
            .bind("wurrlyCount", track.getWurrlyCount())
            .bind("artistId", track.getArtist().getId())
            .execute();

        } catch (Exception e){
            success = false;
            e.printStackTrace();
        } finally{
            h.close();
        }

        return success;
    }

    public Track getTrack(Integer id){
        Handle h = null;
        List<Track> listy = null;
        try{
            h = DB.get().getHandle();
            listy=  h.createQuery("SELECT track_id, title,image_path, wurrly_count, artist_id  FROM track WHERE track_ID = :id")
            .bind("id", id)
            .mapToBean(Track.class)
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

    public List<Track> getAllTracks(){
        Handle h = null;
        List<Track> listy = null;
        try{
            h = DB.get().getHandle();
            listy=  h.createQuery("SELECT track_id, title,image_path, wurrly_count, artist_id  FROM track ")
            
            .mapToBean(Track.class)
            .list();

        } catch (Exception e){
            e.printStackTrace();
            listy = new ArrayList<>(0);
        } finally{
            h.close();
        }
            return listy;
    }

    public Track getTrackEager(Integer id){
        Handle h = null;
        List<Track> listy = null;
        try{
            h = DB.get().getHandle();
            h.registerRowMapper(FieldMapper.factory(Track.class));
            listy=  h.createQuery("SELECT artist.name art_name, artist.id art_id, track_id,  title,image_path, wurrly_count, artist_id FROM track inner join artist on artist_id = artist.id  WHERE track_id = :id")
            .bind("id", id)
            .mapTo(Track.class)
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

    

    public Boolean removeTrack(Integer id){
        if(id == null){
            return false;
        }
        Boolean success = true;
        Handle h= null;
        
        try{
            h = DB.get().getHandle();
            h.createUpdate("DELETE FROM Track WHERE track_id = :id")
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