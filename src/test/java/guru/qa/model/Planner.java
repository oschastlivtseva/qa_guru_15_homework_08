package guru.qa.model;

import java.util.List;

public class Planner {

    public Date todaysDate;
    public boolean isAHoliday;
    public int numberOfPlannedActivities;
    public Activities todaysActivities;

    public static class Date {
        public String year;
        public String month;
        public String day;
        public String dayOfWeek;
        public String time;
    }

    public static class Activities {
        public List<String> study;
        public List<String> choresAtHome;
        public List<String> work;
        public List<String> fitness;
        public List<String> meetings;
    }
}
