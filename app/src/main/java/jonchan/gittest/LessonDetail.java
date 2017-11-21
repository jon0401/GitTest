package jonchan.gittest;

/**
 * Created by Chung on 22/11/2017.
 */

public class LessonDetail {


    public LessonDetail(String date, String startTime, String endTime, String location){
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
    }

    String date;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    String startTime;

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    String endTime;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    String location;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
