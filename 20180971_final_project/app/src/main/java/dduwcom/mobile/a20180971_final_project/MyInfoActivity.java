package dduwcom.mobile.a20180971_final_project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MyInfoActivity extends AppCompatActivity {

    //상수
    final static String TAG = "MyInfoActivity";
    final int REQ_CODE = 101;
    final int UPDATE_CODE = 202;

    //변수
    Intent intent;
    boolean is_age_int = true;
    boolean is_num = true;

    //뷰
    Spinner sex_spinner;
    String[] sex_spinner_list = {"여", "남", "그 외"};
    int myInfo_sex = 0;
    Spinner state_spinner;
    String[] state_spinner_list = {"감량 중", "유지 중", "증량 중"};
    int myInfo_state = 0;
    EditText myInfo_age;
    EditText myInfo_weight;
    EditText myInfo_height;
    double recommended;

    //DB
    MyInfo myInfo = null;
    MyInfoDB db;
    MyInfoDAO dao;
    DBThread dbThread;
    DBGetThread dbGetThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);

        sex_spinner = findViewById(R.id.sex_spinner);
        state_spinner = findViewById(R.id.state_spinner);

        myInfo_age = findViewById(R.id.myInfo_age);
        myInfo_weight = findViewById(R.id.myInfo_weight);
        myInfo_height = findViewById(R.id.myInfo_height);

        //db구현
        db = MyInfoDB.getDatabase(this);
        dao = db.MyInfoDAO();
        dbThread = new DBThread();
        dbGetThread = new DBGetThread();

        //성별스피너 구현
        ArrayAdapter<String> sex_spinner_adapter = new ArrayAdapter<String>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                sex_spinner_list
        );
        sex_spinner_adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        sex_spinner.setAdapter(sex_spinner_adapter);

        sex_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                myInfo_sex = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //상태스피너 구현
        ArrayAdapter<String> state_spinner_adapter = new ArrayAdapter<String>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                state_spinner_list
        );
        state_spinner_adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        state_spinner.setAdapter(state_spinner_adapter);

        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                myInfo_state = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //값 가져오기
        dbGetThread.start();

    }
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.myInfo_b1 : //db에 반영

                //나이가 정수인지 판별
                for (char chara : myInfo_age.getText().toString().toCharArray()) {
                    if (!Character.isDigit(chara)) {
                        is_age_int = false;
                    }
                }
                //몸무게가 숫자인지 판별
                for (char chara : myInfo_weight.getText().toString().toCharArray()) {
                    if (!(Character.isDigit(chara) || chara == '.')
                            || myInfo_weight.getText().toString().equals(".")) {
                        is_num = false;
                    }
                }
                //키가 숫자인지 판별
                for (char chara : myInfo_height.getText().toString().toCharArray()) {
                    if (!(Character.isDigit(chara) || chara == '.')
                            || myInfo_height.getText().toString().equals(".")) {
                        is_num = false;
                    }
                }

                //빈칸이거나 숫자가 아닌 경우 오류팝업
                if (is_num == false
                        || myInfo_age.getText().toString().equals("")
                        || myInfo_weight.getText().toString().equals("")
                        || myInfo_height.getText().toString().equals("")) {

                    showDialog("blankOrNotNum");
                }
                //나이가 정수가 아니면 오류팝업
                else if (is_age_int == false) {
                    showDialog("int");

                } else {
                    showDialog("reco");
                }
                is_age_int = true;
                is_num = true;
                break;
            case R.id.myInfo_b2 : // 취소
                intent = new Intent(MyInfoActivity.this, MainActivity.class);
                intent.putExtra("myInfo", myInfo);
                startActivityForResult(intent, UPDATE_CODE);
                finish();
                break;

        }
    }

    //기초대사량 계산
    public void cacl() {

        int age = Integer.parseInt(myInfo_age.getText().toString());
        double weight = Double.parseDouble(myInfo_weight.getText().toString());
        double height = Double.parseDouble(myInfo_height.getText().toString());

        if (myInfo_sex == 1) {
           recommended = 66.47 + (13.75 * weight) + (5 * height) - (6.76 * age);
        } else {
            recommended = 655.1 + (9.56 * weight) + (1.85 * height) - (4.68 * age);
        }

        if (myInfo_state == 0) {

            recommended = recommended * 1.2;

        } else if (myInfo_state == 1) {

            recommended = recommended * 1.55;

        } else {

            recommended = recommended * 1.9;

        }

        //소숫점 버림
        recommended = Math.ceil(recommended);

    }
    //경고창 구현
    void showDialog(String s) {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(MyInfoActivity.this);
        if (s.equals("int")) {
            msgBuilder.setTitle("오류");
            msgBuilder.setMessage("나이는 정수로 입력해 주십시오.");
            msgBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
        } else if (s.equals("blankOrNotNum")) {
            msgBuilder.setTitle("오류");
            msgBuilder.setMessage("숫자를 입력해 주십시오.");
            msgBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
        } else if (s.equals("reco")) {
            //계산 후 출력
            cacl();
            msgBuilder.setTitle("확인");
            msgBuilder.setMessage("당신의 하루 권장 섭취 칼로리는 " + recommended + " kcal 입니다." +
                    "\n 반영하시겠습니까?");

            //확인을 누르면 db반영 후 메인으로 돌아감
            msgBuilder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                        dbThread.start();

                        Toast.makeText(MyInfoActivity.this, "반영되었습니다,", Toast.LENGTH_SHORT).show();

                        intent = new Intent(MyInfoActivity.this, MainActivity.class);
                        intent.putExtra("myInfo", myInfo);
                        startActivityForResult(intent, UPDATE_CODE);
                        finish();


                }
            });
            msgBuilder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });

        }
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }

    //db스레드
    private class DBThread extends Thread {

        public void run() {
            //db에 저장
            dao.insertMyInfo(new MyInfo(
                    0,
                    myInfo_sex,
                    Integer.parseInt(myInfo_age.getText().toString()),
                    Double.parseDouble(myInfo_weight.getText().toString()),
                    Double.parseDouble(myInfo_height.getText().toString()),
                    myInfo_state,
                    recommended
            ));
        }
    }
    //db 저장값 가져오기
    private class DBGetThread extends Thread {

        public void run() {
            myInfo = dao.returnMyInfo();

            if (myInfo != null) {

                myInfo_sex = myInfo.getSex();
                myInfo_state = myInfo.getSex();

                myInfo_age.setText(Integer.toString(myInfo.getAge()));
                myInfo_weight.setText(Double.toString(myInfo.getWeight()));
                myInfo_height.setText(Double.toString(myInfo.getHeight()));

            }
        }
    }

}
