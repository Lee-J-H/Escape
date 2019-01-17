package team_2e.escape_farm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import static team_2e.escape_farm.Variable.boardSize;
import static team_2e.escape_farm.Variable.mAct;
import static team_2e.escape_farm.Variable.moveCount;
import static team_2e.escape_farm.Variable.stage;
import static team_2e.escape_farm.Variable.stageCount;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    Context mContext;
    SurfaceHolder mHolder;
    ImageThread mThread;
    Canvas mCanvas = null;
    float Width, Height, firstX, firstY, lastX, lastY;
    int spaceX, spaceY, blankX, blankY, animal;
    boolean animal_clk = false;
    float moveX, moveY;

    public MySurfaceView(Context context) {
        super(context);
        mContext = context;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mThread = new MySurfaceView.ImageThread();
    }

    public MySurfaceView(Context context, AttributeSet attr) {
        super(context, attr);
        mContext = context;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mThread = new MySurfaceView.ImageThread();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // Surface가 만들어질 때 호출됨
        mThread.start();
        ((Game_Stage) mContext).setMoveCount();
        ((Game_Stage) mContext).setStageCount();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        // Surface가 변경될 때 호출됨
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        // Surface가 종료될 때 호출됨
        try {
            mThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstX = event.getX();
                firstY = event.getY();
                for (int i = 0; i < stage.size(); i++)
                    if (!stage.get(i).get(0).equals("0") && (int) (firstX - blankX) / spaceX == Integer.parseInt(stage.get(i).get(1)) && (int) (firstY - blankY) / spaceY == Integer.parseInt(stage.get(i).get(2))) { //빈칸이 아니고 동물이 있는 땅을 눌렀을 때
                        if (Integer.parseInt(stage.get(i).get(0)) < 10 && !stage.get(i).get(0).equals("0")) {
                            animal_clk = true;
                            animal = i;
                        }
                        break;

                    }
                break;
            case MotionEvent.ACTION_MOVE:
                return true;
            case MotionEvent.ACTION_UP:
                lastX = event.getX();
                lastY = event.getY();
                if (animal_clk) {
                    onAnimalMove(firstX, firstY, lastX, lastY);
                }
                animal_clk = false;
                return false;
        }
        return true;
    }

    public void onAnimalMove(float x_1, float y_1, float x_2, float y_2) {
        int block = 0;
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
                        if (stage.get(i).get(4).equals("1")) {//오른쪽벽이 있을 때
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
                        if (stage.get(i).get(4).equals("1")) {//오른쪽벽이 있을 때
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
                        if (stage.get(i).get(3).equals("1")) {//아래쪽 벽이 있을 때
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
                        if (stage.get(i).get(3).equals("1")) {//아래쪽 벽이 있을 때
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

    public boolean activatedTrap(int animal) {
        for (int i = 0; i < stage.size(); i++) {
            if (Integer.parseInt(stage.get(i).get(0)) == 100)
                if (Math.round(Float.parseFloat(stage.get(animal).get(1))) == Float.parseFloat(stage.get(i).get(1)) && Math.round(Float.parseFloat(stage.get(animal).get(2))) == Float.parseFloat(stage.get(i).get(2)))
                    return true;
        }
        return false;
    }

    public void onFinish() {
        for (int i = 0; i < stage.size(); i++) {
            if (Integer.parseInt(stage.get(i).get(0)) >= 10 && Integer.parseInt(stage.get(i).get(0)) < 100) { //finish 확인
                if (!matchFinish(i)) return;
            }
        }
        //mAct.finish();
        ((Game_Stage) mContext).Dialog();
        moveCount = 0;
        stageCount = 0;
    }

    public boolean matchFinish(int finish) {
        for (int i = 0; i < stage.size(); i++)
            if (Integer.parseInt(stage.get(i).get(0)) == Integer.parseInt(stage.get(finish).get(0)) - 9) { //확인된 finish-9의 값을 갖는 동물
                if (stage.get(finish).get(1).equals(stage.get(i).get(1)) && stage.get(finish).get(2).equals(stage.get(i).get(2))) { //확인된finish와 같은 위치에 그 동물이 일치하면
                    return true;
                }
            }
        return false;
    }

    class ImageThread extends Thread {
        Bitmap animal_1, animal_2, animal_3, animal_4, animal_5, ground, wall_down, wall_right, finish_1, finish_2, finish_3, finish_4, finish_5, trap, Back;

        private ImageThread() {
            WindowManager manager = (WindowManager)
                    mContext.getSystemService((Context.WINDOW_SERVICE));
            Display display = manager.getDefaultDisplay();
            Point sizePoint = new Point();
            display.getSize(sizePoint);
            Width = sizePoint.x;
            Height = sizePoint.y;
            spaceX = (int) Width / 10;
            spaceY = (int) Height / 20;
            animal_1 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.animal_1);
            animal_1 = Bitmap.createScaledBitmap(animal_1, (int) spaceX, (int) spaceY, true);
            animal_2 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.animal_2);
            animal_2 = Bitmap.createScaledBitmap(animal_2, (int) spaceX, (int) spaceY, true);
            animal_3 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.animal_3);
            animal_3 = Bitmap.createScaledBitmap(animal_3, (int) spaceX, (int) spaceY, true);
            animal_4 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.animal_4);
            animal_4 = Bitmap.createScaledBitmap(animal_4, (int) spaceX, (int) spaceY, true);
            animal_5 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.animal_5);
            animal_5 = Bitmap.createScaledBitmap(animal_5, (int) spaceX, (int) spaceY, true);
            ground = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ground);
            ground = Bitmap.createScaledBitmap(ground, (int) spaceX, (int) spaceY, true);
            wall_right = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.wall);
            wall_right = Bitmap.createScaledBitmap(wall_right, (int) spaceX / 10, (int) spaceY, true);
            wall_down = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.wall);
            wall_down = Bitmap.createScaledBitmap(wall_down, (int) spaceX, (int) spaceY / 10, true);
            finish_1 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.finish_1);
            finish_1 = Bitmap.createScaledBitmap(finish_1, (int) spaceX, (int) spaceY, true);
            finish_2 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.finish_2);
            finish_2 = Bitmap.createScaledBitmap(finish_2, (int) spaceX, (int) spaceY, true);
            finish_3 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.finish_3);
            finish_3 = Bitmap.createScaledBitmap(finish_3, (int) spaceX, (int) spaceY, true);
            finish_4 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.finish_4);
            finish_4 = Bitmap.createScaledBitmap(finish_4, (int) spaceX, (int) spaceY, true);
            finish_5 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.finish_5);
            finish_5 = Bitmap.createScaledBitmap(finish_5, (int) spaceX, (int) spaceY, true);
            trap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.trap);
            trap = Bitmap.createScaledBitmap(trap, (int) spaceX, (int) spaceY, true);
            Back = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.backimg);
            Back = Bitmap.createScaledBitmap(Back, (int) Width, (int) Height, true);

        }

        public void run() {
            while (true) {
                mCanvas = mHolder.lockCanvas();
                try {
                    synchronized (mHolder) {
                        mCanvas.drawBitmap(Back, 0, 0, null);
                        switch (boardSize) {
                            case 4:
                                blankX = spaceX * 3;
                                blankY = spaceY * 7;
                                break;
                            case 6:
                                blankX = spaceX * 2;
                                blankY = spaceY * 6;
                                break;
                            case 8:
                                blankX = spaceX;
                                blankY = spaceY * 5;
                                break;
                        }
                        for (int i = 0; i < boardSize; i++) {
                            for (int j = 0; j < boardSize; j++)
                                mCanvas.drawBitmap(ground, (spaceX * i) + blankX, (spaceY * j) + blankY, null);
                        }
                        //stage.get(0)[0] 종류 ()[1] x, ()[2] y, ()[3] 아래쪽벽, ()[4] 오른쪽벽
                        for (int i = 0; i < stage.size(); i++) {
                            if (stage.get(i).get(3).equals("1"))
                                mCanvas.drawBitmap(wall_down, (spaceX * Integer.parseInt(stage.get(i).get(1))) + blankX, (spaceY * (Integer.parseInt(stage.get(i).get(2)) + 1)) + blankY, null);
                            if (stage.get(i).get(4).equals("1"))
                                mCanvas.drawBitmap(wall_right, (spaceX * (Integer.parseInt(stage.get(i).get(1)) + 1)) + blankX, (spaceY * Integer.parseInt(stage.get(i).get(2))) + blankY, null);
                            if (stage.get(i).get(0).equals("1"))
                                mCanvas.drawBitmap(animal_1, (spaceX * Float.parseFloat(stage.get(i).get(1))) + blankX, (spaceY * Float.parseFloat(stage.get(i).get(2))) + blankY, null);
                            if (stage.get(i).get(0).equals("2"))
                                mCanvas.drawBitmap(animal_2, (spaceX * Float.parseFloat(stage.get(i).get(1))) + blankX, (spaceY * Float.parseFloat(stage.get(i).get(2))) + blankY, null);
                            if (stage.get(i).get(0).equals("3"))
                                mCanvas.drawBitmap(animal_3, (spaceX * Float.parseFloat(stage.get(i).get(1))) + blankX, (spaceY * Float.parseFloat(stage.get(i).get(2))) + blankY, null);
                            if (stage.get(i).get(0).equals("4"))
                                mCanvas.drawBitmap(animal_4, (spaceX * Float.parseFloat(stage.get(i).get(1))) + blankX, (spaceY * Float.parseFloat(stage.get(i).get(2))) + blankY, null);
                            if (stage.get(i).get(0).equals("5"))
                                mCanvas.drawBitmap(animal_5, (spaceX * Float.parseFloat(stage.get(i).get(1))) + blankX, (spaceY * Float.parseFloat(stage.get(i).get(2))) + blankY, null);
                            if (stage.get(i).get(0).equals("10"))
                                mCanvas.drawBitmap(finish_1, (spaceX * Float.parseFloat(stage.get(i).get(1))) + blankX, (spaceY * Float.parseFloat(stage.get(i).get(2))) + blankY, null);
                            if (stage.get(i).get(0).equals("11"))
                                mCanvas.drawBitmap(finish_2, (spaceX * Float.parseFloat(stage.get(i).get(1))) + blankX, (spaceY * Float.parseFloat(stage.get(i).get(2))) + blankY, null);
                            if (stage.get(i).get(0).equals("12"))
                                mCanvas.drawBitmap(finish_3, (spaceX * Float.parseFloat(stage.get(i).get(1))) + blankX, (spaceY * Float.parseFloat(stage.get(i).get(2))) + blankY, null);
                            if (stage.get(i).get(0).equals("13"))
                                mCanvas.drawBitmap(finish_4, (spaceX * Float.parseFloat(stage.get(i).get(1))) + blankX, (spaceY * Float.parseFloat(stage.get(i).get(2))) + blankY, null);
                            if (stage.get(i).get(0).equals("14"))
                                mCanvas.drawBitmap(finish_5, (spaceX * Float.parseFloat(stage.get(i).get(1))) + blankX, (spaceY * Float.parseFloat(stage.get(i).get(2))) + blankY, null);
                            if (stage.get(i).get(0).equals("100"))
                                mCanvas.drawBitmap(trap, (spaceX * Float.parseFloat(stage.get(i).get(1))) + blankX, (spaceY * Float.parseFloat(stage.get(i).get(2))) + blankY, null);
                        }
                    }
                } finally {
                    if (mCanvas == null) return;
                    mHolder.unlockCanvasAndPost(mCanvas);
                }
            }
        }
    }
}