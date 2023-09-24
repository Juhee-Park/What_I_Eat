package dduwcom.mobile.a20180971_final_project;

import android.content.Context;

import androidx.room.*;
import androidx.room.RoomDatabase;

@Database(entities = {Meal.class}, version = 1)
public abstract class MealDB extends RoomDatabase {
    public abstract MealDAO mealDAO();

    private static volatile MealDB INSTANCE;

    static MealDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MealDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    MealDB.class, "meal_db.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

