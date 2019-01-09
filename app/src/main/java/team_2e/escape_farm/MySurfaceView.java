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

import static team_2e.escape_farm.Game_Stage.mhandler;
import static team_2e.escape_farm.Variable.boardSize;
import static team_2e.escape_farm.Variable.stage;
import static team_2e.escape_farm.Variable.stage_clear;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    Context mContext;
    SurfaceHolder mHolder;
    ImageThread mThread;
    Canvas mCanvas = null;
    Paint mPaint = new Paint();
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
        //((MainActivity) mContext).getMoney();
        //((MainActivity) mContext).getLife();
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
                if (Math.abs(firstX - lastX) <= spaceX && Math.abs(firstY - lastY) <= spaceY) {
                    for (int i = 0; i < stage.size(); i++)
                        if (!stage.get(i).get(0).equals("0") && (int) (firstX - blankX) / spaceX == Integer.parseInt(stage.get(i).get(1)) && (int) (firstY - blankY) / spaceY == Integer.parseInt(stage.get(i).get(2))) { //빈칸이 아니고 동물이 있는 땅을 눌렀을 때
                            if (stage.get(i).get(0).equals("10"))
                                break;
                            animal_clk = true;
                            animal = i;
                        }
                }
                return false;
        }
        return true;
    }

    public void onAnimalMove(float x_1, float y_1, float x_2, float y_2) {
        int block = 0;
        //if (Math.abs(x_1 - x_2) >= spaceX && (int) (y_2 - blankY) / spaceY == Integer.parseInt(stage.get(animal).get(2))) {
            if(Math.abs(x_1 - x_2)>= spaceX && ((Integer.parseInt(stage.get(animal).get(2))-2)*spaceY)+blankY < y_2 && y_2 < ((Integer.parseInt(stage.get(animal).get(2))+2)*spaceY)+blankY) {
            moveX = Float.parseFloat(stage.get(animal).get(1));
            if (x_1 > x_2) { // 좌측으로 이동
                for (int i = 0; i < stage.size(); i++) {
                    if (stage.get(i).get(2).equals(stage.get(animal).get(2)) && Integer.parseInt(stage.get(i).get(1)) <= Integer.parseInt(stage.get(animal).get(1))) {//y좌표가 동물과 같고, x좌표가 동물보다 작을 때
                        if (!stage.get(i).get(0).equals(stage.get(animal).get(0)) && Integer.parseInt(stage.get(i).get(0)) > 0) {//빈땅이 아니고 동물이 내가 아닐 때(다른 동물인 경우)
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
                    if (block != 0) {
                        if (!stage.get(block).get(0).equals("10") && !stage.get(block).get(0).equals("0") && Float.parseFloat(stage.get(block).get(1)) + 1 >= Float.parseFloat(stage.get(animal).get(1))) //다른 동물을 만나면 멈춤
                            break;
                        if (Float.parseFloat(stage.get(block).get(1)) >= Float.parseFloat(stage.get(animal).get(1))) //벽을 만나면 멈춤
                            break;
                    }
                    moveX -= 0.0001;
                    stage.get(animal).set(1, String.valueOf(moveX));
                }
            } else if (x_1 < x_2) { // 우측으로 이동
                block = 1;
                for (int i = 0; i < stage.size(); i++) {
                    if (stage.get(i).get(2).equals(stage.get(animal).get(2)) && Integer.parseInt(stage.get(i).get(1)) > Integer.parseInt(stage.get(animal).get(1))) {//y좌표가 동물과 같고, x좌표가 동물보다 클 때
                        if (!stage.get(i).get(0).equals(stage.get(animal).get(0)) && Integer.parseInt(stage.get(i).get(0)) > 0) {//빈땅이 아니고 동물이 내가 아닐 때(다른 동물인 경우)
                            if (Integer.parseInt(stage.get(block).get(1)) >= Integer.parseInt(stage.get(i).get(1)))
                                block = i;//가장 가까이에 있는 벽의 정보를 저장
                        }
                        if (stage.get(i).get(4).equals("1")) {//오른쪽벽이 있을 때
                            if (Integer.parseInt(stage.get(block).get(1)) >= Integer.parseInt(stage.get(i).get(1)))
                                block = i;
                        }
                    }
                }
                while (Float.parseFloat(stage.get(animal).get(1)) <= boardSize - 1) { //테두리를 만날때까지 반복
                    if (block != 1) {
                        if (!stage.get(block).get(0).equals("10") && !stage.get(block).get(0).equals("0") && Float.parseFloat(stage.get(block).get(1)) <= Float.parseFloat(stage.get(animal).get(1))) //다른 동물을 만나면 멈춤
                            break;
                        if (Float.parseFloat(stage.get(block).get(1)) <= Float.parseFloat(stage.get(animal).get(1)) + 1) //벽을 만나면 멈춤
                            break;
                    }
                    moveX += 0.0001;
                    stage.get(animal).set(1, String.valueOf(moveX));
                }
            }
            stage.get(animal).set(1, String.valueOf((Math.round(moveX))));
        } //else if ((int) (x_2 - blankX) / spaceX == Integer.parseInt(stage.get(animal).get(1)) && Math.abs(y_1 - y_2) >= spaceY) {
                else if(Math.abs(y_1-y_2)>=spaceY && ((Integer.parseInt(stage.get(animal).get(1))-2)*spaceX)+blankX < x_2 && x_2 < ((Integer.parseInt(stage.get(animal).get(1))+2)*spaceX)+blankX) {
            moveY = Float.parseFloat(stage.get(animal).get(2));
            if (y_1 < y_2) {//밑으로 이동
                block = 1;
                for (int i = 0; i < stage.size(); i++) {
                    if (stage.get(i).get(1).equals(stage.get(animal).get(1)) && Integer.parseInt(stage.get(i).get(2)) >= Integer.parseInt(stage.get(animal).get(2))) {//x좌표가 동물과 같고, y좌표가 동물보다 클 때
                        if (!stage.get(i).get(0).equals(stage.get(animal).get(0)) && Integer.parseInt(stage.get(i).get(0)) > 0) {//빈땅이 아니고 동물이 내가 아닐 때(다른 동물인 경우)
                            if (Integer.parseInt(stage.get(block).get(2)) >= Integer.parseInt(stage.get(i).get(2)))
                                block = i;//가장 가까이에 있는 위쪽 벽의 정보를 저장
                        }
                        if (stage.get(i).get(3).equals("1")) {//위쪽 벽이 있을 때
                            if (Integer.parseInt(stage.get(block).get(2)) >= Integer.parseInt(stage.get(i).get(2)))
                                block = i;
                        }
                    }
                }
                while (Float.parseFloat(stage.get(animal).get(2)) < boardSize - 1) { //테두리를 만날때까지 반복
                    if (block != 1) {
                        if (!stage.get(block).get(0).equals("10") && !stage.get(block).get(0).equals("0") && Float.parseFloat(stage.get(block).get(2)) - 1 <= Float.parseFloat(stage.get(animal).get(2))) //다른 동물을 만나면 멈춤
                            break;
                        if (Float.parseFloat(stage.get(block).get(2)) <= Float.parseFloat(stage.get(animal).get(2))) //벽을 만나면 멈춤
                            break;
                    }
                    moveY += 0.0001;
                    stage.get(animal).set(2, String.valueOf(moveY));
                }
            } else if (y_1 > y_2) {//위로 이동
                for (int i = 0; i < stage.size(); i++) {

                    if (stage.get(i).get(1).equals(stage.get(animal).get(1)) && Integer.parseInt(stage.get(i).get(2)) < Integer.parseInt(stage.get(animal).get(2))) {//x좌표가 동물과 같고, y좌표가 동물보다 작을 때
                        if (!stage.get(i).get(0).equals(stage.get(animal).get(0)) && Integer.parseInt(stage.get(i).get(0)) > 0) {//빈땅이 아니고 동물이 내가 아닐 때(다른 동물인 경우)
                            if (Integer.parseInt(stage.get(block).get(2)) <= Integer.parseInt(stage.get(i).get(2)))
                                block = i;//가장 가까이에 있는 위쪽 벽의 정보를 저장
                        }
                        if (stage.get(i).get(3).equals("1")) {//위쪽 벽이 있을 때
                            if (Integer.parseInt(stage.get(block).get(2)) <= Integer.parseInt(stage.get(i).get(2)))
                                block = i;
                        }
                    }
                }
                while (Float.parseFloat(stage.get(animal).get(2)) >= 0) { //테두리를 만날때까지 반복
                    if (block != 0) {
                        if (!stage.get(block).get(0).equals("10") && !stage.get(block).get(0).equals("0") && Float.parseFloat(stage.get(block).get(2)) >= Float.parseFloat(stage.get(animal).get(2))) //다른 동물을 만나면 멈춤
                            break;
                        if (Float.parseFloat(stage.get(block).get(2)) >= Float.parseFloat(stage.get(animal).get(2)) - 1) { //벽을 만나면 멈춤
                            moveY += 0.0001;
                            break;
                        }
                    }
                    moveY -= 0.0001;
                    stage.get(animal).set(2, String.valueOf(moveY));
                }
            }
            stage.get(animal).set(2, String.valueOf((Math.round(moveY))));
        }
        onFinish();
    }

    public void onFinish() {
        int finish;
        for (int i = 0; i < stage.size(); i++)
            if (stage.get(i).get(0).equals("10")) {
                if (stage.get(i).get(1).equals(stage.get(animal).get(1)) && stage.get(i).get(2).equals(stage.get(animal).get(2)))
                    mhandler.sendEmptyMessage(stage_clear);
            }
    }

    class ImageThread extends Thread {
        Bitmap animal_1, animal_2, ground, wall_down, wall_right, finish;

        public ImageThread() {
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
            ground = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ground);
            ground = Bitmap.createScaledBitmap(ground, (int) spaceX, (int) spaceY, true);
            wall_right = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.wall);
            wall_right = Bitmap.createScaledBitmap(wall_right, (int) spaceX / 10, (int) spaceY, true);
            wall_down = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.wall);
            wall_down = Bitmap.createScaledBitmap(wall_down, (int) spaceX, (int) spaceY / 10, true);
            finish = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.finish);
            finish = Bitmap.createScaledBitmap(finish, (int) spaceX, (int) spaceY, true);
        }

        public void run() {
            while (true) {
                mCanvas = mHolder.lockCanvas();
                try {
                    synchronized (mHolder) {
                        //mCanvas.drawBitmap(Back, 0, 0, null);
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
                        //stage.get(0)[0] 종류 ()[1] x, ()[2] y, ()[3] 위쪽벽, ()[4] 우측벽
                        for (int i = 0; i < stage.size(); i++) {
                            if (stage.get(i).get(3).equals("1"))
                                mCanvas.drawBitmap(wall_down, (spaceX * Integer.parseInt(stage.get(i).get(1))) + blankX, (spaceY * (Integer.parseInt(stage.get(i).get(2)) + 1)) + blankY, null);
                            if (stage.get(i).get(4).equals("1"))
                                mCanvas.drawBitmap(wall_right, (spaceX * Integer.parseInt(stage.get(i).get(1))) + blankX, (spaceY * Integer.parseInt(stage.get(i).get(2))) + blankY, null);
                            if (stage.get(i).get(0).equals("1"))
                                mCanvas.drawBitmap(animal_1, (spaceX * Float.parseFloat(stage.get(i).get(1))) + blankX, (spaceY * Float.parseFloat(stage.get(i).get(2))) + blankY, null);
                            if (stage.get(i).get(0).equals("2"))
                                mCanvas.drawBitmap(animal_2, (spaceX * Float.parseFloat(stage.get(i).get(1))) + blankX, (spaceY * Float.parseFloat(stage.get(i).get(2))) + blankY, null);
                            if (stage.get(i).get(0).equals("10"))
                                mCanvas.drawBitmap(finish, (spaceX * Float.parseFloat(stage.get(i).get(1))) + blankX, (spaceY * Float.parseFloat(stage.get(i).get(2))) + blankY, null);
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