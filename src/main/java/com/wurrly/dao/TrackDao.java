package com.wurrly.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wurrly.db.*;
import com.wurrly.domain.Track;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.mapper.reflect.FieldMapper;
import org.jdbi.v3.core.statement.Query;

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
        List<Track> listy = new ArrayList<>();
        try{
            h = DB.get().getHandle();
            Query q = h.createQuery("SELECT track_id, title,image_path, wurrly_count, artist_id  FROM track WHERE track_ID = :id")
            .bind("id", id);

            List<Map<String, Object>> list = q.mapToMap().list();
            for(Map<String,Object> map: list){
                Track t  = new Track();
                t.setTrackId((Integer)map.get("track_id"));
                t.setTitle((String)map.get("title"));
                t.setImagePath((String)map.get("image_path"));
                t.setWurrlyCount((Integer)map.get("wurrly_count"));
                listy.add(t);
            }

        } catch (Exception e){
            e.printStackTrace();
            listy = new ArrayList<>(0);
            
        } finally{
            h.close();
        }
        Track returnTrack = null;
        if(listy != null &&  listy.size()>0){
            returnTrack = listy.get(0);
        }
        return returnTrack;
    }

    public List<Track> getAllTracks(){
        Handle h = null;
        List<Track> listy = new ArrayList<>();
        try{
            h = DB.get().getHandle();
            // listy=  
            Query q = h.createQuery("SELECT track_id, title,image_path, wurrly_count, artist_id  FROM track ");
            List<Map<String, Object>> list = q.mapToMap().list();
            for(Map<String,Object> map: list){
                Track t  = new Track();
                t.setTrackId((Integer)map.get("track_id"));
                t.setTitle((String)map.get("title"));
                t.setImagePath((String)map.get("image_path"));
                t.setWurrlyCount((Integer)map.get("wurrly_count"));
                listy.add(t);
            }
            // .mapTo(Track.class)
            // .mapToBean(Track.class)
            // .list();

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