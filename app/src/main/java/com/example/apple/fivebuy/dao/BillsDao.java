package com.example.apple.fivebuy.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table e_bills.
*/
public class BillsDao extends AbstractDao<Bills, Long> {

    public static final String TABLENAME = "e_bills";

    /**
     * Properties of entity Bills.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property B_id = new Property(0, Long.class, "b_id", true, "B_ID");
        public final static Property B_pid = new Property(1, Long.class, "b_pid", false, "B_PID");
        public final static Property B_add_date = new Property(2, String.class, "b_add_date", false, "B_ADD_DATE");
        public final static Property B_name = new Property(3, String.class, "b_name", false, "B_NAME");
        public final static Property B_type = new Property(4, String.class, "b_type", false, "B_TYPE");
        public final static Property B_num = new Property(5, String.class, "b_num", false, "B_NUM");
        public final static Property B_all = new Property(6, String.class, "b_all", false, "B_ALL");
        public final static Property B_describ = new Property(7, String.class, "b_describ", false, "B_DESCRIB");
    };


    public BillsDao(DaoConfig config) {
        super(config);
    }
    
    public BillsDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'e_bills' (" + //
                "'B_ID' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: b_id
                "'B_PID' INTEGER," + // 1: b_pid
                "'B_ADD_DATE' TEXT," + // 2: b_add_date
                "'B_NAME' TEXT," + // 3: b_name
                "'B_TYPE' TEXT," + // 4: b_type
                "'B_NUM' TEXT," + // 5: b_num
                "'B_ALL' TEXT," + // 6: b_all
                "'B_DESCRIB' TEXT);"); // 7: b_describ
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'e_bills'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Bills entity) {
        stmt.clearBindings();
 
        Long b_id = entity.getB_id();
        if (b_id != null) {
            stmt.bindLong(1, b_id);
        }
 
        Long b_pid = entity.getB_pid();
        if (b_pid != null) {
            stmt.bindLong(2, b_pid);
        }
 
        String b_add_date = entity.getB_add_date();
        if (b_add_date != null) {
            stmt.bindString(3, b_add_date);
        }
 
        String b_name = entity.getB_name();
        if (b_name != null) {
            stmt.bindString(4, b_name);
        }
 
        String b_type = entity.getB_type();
        if (b_type != null) {
            stmt.bindString(5, b_type);
        }
 
        String b_num = entity.getB_num();
        if (b_num != null) {
            stmt.bindString(6, b_num);
        }
 
        String b_all = entity.getB_all();
        if (b_all != null) {
            stmt.bindString(7, b_all);
        }
 
        String b_describ = entity.getB_describ();
        if (b_describ != null) {
            stmt.bindString(8, b_describ);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Bills readEntity(Cursor cursor, int offset) {
        Bills entity = new Bills( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // b_id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // b_pid
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // b_add_date
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // b_name
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // b_type
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // b_num
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // b_all
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // b_describ
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Bills entity, int offset) {
        entity.setB_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setB_pid(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setB_add_date(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setB_name(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setB_type(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setB_num(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setB_all(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setB_describ(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Bills entity, long rowId) {
        entity.setB_id(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Bills entity) {
        if(entity != null) {
            return entity.getB_id();
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
