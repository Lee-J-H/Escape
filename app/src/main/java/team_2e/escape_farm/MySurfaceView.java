package team_2e.escape_farm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import static team_2e.escape_farm.Variable.boardSize;
import static team_2e.escape_farm.Variable.stage;
import static team_2e.escape_farm.Variable.animal;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    Context mContext;
    SurfaceHolder mHolder;
    ImageThread mThread;
    Canvas mCanvas = null;
    float Width, Height, firstX, firstY, lastX, lastY;
    int spaceX, spaceY, blankX, blankY;
    boolean animal_clk = false;

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
                    if (!stage.get(i).get(0).equals("0") && (int)(firstX - blankX) / spaceX == Integer.parseInt(stage.get(i).get(1)) && (int) (firstY - blankY) / spaceY == Integer.parseInt(stage.get(i).get(2))) { //빈칸이 아니고 동물이 있는 땅을 눌렀을 때
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
                    int xy=0;
                    String direction = "";
                    if (Math.abs(firstX - lastX) >= spaceX && ((Integer.parseInt(stage.get(animal).get(2)) - 2.5) * spaceY) + blankY < lastY && lastY < ((Integer.parseInt(stage.get(animal).get(2)) + 2.5) * spaceY) + blankY) {
                        xy = 1;
                        if (firstX > lastX) direction = "left"; // 좌측으로 이동
                        else if (firstX < lastX) direction = "right";// 우측으로 이동
                    } else if (Math.abs(firstY - lastY) >= spaceY && ((Integer.parseInt(stage.get(animal).get(1)) - 2.5) * spaceX) + blankX < lastX && lastX < ((Integer.parseInt(stage.get(animal).get(1)) + 2.5) * spaceX) + blankX) {
                        xy = 2;
                        if (firstY < lastY) direction = "down";//밑으로 이동
                        else if (firstY > lastY) direction = "up"; //위로 이동
                    }
                    GameObject onGame = new GameObject(mContext);
                    onGame.onAnimalMove(xy, direction);
                }
                animal_clk = false;
                return false;
        }
        return true;
    }


    class ImageThread extends Thread {
        Bitmap animal_1, animal_2, animal_3, animal_4, animal_5, ground, wall_down, wall_right, finish_1, finish_2, finish_3, finish_4, finish_5, trap, Back, warp;

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
            warp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.warp_1);
            warp = Bitmap.createScaledBitmap(warp, (int) spaceX, (int) spaceY, true);

        }
        private void doDraw(){
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
            //stage.get(0)[0] 종류 ()[1] x, ()[2] y, ()[3] 우측벽, ()[4] 아래벽
            for (int i = 0; i < stage.size(); i++) {
                if (stage.get(i).get(3).equals("on")) //오른쪽 벽
                    mCanvas.drawBitmap(wall_right, (spaceX * (Integer.parseInt(stage.get(i).get(1)) + 0.9f)) + blankX, (spaceY * Integer.parseInt(stage.get(i).get(2))) + blankY, null);
                if (stage.get(i).get(4).equals("on")) //아래쪽 벽
                    mCanvas.drawBitmap(wall_down, (spaceX * Integer.parseInt(stage.get(i).get(1))) + blankX, (spaceY * (Integer.parseInt(stage.get(i).get(2)) + 0.9f)) + blankY, null);
                if (stage.get(i).get(0).equals("1")) //동물1
                    mCanvas.drawBitmap(animal_1, (spaceX * Float.parseFloat(stage.get(i).get(1))) + blankX, (spaceY * Float.parseFloat(stage.get(i).get(2))) + blankY, null);
                if (stage.get(i).get(0).equals("2")) //동물2
                    mCanvas.drawBitmap(animal_2, (spaceX * Float.parseFloat(stage.get(i).get(1))) + blankX, (spaceY * Float.parseFloat(stage.get(i).get(2))) + blankY, null);
                if (stage.get(i).get(0).equals("3")) //동물3
                    mCanvas.drawBitmap(animal_3, (spaceX * Float.parseFloat(stage.get(i).get(1))) + blankX, (spaceY * Float.parseFloat(stage.get(i).get(2))) + blankY, null);
                if (stage.get(i).get(0).equals("4")) //동물4
                    mCanvas.drawBitmap(animal_4, (spaceX * Float.parseFloat(stage.get(i).get(1))) + blankX, (spaceY * Float.parseFloat(stage.get(i).get(2))) + blankY, null);
                if (stage.get(i).get(0).equals("5")) //동물5
                    mCanvas.drawBitmap(animal_5, (spaceX * Float.parseFloat(stage.get(i).get(1))) + blankX, (spaceY * Float.parseFloat(stage.get(i).get(2))) + blankY, null);
                if (stage.get(i).get(0).equals("10")) //동물1-피니시
                    mCanvas.drawBitmap(finish_1, (spaceX * Float.parseFloat(stage.get(i).get(1))) + blankX, (spaceY * Float.parseFloat(stage.get(i).get(2))) + blankY, null);
                if (stage.get(i).get(0).equals("11")) //동물2-피니시
                    mCanvas.drawBitmap(finish_2, (spaceX * Float.parseFloat(stage.get(i).get(1))) + blankX, (spaceY * Float.parseFloat(stage.get(i).get(2))) + blankY, null);
                if (stage.get(i).get(0).equals("12")) //동물3-피니시
                    mCanvas.drawBitmap(finish_3, (spaceX * Float.parseFloat(stage.get(i).get(1))) + blankX, (spaceY * Float.parseFloat(stage.get(i).get(2))) + blankY, null);
                if (stage.get(i).get(0).equals("13")) //동물4-피니시
                    mCanvas.drawBitmap(finish_4, (spaceX * Float.parseFloat(stage.get(i).get(1))) + blankX, (spaceY * Float.parseFloat(stage.get(i).get(2))) + blankY, null);
                if (stage.get(i).get(0).equals("14")) //동물5-피니시
                    mCanvas.drawBitmap(finish_5, (spaceX * Float.parseFloat(stage.get(i).get(1))) + blankX, (spaceY * Float.parseFloat(stage.get(i).get(2))) + blankY, null);
                if (stage.get(i).get(0).equals("100")) //덫
                    mCanvas.drawBitmap(trap, (spaceX * Float.parseFloat(stage.get(i).get(1))) + blankX, (spaceY * Float.parseFloat(stage.get(i).get(2))) + blankY, null);
                if (stage.get(i).get(0).equals("150")) //워프1
                    mCanvas.drawBitmap(warp, (spaceX * Float.parseFloat(stage.get(i).get(1))) + blankX, (spaceY * Float.parseFloat(stage.get(i).get(2))) + blankY, null);
                if (stage.get(i).get(0).equals("200")) //워프1-1
                    mCanvas.drawBitmap(warp, (spaceX * Float.parseFloat(stage.get(i).get(1))) + blankX, (spaceY * Float.parseFloat(stage.get(i).get(2))) + blankY, null);
            }
        }

        public void run() {
            while (true) {
                mCanvas = mHolder.lockCanvas();
                try {
                    synchronized (mHolder) {
                        mCanvas.drawBitmap(Back, 0, 0, null);
                        doDraw();
                    }
                } finally {
                    if (mCanvas == null) return;
                    mHolder.unlockCanvasAndPost(mCanvas);
                }
            }
        }
    }
}