package team_2e.escape_farm;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;

import static team_2e.escape_farm.CheckedStage.onCheckStage;
import static team_2e.escape_farm.CheckedStage.onClearStage;
import static team_2e.escape_farm.Variable.animals;
import static team_2e.escape_farm.Variable.boardSize;
import static team_2e.escape_farm.Variable.direction;
import static team_2e.escape_farm.Variable.moveCount;
import static team_2e.escape_farm.Variable.stage;
import static team_2e.escape_farm.Variable.stageCount;
import static team_2e.escape_farm.Variable.animal;

public class GameObject {

    Context mContext;


    public GameObject(Context context) {
        mContext = context;
    }

    public static boolean activatedTrap() {
        for (int i = 0; i < animals.length; i++) {
            if (animals[i].getKind().equals("trap"))
                if (Math.round(animals[animal].getX()) == animals[i].getX() && Math.round(animals[animal].getY()) == animals[i].getY())
                    return true;
        }
        return false;
    }

    public void onFinish() {
        for (int i = 0; i < animals.length; i++) {
            if (animals[i].getKind().endsWith("fin")) { //fin 확인
                if (!matchFinish(i)) return;
            }
        }
        if (stageCount > Integer.parseInt(onCheckStage(mContext)))
            onClearStage(mContext, String.valueOf(stageCount));
        ((Game_Stage) mContext).Dialog();
        moveCount = 0;
        stageCount = 0;
    }

    private boolean matchFinish(int finish) {
        for (int i = 0; i < animals.length; i++) {
            if (animals[finish].getKind().equals(animals[i].getKind() + "_fin")) { //확인된 fin의 해당되는 동물
                if (animals[finish].getX() == animals[i].getX() && animals[finish].getY() == animals[i].getY()) //확인된 fin과 같은 위치에 그 동물이 위치하면
                    return true;
            }
        }
        return false;
    }

    public static float checkKind(int block) {
        float blockPoint = 0, isBlock = 0;
        String blockKind = animals[block].getKind();
        if (blockKind.equals("none") || blockKind.endsWith("fin"))
            isBlock = 1;
        if(block == 0)
            return 0;
        else if(block == 1)
            return boardSize-1;
        switch (direction) {
            case "left":
                blockPoint = animals[block].getX()+1;
                break;
            case "right":
                blockPoint = animals[block].getX()-1 + isBlock;
                break;
            case "up":
                blockPoint = animals[block].getY()+1;
                break;
            case "down":
                blockPoint = animals[block].getY()-1 + isBlock;
                break;
        }
        return blockPoint;
    }
}