package dduwcom.mobile.a20180971_final_project;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.w3c.dom.Entity;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class GraphActivity extends AppCompatActivity {

    //뷰
    LineChart chart_week;
    LineChart chart_month;

    //DB
    Meal meal;
    MealDB db;
    MealDAO dao;

    //그래프
    MakeWeekGraph makeWeekGraph;
    MakeMonthGraph makeMonthGraph;

    SimpleDateFormat dayFormat;
    SimpleDateFormat monthFormat;
    String[] week = {"월", "화", "수", "목", "금", "토", "일"};
    String[] month = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul","Aug", "Sep", "Oct", "Nov","Dec"};

    ArrayList<Entry> thisWeekCal;
    ArrayList<Entry> thisMonthCal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        ArrayList<Entry> chart_week_entry = new ArrayList<>();
        ArrayList<Entry> chart_month_entry = new ArrayList<>();

        chart_week = findViewById(R.id.chart_week);
        chart_month = findViewById(R.id.chart_month);

        dayFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        monthFormat = new SimpleDateFormat("yyyy년 MM월");

        thisWeekCal = new ArrayList<>();
        thisMonthCal = new ArrayList<>();

        //db 구현
        db = MealDB.getDatabase(this);
        dao = db.mealDAO();

        //각각 스레드를 나눠서 그래프 구현
        makeWeekGraph= new MakeWeekGraph();
        makeMonthGraph = new MakeMonthGraph();

        makeWeekGraph.start();
        makeMonthGraph.start();
    }
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.graph_b1 :
                finish();
                break;
        }
    }
    public void makeGraph(LineChart lineChart, ArrayList<Entry> cal) {

        LineDataSet dataSet = new LineDataSet(cal, "일주일 칼로리 변화량");
        LineData data = new LineData(dataSet);
        lineChart.setData(data);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

    }

    //이번 주의 칼로리 그래프 출력
    private class MakeWeekGraph extends Thread {

        public void run() {

            Calendar today = new GregorianCalendar();
            int day = today.get(Calendar.DAY_OF_WEEK);
            today.add(Calendar.DATE, - day + 1);

            for (int i = 1; i <= 7 ; i++) {

                Double todayTotalCal = dao.getTodaysTotalCal(dayFormat.format(today.getTime()));

                if (todayTotalCal != null) {
                    thisWeekCal.add(new Entry(i, todayTotalCal.floatValue()));
                } else {
                    thisWeekCal.add(new Entry(i,  0));
                }
                today.add(Calendar.DATE, 1);
            }

            makeGraph(chart_week, thisWeekCal);
        }
    }

    //이번 달의 칼로리 그래프 출력
    private class MakeMonthGraph extends Thread {

        public void run() {

            Calendar today = new GregorianCalendar();
            int month = today.get(Calendar.DAY_OF_MONTH);
            today.add(Calendar.DAY_OF_MONTH, - month + 1);

            for (int i = 1; i <= today.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {

                Double monthTotalCal = dao.getTodaysTotalCal(dayFormat.format(today.getTime()));
                if (monthTotalCal != null) {
                    thisMonthCal.add(new Entry(i, monthTotalCal.floatValue()));
                } else {
                    thisMonthCal.add(new Entry(i,  0));
                }
                today.add(Calendar.DATE, 1);
            }

            makeGraph(chart_month, thisMonthCal);

        }
    }
}
