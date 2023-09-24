package dduwcom.mobile.a20180971_final_project;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyInfoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE) //추가 후 갱신 가능하도록 설정함.
    void insertMyInfo(MyInfo myInfo);

    @Update
    void updateMyInfo(MyInfo myInfo);

    @Delete
    void deleteMyInfo(MyInfo myInfo);

    //권장 칼로리 반환. 메인에서 사용
    @Query("SELECT recommended FROM myInfo_table WHERE _id = 0 ")
    Double returnMyInfoRec();

    //내 정보 반환. 마이인포 페이지에서 사용
    @Query("SELECT * FROM myInfo_table WHERE _id = 0 ")
    MyInfo returnMyInfo();
}
