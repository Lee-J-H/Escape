package team_2e.escape_farm;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static team_2e.escape_farm.Variable.stage;
import static team_2e.escape_farm.Variable.stage_clear;
import static team_2e.escape_farm.Variable.mAct;

public class Game_Stage extends AppCompatActivity {
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//제목 없음
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_stage);
        mAct=this;

        File file = new File(context.getFilesDir(), "stage_data.txt");
        FileWriter fw = null;
        BufferedWriter bufwr = null;

        String str = "0 0 0 0 0\n0 7 7 0 0\n0 2 3 0 1\n0 1 2 1 1\n1 3 3 0 0\n2 3 0 0 0\n0 6 3 0 1\n0 3 5 1 0\n0 3 2 1 0\n10 5 6 1 1";

        try {
            // open file.
            fw = new FileWriter(file);
            bufwr = new BufferedWriter(fw);

            // write data to the file.
            bufwr.write(str);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // close file.
        try {
            if (bufwr != null)
                bufwr.close();

            if (fw != null)
                fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        int ch;
        String data_txt = "";

        try {
            // open file.
            FileReader fr = new FileReader(file);
            BufferedReader bufrd = new BufferedReader(fr);

            // read 1 char from file.
            while ((ch = bufrd.read()) != -1)
                data_txt += String.format("%c", ch);

            stage.clear();
            List<String> list = Arrays.asList(data_txt.split("\n"));
            for (int i = 0; i < list.size(); i++) {
                String[] array = list.get(i).split(" ");
                List<String> list2 = Arrays.asList(array);
                stage.add(list2);
            }
            /*//stage_data = Arrays.asList(data_txt.split("\n"));
            stage_data = new ArrayList<String>(Arrays.asList(data_txt.split("\n")));
            for (int i = 0; i < stage_data.size(); i++)
                stage.add(stage_data.get(i).split(" "));*/

            // close file.
            bufrd.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    final static Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case stage_clear:
                    mAct.finish();
                    break;
            }
        }
    };

}
