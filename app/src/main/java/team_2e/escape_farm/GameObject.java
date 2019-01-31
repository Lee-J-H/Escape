package team_2e.escape_farm;

import android.content.Context;

import static team_2e.escape_farm.CheckedStage.onCheckStage;
import static team_2e.escape_farm.CheckedStage.onClearStage;
import static team_2e.escape_farm.Variable.boardSize;
import static team_2e.escape_farm.Variable.moveCount;
import static team_2e.escape_farm.Variable.stage;
import static team_2e.escape_farm.Variable.stageCount;

public class GameObject {

    Context mContext;

    public GameObject(Context context) {
        mContext = context;
    }

    public void onAnimalMove(float x_1, float y_1, float x_2, float y_2, int animal, float spaceX, float spaceY, float blankX, float blankY) {
        int block = 0;
        float moveX, moveY;
        String checkX = stage.get(animal).get(1);
        String checkY = stage.get(animal).get(2); //이동 전 동물의 x,y값 저장
        if (Math.abs(x_1 - x_2) >= spaceX && ((Integer.parseInt(stage.get(animal).get(2)) - 2.5) * spaceY) + blankY < y_2 && y_2 < ((Integer.parseInt(stage.get(animal).get(2)) + 2.5) * spaceY) + blankY) {
            moveX = Float.parseFloat(stage.get(animal).get(1));
            if (x_1 > x_2) { // 좌측으로 이동
                for (int i = 0; i < stage.size(); i++) {
                    if (stage.get(i).get(2).equals(stage.get(animal).get(2)) && Integer.parseInt(stage.get(i).get(1)) < Integer.parseInt(stage.get(animal).get(1))) {//y좌표가 동물과 같고, x좌표가 동물보다 작을 때
                        if (!stage.get(i).get(0).equals(stage.get(animal).get(0)) && Integer.parseInt(stage.get(i).get(0)) < 10 && Integer.parseInt(stage.get(i).get(0)) > 0) {//빈땅이 아니고 동물이 내가 아닐 때(다른 동물인 경우)
                            if (Integer.parseInt(stage.get(block).get(1)) <= Integer.parseInt(stage.get(i).get(1)))
                                block = i;//가장 가까이에 있는 벽의 정보를 저장
                        }
                        if (stage.get(i).get(4).equals("right")) {//오른쪽벽이 있을 때
                            if (Integer.parseInt(stage.get(block).get(1)) <= Integer.parseInt(stage.get(i).get(1)))
                                block = i;
                        }
                    }
                }
                while (Float.parseFloat(stage.get(animal).get(1)) >= 0) { //테두리를 만날때까지 반복
                    if (activatedTrap(animal)) break; //덫에 걸렸을 때
                    if (block != 0) {
                        if (Integer.parseInt(stage.get(block).get(0)) < 10 && !stage.get(block).get(0).equals("0") && Float.parseFloat(stage.get(block).get(1)) >= Float.parseFloat(stage.get(animal).get(1))) //다른 동물을 만나면 멈춤
                            break;
                        if (Float.parseFloat(stage.get(block).get(1)) >= Float.parseFloat(stage.get(animal).get(1)) - 1) //벽을 만나면 멈춤
                            break;
                    }
                    moveX -= 0.0002;
                    stage.get(animal).set(1, String.valueOf(moveX));
                }
            } else if (x_1 < x_2) { // 우측으로 이동
                block = 1;
                for (int i = 0; i < stage.size(); i++) {
                    if (stage.get(i).get(2).equals(stage.get(animal).get(2)) && Integer.parseInt(stage.get(i).get(1)) >= Integer.parseInt(stage.get(animal).get(1))) {//y좌표가 동물과 같고, x좌표가 동물보다 클 때
                        if (!stage.get(i).get(0).equals(stage.get(animal).get(0)) && Integer.parseInt(stage.get(i).get(0)) < 10 && Integer.parseInt(stage.get(i).get(0)) > 0) {//빈땅이 아니고 동물이 내가 아닐 때(다른 동물인 경우)
                            if (Integer.parseInt(stage.get(block).get(1)) >= Integer.parseInt(stage.get(i).get(1)))
                                block = i;//가장 가까이에 있는 벽의 정보를 저장
                        }
                        if (stage.get(i).get(4).equals("right")) {//오른쪽벽이 있을 때
                            if (Integer.parseInt(stage.get(block).get(1)) > Integer.parseInt(stage.get(i).get(1)))
                                block = i;
                        }
                    }
                }
                while (Float.parseFloat(stage.get(animal).get(1)) < boardSize - 1) { //테두리를 만날때까지 반복
                    if (activatedTrap(animal)) break; //덫에 걸렸을 때
                    if (block != 1) {
                        if (Integer.parseInt(stage.get(block).get(0)) < 10 && !stage.get(block).get(0).equals("0") && Float.parseFloat(stage.get(block).get(1)) - 1 <= Float.parseFloat(stage.get(animal).get(1))) //다른 동물을 만나면 멈춤
                            break;
                        if (Float.parseFloat(stage.get(block).get(1)) <= Float.parseFloat(stage.get(animal).get(1))) //벽을 만나면 멈춤
                            break;
                    }
                    moveX += 0.0002;
                    stage.get(animal).set(1, String.valueOf(moveX));
                }
            }
            stage.get(animal).set(1, String.valueOf((Math.round(moveX))));
            if (!stage.get(animal).get(1).equals(checkX))
                moveCount++; //동물이 이동을 했으면 moveCount 증가
        } else if (Math.abs(y_1 - y_2) >= spaceY && ((Integer.parseInt(stage.get(animal).get(1)) - 2.5) * spaceX) + blankX < x_2 && x_2 < ((Integer.parseInt(stage.get(animal).get(1)) + 2.5) * spaceX) + blankX) {
            moveY = Float.parseFloat(stage.get(animal).get(2));
            if (y_1 < y_2) {//밑으로 이동
                block = 1;
                for (int i = 0; i < stage.size(); i++) {
                    if (stage.get(i).get(1).equals(stage.get(animal).get(1)) && Integer.parseInt(stage.get(i).get(2)) >= Integer.parseInt(stage.get(animal).get(2))) {//x좌표가 동물과 같고, y좌표가 동물보다 클 때
                        if (!stage.get(i).get(0).equals(stage.get(animal).get(0)) && Integer.parseInt(stage.get(i).get(0)) < 10 && Integer.parseInt(stage.get(i).get(0)) > 0) {//빈땅이 아니고 동물이 내가 아닐 때(다른 동물인 경우)
                            if (Integer.parseInt(stage.get(block).get(2)) >= Integer.parseInt(stage.get(i).get(2)))
                                block = i;//가장 가까이에 있는 아래쪽 벽의 정보를 저장
                        }
                        if (stage.get(i).get(3).equals("down")) {//아래쪽 벽이 있을 때
                            if (Integer.parseInt(stage.get(block).get(2)) > Integer.parseInt(stage.get(i).get(2)))
                                block = i;
                        }
                    }
                }
                while (Float.parseFloat(stage.get(animal).get(2)) < boardSize - 1) { //테두리를 만날때까지 반복
                    if (activatedTrap(animal)) break; //덫에 걸렸을 때
                    if (block != 1) {
                        if (Integer.parseInt(stage.get(block).get(0)) < 10 && !stage.get(block).get(0).equals("0") && Float.parseFloat(stage.get(block).get(2)) - 1 <= Float.parseFloat(stage.get(animal).get(2))) //다른 동물을 만나면 멈춤
                            break;
                        if (Float.parseFloat(stage.get(block).get(2)) <= Float.parseFloat(stage.get(animal).get(2))) //벽을 만나면 멈춤
                            break;
                    }
                    moveY += 0.0002;
                    stage.get(animal).set(2, String.valueOf(moveY));
                }
            } else if (y_1 > y_2) {//위로 이동
                for (int i = 0; i < stage.size(); i++) {
                    if (stage.get(i).get(1).equals(stage.get(animal).get(1)) && Integer.parseInt(stage.get(i).get(2)) < Integer.parseInt(stage.get(animal).get(2))) {//x좌표가 동물과 같고, y좌표가 동물보다 작을 때
                        if (!stage.get(i).get(0).equals(stage.get(animal).get(0)) && Integer.parseInt(stage.get(i).get(0)) < 10 && Integer.parseInt(stage.get(i).get(0)) > 0) {//빈땅이 아니고 동물이 내가 아닐 때(다른 동물인 경우)
                            if (Integer.parseInt(stage.get(block).get(2)) <= Integer.parseInt(stage.get(i).get(2)))
                                block = i;//가장 가까이에 있는 아래쪽 벽의 정보를 저장
                        }
                        if (stage.get(i).get(3).equals("down")) {//아래쪽 벽이 있을 때
                            if (Integer.parseInt(stage.get(block).get(2)) <= Integer.parseInt(stage.get(i).get(2)))
                                block = i;
                        }
                    }
                }
                while (Float.parseFloat(stage.get(animal).get(2)) >= 0) { //테두리를 만날때까지 반복
                    if (activatedTrap(animal)) break; //덫에 걸렸을 때
                    if (block != 0) {
                        if (Integer.parseInt(stage.get(block).get(0)) < 10 && !stage.get(block).get(0).equals("0") && Float.parseFloat(stage.get(block).get(2)) >= Float.parseFloat(stage.get(animal).get(2))) //다른 동물을 만나면 멈춤
                            break;
                        if (Float.parseFloat(stage.get(block).get(2)) >= Float.parseFloat(stage.get(animal).get(2)) - 1) { //벽을 만나면 멈춤
                            break;
                        }
                    }
                    moveY -= 0.0002;
                    stage.get(animal).set(2, String.valueOf(moveY));
                }
            }
            stage.get(animal).set(2, String.valueOf((Math.round(moveY))));
            if (!stage.get(animal).get(2).equals(checkY))
                moveCount++; //동물이 이동을 했으면 moveCount 증가
        }
        ((Game_Stage) mContext).setMoveCount(); //Game_Stage Activity의 setMoveCount함수 호출(뷰의 이동횟수 재설정)
        onFinish();
    }

    private boolean activatedTrap(int animal) {
        for (int i = 0; i < stage.size(); i++) {
            if (Integer.parseInt(stage.get(i).get(0)) == 100)
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
}
