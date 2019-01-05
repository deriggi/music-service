package com.wurrly.domain;

import org.jdbi.v3.core.mapper.Nested;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

public class Track{
    private Integer trackId;
    private String title;
    private String imagePath;
    private Integer wurrlyCount;

    @Nested("art")
    private Artist artist;

    /**
     * @return the id
     */
    @ColumnName("id")
    public Integer getTrackId() {
        return trackId;
    }

    /**
     * @return the artist
     */
    public Artist getArtist() {
        return artist;
    }

    /**
     * @param artist the artist to set
     */
    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    /**
     * @return the wurrlyPath
     */
    public Integer getWurrlyCount() {
        return wurrlyCount;
    }

    /**
     * @param wurrlyPath the wurrlyCount to set
     */
    public void setWurrlyCount(Integer wurrlyCount) {
        this.wurrlyCount = wurrlyCount;
    }

    /**
     * @return the imagePath
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * @param imagePath the imagePath to set
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @param id the id to set
     */
    public void setTrackId(Integer id) {
        this.trackId = id;
    }

    public String toString(){
        return this.trackId + " - " + this.title;
    }
    


}