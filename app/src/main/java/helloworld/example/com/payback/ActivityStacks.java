package helloworld.example.com.payback;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by Owner on 2016-05-22.
 */
public class ActivityStacks {
    public static final ActivityStacks activityManager = new ActivityStacks();
    private ArrayList<Activity> listActivity = null;

    private ActivityStacks() {
        listActivity = new ArrayList();
    }

    public static ActivityStacks getInstance() {
        return activityManager;
    }

    public void addActivity(Activity activity) {
        listActivity.add(activity);
    }

    public boolean removeActivity(Activity activity) {
        return listActivity.remove(activity);
    }

    public void finishAllActivity() {
        for (Activity activity : listActivity) {
            activity.finish();
        }
    }

    public ArrayList getListActivity() {
        return listActivity;
    }

    public void setListActivity(ArrayList listActivity) {
        this.listActivity = listActivity;
    }
}
