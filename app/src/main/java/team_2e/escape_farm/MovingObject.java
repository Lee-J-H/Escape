package team_2e.escape_farm;

import android.content.Context;

import static team_2e.escape_farm.GameObject.activatedTrap;
import static team_2e.escape_farm.GameObject.checkKind;
import static team_2e.escape_farm.Variable.animal;
import static team_2e.escape_farm.Variable.animals;
import static team_2e.escape_farm.Variable.direction;
import static team_2e.escape_farm.Variable.moveCount;

public class MovingObject {
    Context mContext;
    private GameObject GameObject;

    public MovingObject(Context context) {
        mContext = context;
    }

    public void onAnimalMove() {
        float checkX = animals[animal].getX(), checkY = animals[animal].getY(); //  이동 전 동물의 x,y값 저장
        if (!direction.equals("")) {
            movingAnimal(); //동물 이동
        }
        if (!(animals[animal].getX() == checkX && animals[animal].getY() == checkY))
            moveCount++;//동물이 이동을 했으면 moveCount 증가
        ((Game_Stage) mContext).setMoveCount(); //Game_Stage Activity의 setMoveCount함수 호출(뷰에 이동횟수 재설정)
        GameObject = new GameObject(mContext);
        GameObject.onFinish();
    }

    private int setBlock() {
        int block = 0;
        switch (direction) {
            case "left":
            case "right":
                if (direction.equals("right"))
                    block = 1;
                for (int i = 2; i < animals.length; i++) {
                    if (animals[i].getY() == animals[animal].getY()) {
                        if (animals[i].getKind().equals(animals[animal].getKind())) continue;
                        if (animals[i].getBlock().equals("down")) continue;
                        if (Math.abs(animals[animal].getX() - animals[i].getX()) <= Math.abs(animals[animal].getX() - animals[block].getX())) { //기존 벽보다 가까이에 있을 때
                            if (direction.equals("left")) {
                                if (animals[i].getX() < animals[animal].getX())
                                    block = i;
                            } else {
                                if (animals[i].getX() >= animals[animal].getX()) {
                                    block = i;
                                }
                            }
                        }
                    }
                }
                break;
            case "up":
            case "down":
                if (direction.equals("down"))
                    block = 1;
                for (int i = 2; i < animals.length; i++) {
                    if (animals[i].getX() == animals[animal].getX()) {
                        if (animals[i].getKind().equals(animals[animal].getKind())) continue;
                        if (animals[i].getBlock().equals("right")) continue;
                        if (Math.abs(animals[animal].getY() - animals[i].getY()) <= Math.abs(animals[animal].getY() - animals[block].getY())) { //기존 벽보다 가까이에 있을 때
                            if (direction.equals("up")) {
                                if (animals[i].getY() < animals[animal].getY())
                                    block = i;
                            } else {
                                if (animals[i].getY() >= animals[animal].getY()) {
                                    block = i;
                                }
                            }
                        }
                    }
                }
                break;
        }
        return block;
    }

    private void movingAnimal() {
        int block = setBlock();
        float movingPoint, blockPoint;
        if (activatedTrap()) return; //덫에 걸렸는지 여부 확인
        blockPoint = checkKind(block);
        switch (direction) {
            case "left":
                movingPoint = animals[animal].getX();
                while (animals[animal].getX() >= blockPoint) {
                    movingPoint -= 0.000001f;
                    animals[animal].setX(movingPoint);
                }
                animals[animal].setX((float) Math.round(movingPoint));
                break;
            case "right":
                movingPoint = animals[animal].getX();
                while (animals[animal].getX() <= blockPoint) {
                    movingPoint += 0.000001f;
                    animals[animal].setX(movingPoint);
                }
                animals[animal].setX((float) Math.round(movingPoint));
                break;
            case "up":
                movingPoint = animals[animal].getY();
                while (animals[animal].getY() >= blockPoint) {
                    movingPoint -= 0.000001f;
                    animals[animal].setY(movingPoint);
                }
                animals[animal].setY((float) Math.round(movingPoint));
                break;
            case "down":
                movingPoint = animals[animal].getY();
                while (animals[animal].getY() <= blockPoint) {
                    movingPoint += 0.000001f;
                    animals[animal].setY(movingPoint);
                }
                animals[animal].setY((float) Math.round(movingPoint));
                break;
        }
    }
}
