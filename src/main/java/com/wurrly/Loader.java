package com.wurrly;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wurrly.dao.ArtistDao;
import com.wurrly.dao.TrackDao;
import com.wurrly.domain.Artist;
import com.wurrly.domain.Track;

public class Loader {

    public static void main(String[] args) {
        gatherInput();
    }

    /**
     * A data loader for Wurrly data from the Json resource
     */
    public static void gatherInput() {
        Gson gson = new Gson();
        try {

            // setup a connection
            URL url = new URL("https://s3-us-west-2.amazonaws.com/wurrly-data/test/songs.json");
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            // read the input
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            // parse the json
            JsonParser parser = new JsonParser();
            JsonObject entireObject = parser.parse(content.toString()).getAsJsonObject();
            JsonArray allTracks = entireObject.get("items").getAsJsonArray();

            // add artists and tracks to the db
            ArtistDao artistDao = ArtistDao.get();
            TrackDao trackDao = TrackDao.get();
            for (JsonElement track : allTracks) {
                Artist a1 = gson.fromJson(track.getAsJsonObject().get("artist"), Artist.class);
                if (artistDao.getArtist(a1.getId()) == null) {
                    artistDao.addArtist(a1);
                }
                Track trackObj = gson.fromJson(track, Track.class);
                trackObj.setTrackId(track.getAsJsonObject().get("id").getAsInt());
                trackDao.addTrack(trackObj);
            }

            // print out lists sizes from the DB to see what we have
            System.out.println("Artists size is " + artistDao.getAllArtists().size());
            System.out.println("Track size is " + trackDao.getAllTracks().size());

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

}
