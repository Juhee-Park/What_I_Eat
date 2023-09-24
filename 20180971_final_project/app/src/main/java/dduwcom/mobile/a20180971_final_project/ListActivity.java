package dduwcom.mobile.a20180971_final_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    //상수
    final static String TAG = "ListActivity";
    final int REQ_CODE = 101;
    final int UPDATE_CODE = 202;

    //뷰
    ListView listView_list;
    CustomAdapter adapter;
    ArrayList<Meal> mealList = null;

    //DB
    MealDB mealDB;
    MealDAO mealDAO;
    DBThread dbThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //어뎁터 리스트에 불러오기
        listView_list = findViewById(R.id.listView_list);
        mealList = new ArrayList();
        adapter = new CustomAdapter(this, R.layout.custom_adapter_view, mealList);
        listView_list.setAdapter(adapter);

        //db구현
        mealDB = MealDB.getDatabase(this);
        mealDAO = mealDB.mealDAO();
        dbThread = new DBThread();

        //리스트 클릭 구현. 클릭 시 해당 정보 페이지로
        listView_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                Meal meal = mealList.get(pos);

                Intent intent = new Intent(ListActivity.this, FoodInfoActivity.class);
                intent.putExtra("meal", meal);
                setResult(RESULT_OK, intent);
                startActivityForResult(intent, UPDATE_CODE);
                finish();
            }

        });

    }
    public void onClick(View v) {

        switch (v.getId()) { //뒤로가기
            case R.id.list_b1 :
                finish();
                break;

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        //db 실행
        dbThread.start();
    }

    //db 스레드
    private class DBThread extends Thread {

        public void run() {
            mealList.clear();
            mealList.addAll(mealDAO.getTodayMeal());
            adapter.notifyDataSetChanged();

        }
    }
}
