package waterplace.finalproj.util;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import waterplace.finalproj.R;
import waterplace.finalproj.activity.MainMenu;
import waterplace.finalproj.activity.Orders;

public class BottomNavigationManager implements BottomNavigationView.OnNavigationItemSelectedListener {

    private Activity activity;

    public BottomNavigationManager(Activity activity) {
        this.activity = activity;
    }

    public void setupBottomNavigation(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                startActivity(MainMenu.class);
                return true;
            case R.id.lista:
                startActivity(Orders.class);
                return true;
        }
        return false;
    }

    private void startActivity(Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
    }
}