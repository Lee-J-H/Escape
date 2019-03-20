package team_2e.escape_farm;

import android.app.Activity;
import java.util.ArrayList;
import java.util.List;

public class Variable {
    public static int boardSize;
    public static ArrayList<List<String>> stage = new ArrayList<List<String>>();
    static int stageCount, moveCount, animal;
    public static int stageNum=0;
    public static Animal[] animals = new Animal[stageNum];
    public static String direction="";
}
