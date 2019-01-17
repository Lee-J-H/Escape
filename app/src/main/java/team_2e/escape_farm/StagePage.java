package team_2e.escape_farm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;


public class StagePage extends AppCompatActivity {
    getStageData StageData = new getStageData(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//제목 없음
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_page);
    }

    public void onStageClick(View view) {
        switch (view.getId()) {
            case R.id.stage_1:
                StageData.getData("1");
                break;
            case R.id.stage_2:
                StageData.getData("2");
                break;
            case R.id.stage_3:
                StageData.getData("3");
                break;
        }
        Intent intent = new Intent(StagePage.this, Game_Stage.class);
        startActivity(intent);
    }
}
