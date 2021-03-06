package team_2e.escape_farm;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Startpage extends AppCompatActivity {
    Thread w;
    boolean running = true, blink = true;
    Context context;
    CreateDB CreateDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//제목 없음
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);
        LinearLayout fullscreen = (LinearLayout) findViewById(R.id.fullscreen);
        fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDB();
                running = false;
                Intent intent = new Intent(Startpage.this,MainPage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void createDB(){
        if( CreateDB == null ) {
            CreateDB = new CreateDB(Startpage.this, "StageDB", null , 1);
            //CreateDB.putInData();
        }
    }

    @Override
    public void onStart() {
        final TextView tap = (TextView) findViewById(R.id.tap);
        super.onStart();
        w = new Thread(new Runnable() {
            public void run() {
                while(running) {
                    try{
                        Thread.sleep(800);
                    }catch (InterruptedException e){
                    }
                    blink=!blink;
                    tap.post(new Runnable() {
                        @Override
                        public void run() {
                            if (blink) tap.setVisibility(View.VISIBLE);
                            else tap.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });
        w.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        running = false;
    }
}
