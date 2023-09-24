package dduwcom.mobile.a20180971_final_project;

import androidx.room.*;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


@Entity(tableName = "meal_table")
public class Meal implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int _id;

    @ColumnInfo(name = "today")
    private String today;
    @ColumnInfo(name = "when_meal")
    private int when_meal;
    @ColumnInfo(name = "menu")
    private String menu;
    @ColumnInfo(name = "calorie")
    private double calorie;
    @ColumnInfo(name = "lat")
    private double lat;
    @ColumnInfo(name = "lng")
    private double lng;
    @ColumnInfo(name = "restaurant")
    private String restaurant;

    public Meal() {
    }
    public Meal(String today, int when_meal, String menu, double calorie,
        double lat, double lng, String restaurant){
        this.today = today;
        this.when_meal = when_meal;
        this.menu = menu;
        this.calorie = calorie;
        this.lat = lat;
        this.lng = lng;
        this.restaurant = restaurant;
    }
    public Meal(int _id, String today, int when_meal, String menu, double calorie,
                double lat, double lng, String restaurant){
        this._id = _id;
        this.today = today;
        this.when_meal = when_meal;
        this.menu = menu;
        this.calorie = calorie;
        this.lat = lat;
        this.lng = lng;
        this.restaurant = restaurant;
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public int getWhen_meal() {
        return when_meal;
    }

    public void setWhen_meal(int when_meal) {
        this.when_meal = when_meal;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public double getCalorie() {
        return calorie;
    }

    public void setCalorie(double calorie) {
        this.calorie = calorie;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "_id=" + _id +
                ", today='" + today + '\'' +
                ", when_meal=" + when_meal +
                ", menu='" + menu + '\'' +
                ", calorie=" + calorie +
                ", lat=" + lat +
                ", lng=" + lng +
                ", restaurant='" + restaurant + '\'' +
                '}';
    }
}

