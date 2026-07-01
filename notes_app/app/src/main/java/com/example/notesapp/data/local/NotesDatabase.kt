package com.example.notesapp.data.local

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

//database configuration
@Database(entities = [NotesData::class], version = 1)   //Database contains NotesData table

/*version = 1 Database schema version.
in future when columns change -> version 1 → version 2 ->Migration required*/
abstract class NotesDatabase: RoomDatabase(){
    abstract fun NoteDao(): NoteDao

    companion object {  //call func without creating object

/*      Singleton instance store Purpose: ONE database object in whole app
     Why? Multiple database instances: memory waste -> corruption risk -> threading problems  */

        @Volatile private var INSTANCE: NotesDatabase? = null

        val migration = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase){
                db.execSQL("ALTER TABLE notes ADD COLUMN color INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE notes ADD COLUMN timestamp INTEGER NOT NULL DEFAULT 0")
                db.execSQL("UPDATE notes SET timestamp = strftime('%s', 'now') WHERE timestamp = 0")
            }
        }

        fun getDatabase(context: Context): NotesDatabase {//context location where db will create

            return INSTANCE ?: synchronized(this) {  //  If INSTANCE exists: return it
                // -> Else: create new database -> Thread locking ONE thread database create

                Room.databaseBuilder(
                    context.applicationContext,//Application level context.->
                    // Why not activity context? activity destroy memory leak -> Application context app lifetime stays

                    NotesDatabase::class.java,
                    "note_db"
                ).addMigrations(migration).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}





