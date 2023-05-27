package waterplace.finalproj.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;

import waterplace.finalproj.R;
import waterplace.finalproj.model.User;
import waterplace.finalproj.util.DistanceUtils;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_menu);
        DistanceUtils distanceUtils = new DistanceUtils();
        User user = User.getInstance();
    }
}