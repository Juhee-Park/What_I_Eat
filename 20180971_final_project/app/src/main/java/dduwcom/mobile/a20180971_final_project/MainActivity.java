package dduwcom.mobile.a20180971_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    //상수
    final int REQ_CODE = 101;

    //변수
    Intent intent;

    String today;
    int todaysTotalCal = 0;
    int recommened = 0;

    //뷰
    TextView main_todayCal;
    TextView main_recCal;

    //DB
    MealDB mealDB;
    MealDAO mealDAO;
    DBMealThread dbMealThread;

    MyInfoDB myInfoDB;
    MyInfoDAO myInfoDAO;
    DBMyInfoThread dbMyInfoThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        today = new SimpleDateFormat("yyyy년 MM월 dd일").format(Calendar.getInstance().getTime());

        main_todayCal = findViewById(R.id.main_todayCal);
        main_recCal = findViewById(R.id.main_recCal);

        //db 구현
        mealDB = MealDB.getDatabase(this);
        mealDAO = mealDB.mealDAO();
        dbMealThread = new DBMealThread();

        myInfoDB = MyInfoDB.getDatabase(this);
        myInfoDAO = myInfoDB.MyInfoDAO();
        dbMyInfoThread = new DBMyInfoThread();

        dbMealThread.start();
        dbMyInfoThread.start();

    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.main_b1 : //권장 칼로리 계산
                intent = new Intent(this, MyInfoActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.main_b2 : //음식 정보 추가
                intent = new Intent(this, InsertActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.main_b3 : //식단 확인
                intent = new Intent(this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.main_b4 : //그래프 확인
                intent = new Intent(this, GraphActivity.class);
                startActivity(intent);
                break;
        }
    }
    //일일 섭취 칼로리 띄우기
    private class DBMealThread extends Thread {

        public void run() {
            if (mealDAO.getTodaysTotalCal(today) != null) {
                todaysTotalCal = mealDAO.getTodaysTotalCal(today).intValue();
                main_todayCal.setText(Integer.toString(todaysTotalCal));
            }
        }
    }
    //권장 섭취 칼로리 띄우기
    private class DBMyInfoThread extends Thread {

        public void run() {
            if (myInfoDAO.returnMyInfoRec() != null) {
                recommened = myInfoDAO.returnMyInfoRec().intValue();
                main_recCal.setText(Integer.toString(recommened));
            }
        }
    }
}