package com.wurrly.db;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

public class DB {

    private static DB db = new DB();
    private Jdbi jdbi = null;

    static {
        db.initJDBI();
    }

    public static DB get() {
        return db;
    }

    private void createTables() {
        Handle handle = jdbi.open();
        try {
            jdbi.useHandle(h -> {
                handle.execute("CREATE TABLE artist (id INTEGER PRIMARY KEY, name VARCHAR)");
                handle.execute(
                        "CREATE TABLE track (track_id INTEGER PRIMARY KEY, title VARCHAR, artist_id INTEGER, image_path VARCHAR, wurrly_count INTEGER,  foreign key (artist_id) references artist(id) )");

                System.out.println("created tables artist and track");
            });
        } finally {
            handle.close();
        }
    }

    private Jdbi initJDBI() {
        if (jdbi == null) {
            jdbi = Jdbi.create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
            createTables();
        }
        return jdbi;
    }

    public Jdbi getJdbi() {
        return this.jdbi;
    }

    public Handle getHandle() {
        return jdbi.open();
    }

    public void closeHandle(Handle h) {
        if (h != null) {
            h.close();
        }
    }



}