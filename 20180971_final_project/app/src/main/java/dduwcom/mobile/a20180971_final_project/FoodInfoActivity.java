package dduwcom.mobile.a20180971_final_project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FoodInfoActivity extends AppCompatActivity {

    final static String TAG = "FoodInfoActivity";
    final int REQ_CODE = 101;
    final int UPDATE_CODE = 202;
    Intent intent;

    Meal meal;

    //뷰
    TextView foodInfo_whenMeal;
    TextView foodInfo_menu;
    TextView foodInfo_cal;
    TextView foodInfo_addr;
    TextView foodInfo_res;

    //DB
    MealDB db;
    MealDAO dao;
    DBThread dbThread;
    String[] meal_list = {"아침", "아점", "점심", "점저", "저녁", "야식", "간식"};

    //지도
    FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap mGoogleMap;
    Marker marker;
    MarkerOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodinfo);

        meal = (Meal) getIntent().getSerializableExtra("meal");

        foodInfo_whenMeal = findViewById(R.id.foodInfo_whenMeal);
        foodInfo_menu = findViewById(R.id.foodInfo_menu);
        foodInfo_cal = findViewById(R.id.foodInfo_cal);
        foodInfo_addr = findViewById(R.id.foodInfo_addr);
        foodInfo_res = findViewById(R.id.foodInfo_res);

        foodInfo_whenMeal.setText(meal_list[meal.getWhen_meal()]);
        foodInfo_menu.setText(meal.getMenu());
        foodInfo_cal.setText(Double.toString(meal.getCalorie()));
        foodInfo_res.setText(meal.getRestaurant());

        //db 구현
        db = MealDB.getDatabase(this);
        dao = db.mealDAO();
        dbThread = new DBThread();

        //지도 구현
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment
                = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView_foodInfo);
        mapFragment.getMapAsync(mapReadyCallback);


    }
    //버튼 구현
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.foodInfo_b1 : //수정
                intent = new Intent(FoodInfoActivity.this, UpdateActivity.class);
                intent.putExtra("meal", meal);
                startActivityForResult(intent, UPDATE_CODE);

                finish();
                break;
            case R.id.foodInfo_b2 : //삭제

                showDialog();

                break;
            case R.id.foodInfo_b3 : //취소

                intent = new Intent(FoodInfoActivity.this, ListActivity.class);
                startActivityForResult(intent, UPDATE_CODE);

                finish();
                break;
        }
    }
    //지도 구현
    OnMapReadyCallback mapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            mGoogleMap = googleMap;

            LatLng mLatLng = new LatLng(meal.getLat(), meal.getLng());

            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 17));

            options = new MarkerOptions();
            options.position(mLatLng);
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            options.title((String)foodInfo_res.getText());
            marker = mGoogleMap.addMarker(options);
            marker.showInfoWindow();

            Location location = new Location("Test");
            location.setLatitude(mLatLng.latitude);
            location.setLongitude(mLatLng.longitude);
            location.setTime(new Date().getTime());

            new GeoTask().execute(location);

        }
    };
    //Geocoder 구현
    class GeoTask extends AsyncTask<Location, Void, List<Address>> {
        Geocoder geocoder = new Geocoder(FoodInfoActivity.this, Locale.getDefault());
        @Override
        protected List<Address> doInBackground(Location... locations) {
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(locations[0].getLatitude(),
                        locations[0].getLongitude(), 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {
            Address address = addresses.get(0);
            foodInfo_addr.setText(address.getAddressLine(0));
        }
    }
    //경고창 구현
    void showDialog() {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(FoodInfoActivity.this);
        msgBuilder.setTitle("삭제 확인");
        msgBuilder.setMessage("정말로 삭제합니까?");
        msgBuilder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        //확인을 누르면 삭제된 후 리스트로 돌아감.
        msgBuilder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dbThread.start();

                Toast.makeText(FoodInfoActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();

                intent = new Intent(FoodInfoActivity.this, ListActivity.class);
                startActivityForResult(intent, UPDATE_CODE);

                finish();
            }
        });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }
    //db 스레드
    private class DBThread extends Thread {

        public void run() {
            dao.deleteMeal(meal);
        }
    }
}
