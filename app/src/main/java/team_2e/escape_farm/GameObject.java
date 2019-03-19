package team_2e.escape_farm;

import android.content.Context;
import android.graphics.Point;

import static team_2e.escape_farm.CheckedStage.onCheckStage;
import static team_2e.escape_farm.CheckedStage.onClearStage;
import static team_2e.escape_farm.Variable.boardSize;
import static team_2e.escape_farm.Variable.moveCount;
import static team_2e.escape_farm.Variable.stage;
import static team_2e.escape_farm.Variable.stageCount;
import static team_2e.escape_farm.Variable.animal;

public class GameObject {

    Context mContext;
    Float animalX, animalY, blockX, blockY;
    int block = 0;
    int branch_tes = 99999999;
    int branch_tes2 = 99999999;
    int branch_tes3 = 99999999;
    int branch_tes4 = 99999999;

    public GameObject(Context context) {
        mContext = context;
    }

    public void onAnimalMove(int xy, String direction) {
        String check = stage.get(animal).get(xy); //이동 전 동물의 x,y값 저장
        //setAnimalXY();
        if (!direction.equals("")) {
            movingAnimal(xy, direction); //동물 이동
        }
        if (!stage.get(animal).get(xy).equals(check))
            moveCount++; //동물이 이동을 했으면 moveCount 증가
        ((Game_Stage) mContext).setMoveCount(); //Game_Stage Activity의 setMoveCount함수 호출(뷰에 이동횟수 재설정)
        onFinish();
    }

   /* private void setAnimalXY() {
        animalX = Float.parseFloat(stage.get(animal).get(1));
        animalY = Float.parseFloat(stage.get(animal).get(2));
    }

    private void setBlockXY() {
        blockX = Float.parseFloat(stage.get(block).get(1));
        blockY = Float.parseFloat(stage.get(block).get(2));
    }*/

    private boolean activatedTrap() {
        for (int i = 0; i < stage.size(); i++) {
            if (stage.get(i).get(0).equals("100"))
                if (Math.round(Float.parseFloat(stage.get(animal).get(1))) == Float.parseFloat(stage.get(i).get(1)) && Math.round(Float.parseFloat(stage.get(animal).get(2))) == Float.parseFloat(stage.get(i).get(2)))
                    return true;
        }
        return false;
    }

    private void onFinish() {
        for (int i = 0; i < stage.size(); i++) {
            if (Integer.parseInt(stage.get(i).get(0)) >= 10 && Integer.parseInt(stage.get(i).get(0)) < 100) { //finish 확인
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
        for (int i = 0; i < stage.size(); i++)
            if (Integer.parseInt(stage.get(i).get(0)) == Integer.parseInt(stage.get(finish).get(0)) - 9) { //확인된 finish-9의 값을 갖는 동물
                if (stage.get(finish).get(1).equals(stage.get(i).get(1)) && stage.get(finish).get(2).equals(stage.get(i).get(2))) { //확인된finish와 같은 위치에 그 동물이 일치하면
                    return true;
                }
            }
        return false;
    }

    private boolean meetAnotherAnimal(int block, int xy, String direction) {
        boolean isMeet = false;
        switch (direction) {
            case "right":
            case "down":
                if (Integer.parseInt(stage.get(block).get(0)) < 10 && !stage.get(block).get(0).equals("0") && Float.parseFloat(stage.get(block).get(xy)) - 1 <= Float.parseFloat(stage.get(animal).get(xy)))
                    isMeet = true;
                break;
            case "left":
            case "up":
                if (Integer.parseInt(stage.get(block).get(0)) < 10 && !stage.get(block).get(0).equals("0") && Float.parseFloat(stage.get(block).get(xy)) >= Float.parseFloat(stage.get(animal).get(xy)))
                    isMeet = true;
                break;
        }
        return isMeet;
    }

    private boolean meetBlock(int block, int xy, String direction) {
        boolean isMeet = false;
        switch (direction) {
            case "right":
            case "down":
                if (Float.parseFloat(stage.get(block).get(xy)) <= Float.parseFloat(stage.get(animal).get(xy)))
                    isMeet = true;
                break;
            case "left":
            case "up":
                if (Float.parseFloat(stage.get(block).get(xy)) >= Float.parseFloat(stage.get(animal).get(xy)) - 1)
                    isMeet = true;
                break;
        }
        return isMeet;
    }

    private int setBlock(int xy, String direction) {
        int block = 0;
        int fixpoint = 1;
        if (xy == 1)
            fixpoint = 2;
        switch (direction) {
            case "right":
            case "down":
                block = 1;
                for (int i = 0; i < stage.size(); i++) {
                    if (stage.get(i).get(fixpoint).equals(stage.get(animal).get(fixpoint)) && Float.parseFloat(stage.get(i).get(xy)) >= Float.parseFloat(stage.get(animal).get(xy))) {
                        if (!stage.get(i).get(0).equals(stage.get(animal).get(0)) && Integer.parseInt(stage.get(i).get(0)) < 10 && Integer.parseInt(stage.get(i).get(0)) > 0) {//빈땅이 아니고 동물이 내가 아닐 때(다른 동물인 경우)
                            if (Integer.parseInt(stage.get(block).get(xy)) >= Integer.parseInt(stage.get(i).get(xy)))
                                block = i;//가장 가까이에 있는 벽의 정보를 저장
                        }
                        if (stage.get(i).get(xy + 2).equals("on")) {//벽이 있을 때
                            if (Integer.parseInt(stage.get(block).get(xy)) > Integer.parseInt(stage.get(i).get(xy)))
                                block = i;
                        }
                    }
                }
                break;

            case "left":
            case "up":
                for (int i = 0; i < stage.size(); i++) {
                    if (stage.get(i).get(fixpoint).equals(stage.get(animal).get(fixpoint)) && Float.parseFloat(stage.get(i).get(xy)) < Float.parseFloat(stage.get(animal).get(xy))) {//y좌표가 동물과 같고, x좌표가 동물보다 작을 때
                        if (!stage.get(i).get(0).equals(stage.get(animal).get(0)) && Integer.parseInt(stage.get(i).get(0)) < 10 && Integer.parseInt(stage.get(i).get(0)) > 0) {//빈땅이 아니고 동물이 내가 아닐 때(다른 동물인 경우)
                            if (Integer.parseInt(stage.get(block).get(xy)) <= Integer.parseInt(stage.get(i).get(xy)))
                                block = i;//가장 가까이에 있는 벽의 정보를 저장
                        }
                        if (stage.get(i).get(xy + 2).equals("on")) {//벽이 있을 때
                            if (Integer.parseInt(stage.get(block).get(xy)) <= Integer.parseInt(stage.get(i).get(xy)))
                                block = i;
                        }
                    }
                }
                break;
        }
        return block;
    }

    private void movingAnimal(int xy, String direction) {
        boolean warped = false;
        int block = setBlock(xy, direction);
        float moving = Float.parseFloat(stage.get(animal).get(xy));
        switch (direction) {
            case "right":
            case "down":
                while (Float.parseFloat(stage.get(animal).get(xy)) < boardSize - 1) { //테두리를 만날때까지 반복
                    if (activatedTrap()) break; //덫에 걸렸을 때
                    if (!warped)
                        if (activatedWarp(direction)) {
                            warped = true;
                            block = setBlock(xy, direction);
                            moving = Float.parseFloat(stage.get(animal).get(xy));
                        }
                    if (block != 1) {
                        if (meetAnotherAnimal(block, xy, direction)) break; //다른 동물을 만나면 멈춤
                        if (meetBlock(block, xy, direction)) break; //벽을 만나면 멈춤
                    }
                    moving += 0.0002;
                    stage.get(animal).set(xy, String.valueOf(moving));
                }
                break;
            case "left":
            case "up":
                while (Float.parseFloat(stage.get(animal).get(xy)) >= 0) { //테두리를 만날때까지 반복
                    if (activatedTrap()) break; //덫에 걸렸을 때
                    if (!warped)
                        if (activatedWarp(direction)) {
                            warped = true;
                            block = setBlock(xy, direction);
                            moving = Float.parseFloat(stage.get(animal).get(xy));
                        }
                    if (block != 0) {
                        if (meetAnotherAnimal(block, xy, direction)) break; //다른 동물을 만나면 멈춤
                        if (meetBlock(block, xy, direction)) break; //벽을 만나면 멈춤
                    }
                    moving -= 0.0002;
                    stage.get(animal).set(xy, String.valueOf(moving));
                }
                break;
        }
        stage.get(animal).set(xy, String.valueOf((Math.round(moving))));
    }

    private boolean activatedWarp(String direction) {
        boolean warped;
        int warpX_1 = 0, warpX_2 = 0, warpY_1 = 0, warpY_2 = 0;
        for (int i = 0; i < stage.size(); i++) {
            if (stage.get(i).get(0).equals("150")) {
                warpX_1 = Integer.parseInt(stage.get(i).get(1));
                warpY_1 = Integer.parseInt(stage.get(i).get(2));
            }
            if (stage.get(i).get(0).equals("200")) {
                warpX_2 = Integer.parseInt(stage.get(i).get(1));
                warpY_2 = Integer.parseInt(stage.get(i).get(2));
            }
        }
        if (Math.round(Float.parseFloat(stage.get(animal).get(1))) == warpX_1 && Math.round(Float.parseFloat(stage.get(animal).get(2))) == warpY_1) {
            switch (direction) {
                case "right":
                    if (warpX_2 + 1 >= boardSize) return false;
                    stage.get(animal).set(1, String.valueOf(warpX_2 + 1));
                    stage.get(animal).set(2, String.valueOf(warpY_2));
                    if (Integer.parseInt(stage.get(setBlock(1, "right")).get(1)) == warpX_2 + 1 && Integer.parseInt(stage.get(setBlock(1, "right")).get(2)) == warpY_2) {
                        stage.get(animal).set(1, String.valueOf(warpX_1 - 1));
                        stage.get(animal).set(2, String.valueOf(warpY_1));
                        meetAnotherAnimal(setBlock(1, "right"), 1, "right");
                        meetBlock(setBlock(1, "right"), 1, "right");
                    }
                    break;
                case "left":
                    if (warpX_2 - 1 <= 0) return false;
                    stage.get(animal).set(1, String.valueOf(warpX_2 - 0.5));
                    stage.get(animal).set(2, String.valueOf(warpY_2));
                    break;
                case "down":
                    if (warpY_2 + 1 >= boardSize) return false;
                    stage.get(animal).set(1, String.valueOf(warpX_2));
                    stage.get(animal).set(2, String.valueOf(warpY_2 + 0.5));
                    break;
                case "up":
                    if (warpX_2 - 1 <= 0) return false;
                    stage.get(animal).set(1, String.valueOf(warpX_2));
                    stage.get(animal).set(2, String.valueOf(warpY_2 - 0.5));
                    break;
            }
            warped = true;
        } else if (Math.round(Float.parseFloat(stage.get(animal).get(1))) == warpX_2 && Math.round(Float.parseFloat(stage.get(animal).get(2))) == warpY_2) {
            switch (direction) {
                case "right":
                    if (warpX_1 + 1 >= boardSize) return false;
                    stage.get(animal).set(1, String.valueOf(warpX_1 + 0.5));
                    stage.get(animal).set(2, String.valueOf(warpY_1));
                    break;
                case "left":
                    if (warpX_1 - 1 <= 0) return false;
                    stage.get(animal).set(1, String.valueOf(warpX_1 - 0.5));
                    stage.get(animal).set(2, String.valueOf(warpY_1));
                    break;
                case "down":
                    if (warpY_1 + 1 >= boardSize) return false;
                    stage.get(animal).set(1, String.valueOf(warpX_1));
                    stage.get(animal).set(2, String.valueOf(warpY_1 + 0.5));
                    break;
                case "up":
                    if (warpX_1 - 1 <= 0) return false;
                    stage.get(animal).set(1, String.valueOf(warpX_1));
                    stage.get(animal).set(2, String.valueOf(warpY_1 - 0.5));
                    break;
            }
            warped = true;
        } else warped = false;
        return warped;
    }
}