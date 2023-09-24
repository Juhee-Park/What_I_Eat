package dduwcom.mobile.a20180971_final_project;

import androidx.room.*;

import java.util.List;

@Dao
public interface MealDAO {
    @Insert
    void insertMeal(Meal meal);

    @Update
    void updateMeal(Meal meal);

    @Delete
    void deleteMeal(Meal meal);

    //오늘 칼로리 총합 구하기. 메인, 그래프에 사용
    @Query("SELECT SUM(calorie) FROM meal_table WHERE today = :today")
    Double getTodaysTotalCal(String today);

    //날짜별 정렬. 리스트에 사용
    @Query("SELECT * FROM meal_table ORDER BY today ASC")
    List<Meal> getTodayMeal();

}
