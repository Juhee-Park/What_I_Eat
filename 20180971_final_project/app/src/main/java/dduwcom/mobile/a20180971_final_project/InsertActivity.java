package dduwcom.mobile.a20180971_final_project;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InsertActivity extends AppCompatActivity {

    //상수
    final String TAG = "MainActivity";
    private static final int REQ_PERMISSION_CODE = 100;
    private static final int UPDATE_CODE = 101;
    boolean is_calorie_number = true;
    boolean mapIsClicked = false;

    Intent intent;

    //뷰
    Spinner meal_insert_spinner;
    String[] meal_insert_list = {"아침", "아점", "점심", "점저", "저녁", "야식", "간식"};
    int meal_insert;
    EditText menu_insert;
    EditText calorie_insert;
    EditText restaurant_insert;
    TextView location_insert;

    //DB
    String today;
    Meal meal;
    MealDB db;
    MealDAO dao;
    DBThread dbThread;

    //API
    NetWorkTest netWorkTest;
    String apiAddress;
    List<FoodInfo> resultList;

    //지도
    FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap mGoogleMap;
    Marker marker;
    LatLng here;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        meal = new Meal();

        meal_insert_spinner = findViewById(R.id.meal_insert_spinner);
        menu_insert = findViewById(R.id.menu_insert);
        calorie_insert = findViewById(R.id.calorie_insert);
        restaurant_insert = findViewById(R.id.restaurant_insert);
        location_insert = findViewById(R.id.location_insert);

        today = new SimpleDateFormat("yyyy년 MM월 dd일").format(Calendar.getInstance().getTime());

        netWorkTest = new NetWorkTest();
        resultList = new ArrayList<FoodInfo>();

        //스피너 구현
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                meal_insert_list
        );
        spinner_adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        meal_insert_spinner.setAdapter(spinner_adapter);

        meal_insert_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                meal_insert = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                meal_insert = 0;
            }
        });



        //db 구현
        db = MealDB.getDatabase(this);
        dao = db.mealDAO();
        dbThread = new DBThread();

        //지도 구현
        checkPermission();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment
                = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView_insert);
        mapFragment.getMapAsync(mapReadyCallback);
    }
    //버튼 클릭 구현
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.insert_b1: //음식 API
                Toast.makeText(InsertActivity.this, "음식 정보 로딩 중...", Toast.LENGTH_SHORT).show();
                apiUse();
                break;
            case R.id.insert_b2: //지도 현재위치 찾기
                mapIsClicked = true;
                getLastLocation();

                break;
            case R.id.insert_b3: //DB에 정보 입력

                for (char chara : calorie_insert.getText().toString().toCharArray()) {
                    if (!(Character.isDigit(chara) || chara == '.')
                            || calorie_insert.getText().toString().equals(".")) {
                        is_calorie_number = false;
                    }
                }
                //위치를 설정하지 않으면 오류팝업
                if (mapIsClicked == false) {
                    showDialog("map");
                } else
                //빈칸이 있으면 오류팝업
                if (menu_insert.getText().toString().equals("")
                        || restaurant_insert.getText().toString().equals("")
                        || calorie_insert.getText().toString().equals("")) {

                    showDialog("blank");
                }
                //칼로리가 숫자가 아니면 오류팝업
                else if (is_calorie_number == false) {
                    showDialog("calorie");
                } else {
                    dbThread.start();
                    Toast.makeText(InsertActivity.this, "추가했습니다,", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(InsertActivity.this, MainActivity.class);
                    startActivityForResult(intent, UPDATE_CODE);

                    finish();
                }
                is_calorie_number = true;
                break;
            case R.id.insert_b4: //취소
                intent = new Intent(InsertActivity.this, MainActivity.class);
                intent.putExtra("meal", meal);
                startActivityForResult(intent, UPDATE_CODE);
                finish();
                break;
        }
    };

    //api 구현
    public void apiUse() {
        if (menu_insert.getText() != null) {
            apiAddress = getResources().getString(R.string.server_url) +  menu_insert.getText();

            new NetworkAsyncTask().execute(apiAddress);

        } else {
            Toast.makeText(InsertActivity.this, "검색을 위해서 음식 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
    }
    //지도 구현

    //권한 받기
    private void checkPermission() {
        if(!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED)) {
            mapIsClicked = false;
            requestPermissions(new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, REQ_PERMISSION_CODE);
        }
    }
    //지도띄우기
    OnMapReadyCallback mapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            mGoogleMap = googleMap;

            here = new LatLng(R.string.init_lat,R.string.init_lng);

            MarkerOptions options = new MarkerOptions();
            options.position(here);
            options.title("현재 위치");
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

            marker = mGoogleMap.addMarker(options);
            marker.setPosition(here);
            marker.showInfoWindow();

            //지도 클릭
            mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull LatLng latLng) {

                    mapIsClicked = true;
                    here = latLng;
                    marker.setPosition(latLng);
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

                    Location location = new Location("Test");
                    location.setLatitude(latLng.latitude);
                    location.setLongitude(latLng.longitude);
                    location.setTime(new Date().getTime());

                    new GeoTask().execute(location);


                }
            });
        }
    };
    //현재위치 확인
    private void getLastLocation() {
        checkPermission();
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(
                new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                            here = new LatLng(latitude, longitude);
                            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(here, 17));

                            marker.setPosition(here);

                            new GeoTask().execute(location);

                        } else {
                            Toast.makeText(InsertActivity.this, "현재 위치 찾을 수 없음", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        fusedLocationProviderClient.getLastLocation().addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "지도 불러오기 실패!");
                    }
                }
        );
    }

    //Geocoder 구현
    class GeoTask extends AsyncTask<Location, Void, List<Address>> {
        Geocoder geocoder = new Geocoder(InsertActivity.this, Locale.getDefault());
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
            location_insert.setText(address.getAddressLine(0));
        }
    }
    //네트워크
    //api 구현
    class NetworkAsyncTask extends AsyncTask<String, Void, String> {

        final static String NETWORK_ERR_MSG = "목록이 너무 많습니다.";
        public final static String TAG = "NetworkAsyncTask";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String address = strings[0];
            String result = netWorkTest.downloadContents(address);
            if (result == null) {
                cancel(true);
                return NETWORK_ERR_MSG;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            // parser 생성 및 OpenAPI 수신 결과를 사용하여 parsing 수행
            FoodInfoXmlParser parser = new FoodInfoXmlParser();
            resultList = parser.parse(result);

            String[] foodNames = new String[resultList.size()];

            int i = 0;
            for (FoodInfo f : resultList) {
                foodNames[i++] = f.getDESC_KOR() +"     " + f.getNUTR_CONT1() + "kcal";
            }
            i = 0;

            AlertDialog.Builder builder = new AlertDialog.Builder(InsertActivity.this);
            builder.setTitle("검색된 식품 목록");
            builder.setItems(foodNames, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    menu_insert.setText(resultList.get(i).getDESC_KOR());
                    calorie_insert.setText(Double.toString(resultList.get(i).getNUTR_CONT1()));
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        @Override
        protected void onCancelled(String msg) {
            super.onCancelled();
            Toast.makeText(InsertActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }
    //경고창 구현
    void showDialog(String s) {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(InsertActivity.this);
        msgBuilder.setTitle("오류");
        if (s.equals("calorie")) {
            msgBuilder.setMessage("칼로리는 숫자로 입력해 주십시오.");
        } else if (s.equals("blank")) {
            msgBuilder.setMessage("빈칸을 전부 채워주십시오.");
        } else if (s.equals("map")) {
            msgBuilder.setMessage("지도 버튼을 눌러 위치를 가져오십시오.");
        }
        msgBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }

    //db 스레드
    private class DBThread extends Thread {

        public void run() {
            dao.insertMeal(new Meal(
                    today,
                    meal_insert,
                    menu_insert.getText().toString(),
                    Double.parseDouble(calorie_insert.getText().toString()),
                    here.latitude,
                    here.longitude,
                    restaurant_insert.getText().toString()
            ));
        }
    }
}
