package waterplace.finalproj.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import waterplace.finalproj.R;

public class MainActivity extends AppCompatActivity {

    private Button bt;
    private Button bt_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("tamo abrido");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        bt = (Button) findViewById(R.id.bt_log);
        bt_reg = (Button) findViewById(R.id.bt_reg);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("tamo abrido");
                goLogin();
            }
        });

        bt_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("tamo abrido");
                goRegister();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //Desabilita a arrow do android
    }

    public void goLogin(){
        Intent i = new Intent(this,Login.class);
        startActivity(i);
    }

    public void goRegister(){
        Intent i = new Intent(this,Register.class);
        startActivity(i);
    }
}