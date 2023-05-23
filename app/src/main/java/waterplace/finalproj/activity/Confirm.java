package waterplace.finalproj.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import waterplace.finalproj.R;

public class Confirm extends AppCompatActivity {

    private ImageButton bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_confirm);

        bt = (ImageButton) findViewById(R.id.back_arrow_3);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
    }

    public void goBack(){
        Intent i = new Intent(this, Recover.class);
        startActivity(i);
    }
}