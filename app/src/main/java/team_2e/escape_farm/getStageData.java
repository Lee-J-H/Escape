package team_2e.escape_farm;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import static team_2e.escape_farm.Variable.boardSize;
import static team_2e.escape_farm.Variable.stage;
import static team_2e.escape_farm.Variable.stageCount;

public class getStageData extends Activity {

    Context mContext;

    public getStageData(Context context) {
        mContext = context;
    }

    public void getData(String selectedStage) {
        AssetManager am = mContext.getResources().getAssets();
        InputStream is = null;
        BufferedReader bufrd = null;
        int ch;
        String data_txt = "";

        try {
            is = am.open("stage_"+ selectedStage + ".txt");
            bufrd = new BufferedReader(new InputStreamReader(is));

            while ((ch = bufrd.read()) != -1)
                data_txt += String.format("%c", ch);

            stage.clear();
            List<String> list = Arrays.asList(data_txt.split("/"));
            for (int i = 0; i < list.size(); i++) {
                String[] array = list.get(i).split(",");
                List<String> list2 = Arrays.asList(array);
                stage.add(list2);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (is != null) {
            try {
                bufrd.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        stageCount=Integer.parseInt(selectedStage);
        boardSize=Integer.parseInt(stage.get(1).get(1))+1;
    }
}
