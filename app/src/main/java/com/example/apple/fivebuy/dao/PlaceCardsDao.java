package com.example.apple.fivebuy.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table e_placecards.
*/
public class PlaceCardsDao extends AbstractDao<PlaceCards, Long> {

    public static final String TABLENAME = "e_placecards";

    /**
     * Properties of entity PlaceCards.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property S_id = new Property(0, Long.class, "s_id", true, "S_ID");
        public final static Property S_pid = new Property(1, Long.class, "s_pid", false, "S_PID");
        public final static Property S_add_date = new Property(2, String.class, "s_add_date", false, "S_ADD_DATE");
        public final static Property S_name = new Property(3, String.class, "s_name", false, "S_NAME");
        public final static Property S_address = new Property(4, String.class, "s_address", false, "S_ADDRESS");
        public final static Property S_describ = new Property(5, String.class, "s_describ", false, "S_DESCRIB");
    };


    public PlaceCardsDao(DaoConfig config) {
        super(config);
    }
    
    public PlaceCardsDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'e_placecards' (" + //
                "'S_ID' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: s_id
                "'S_PID' INTEGER," + // 1: s_pid
                "'S_ADD_DATE' TEXT," + // 2: s_add_date
                "'S_NAME' TEXT," + // 3: s_name
                "'S_ADDRESS' TEXT," + // 4: s_address
                "'S_DESCRIB' TEXT);"); // 5: s_describ
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'e_placecards'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, PlaceCards entity) {
        stmt.clearBindings();
 
        Long s_id = entity.getS_id();
        if (s_id != null) {
            stmt.bindLong(1, s_id);
        }
 
        Long s_pid = entity.getS_pid();
        if (s_pid != null) {
            stmt.bindLong(2, s_pid);
        }
 
        String s_add_date = entity.getS_add_date();
        if (s_add_date != null) {
            stmt.bindString(3, s_add_date);
        }
 
        String s_name = entity.getS_name();
        if (s_name != null) {
            stmt.bindString(4, s_name);
        }
 
        String s_address = entity.getS_address();
        if (s_address != null) {
            stmt.bindString(5, s_address);
        }
 
        String s_describ = entity.getS_describ();
        if (s_describ != null) {
            stmt.bindString(6, s_describ);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public PlaceCards readEntity(Cursor cursor, int offset) {
        PlaceCards entity = new PlaceCards( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // s_id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // s_pid
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // s_add_date
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // s_name
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // s_address
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // s_describ
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, PlaceCards entity, int offset) {
        entity.setS_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setS_pid(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setS_add_date(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setS_name(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setS_address(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setS_describ(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(PlaceCards entity, long rowId) {
        entity.setS_id(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(PlaceCards entity) {
        if(entity != null) {
            return entity.getS_id();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
