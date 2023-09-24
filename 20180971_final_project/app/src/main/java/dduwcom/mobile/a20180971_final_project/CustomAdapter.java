package dduwcom.mobile.a20180971_final_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Meal> mealList;
    private LayoutInflater inflater;

    public CustomAdapter(Context context, int layout, ArrayList<Meal> mealList) {

        this.context = context;
        this.layout = layout;
        this.mealList = mealList;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mealList.size();
    }

    @Override
    public Object getItem(int i) {
        return mealList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final int pos = i;
        long num = getItemId(pos);

        if(view == null){
            view = inflater.inflate(layout, viewGroup, false);
        }

        TextView date_cAdapter = view.findViewById(R.id.adapter_date);
        TextView adapter_meal = view.findViewById(R.id.adapter_meal);
        TextView adapter_menu = view.findViewById(R.id.adapter_menu);
        TextView adapter_cal = view.findViewById(R.id.adapter_cal);

        String[] meal_list = {"아침", "아점", "점심", "점저", "저녁", "야식", "간식"};

        // 날짜, 식사 때, 메뉴, 칼로리 출력
        date_cAdapter.setText(mealList.get(i).getToday());
        adapter_meal.setText(meal_list[mealList.get(i).getWhen_meal()] + ": ");
        adapter_menu.setText(mealList.get(i).getMenu());
        adapter_cal.setText(Double.toString(mealList.get(i).getCalorie()) + " kcal");

        return view;
    }

}
