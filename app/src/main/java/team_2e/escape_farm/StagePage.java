package team_2e.escape_farm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import static team_2e.escape_farm.Variable.boardSize;

public class StagePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//제목 없음
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_page);
    }

    public void onStageClick(View view) {
        switch (view.getId()) {
            case R.id.stage_1:
                boardSize=4;
                break;
            case R.id.stage_2:
                boardSize=6;
                break;
            case R.id.stage_3:
                boardSize=8;
                break;
        }
        Intent intent = new Intent(StagePage.this, Game_Stage.class);
        startActivity(intent);
    }
}
