package dduwcom.mobile.a20180971_final_project;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {MyInfo.class}, version = 1)
public abstract class MyInfoDB extends RoomDatabase {
    public abstract MyInfoDAO MyInfoDAO();

    private static volatile MyInfoDB INSTANCE;

    static MyInfoDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyInfoDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    MyInfoDB.class, "myinfo_db.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

