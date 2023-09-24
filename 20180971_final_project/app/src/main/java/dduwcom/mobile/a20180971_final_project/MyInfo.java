package dduwcom.mobile.a20180971_final_project;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;


@Entity(tableName = "myInfo_table")
public class MyInfo implements Serializable {
    @PrimaryKey //갱신만이 일어나기 때문에 id는 전부 똑같이 0을 사용함.
    private int _id;

    @ColumnInfo(name = "sex")
    private int sex;
    @ColumnInfo(name = "age")
    private int age;
    @ColumnInfo(name = "weight")
    private double weight;
    @ColumnInfo(name = "height")
    private double height;
    @ColumnInfo(name = "state")
    private int state;
    @ColumnInfo(name = "recommended")
    private double recommended;

    MyInfo (int _id, int sex, int age,
            double weight, double height, int state, double recommended) {

        this._id = _id;
        this.sex = sex;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.state = state;
        this.recommended = recommended;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double getRecommended() {
        return recommended;
    }

    public void setRecommended(double recommended) {
        this.recommended = recommended;
    }

    @Override
    public String toString() {
        return "MyInfo{" +
                "_id=" + _id +
                ", sex=" + sex +
                ", age=" + age +
                ", weight=" + weight +
                ", height=" + height +
                ", state=" + state +
                ", recommended=" + recommended +
                '}';
    }
}
