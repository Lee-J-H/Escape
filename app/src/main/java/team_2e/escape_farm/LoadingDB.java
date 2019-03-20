package team_2e.escape_farm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static team_2e.escape_farm.Variable.animals;
import static team_2e.escape_farm.Variable.boardSize;
import static team_2e.escape_farm.Variable.stageCount;
import static team_2e.escape_farm.Variable.stageNum;

public class LoadingDB extends SQLiteOpenHelper {

    private Context context;

    public LoadingDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /** * */
    public void selectDB() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(" select * from Stage_Table where Stage = " + stageCount + ";", null);
        stageNum = cursor.getCount();
        animals = new Animal[stageNum];
            for (int i = 0; i < stageNum; i++) {
                if(cursor.moveToNext()) {
                animals[i] = new Animal(cursor.getString(1), cursor.getString(4));
                animals[i].setX((cursor.getFloat(2)));
                animals[i].setY((cursor.getFloat(3)));
                Log.d("Stage_Table", "result:" + animals[i].getKind() + "/" + animals[i].getX() + "/" + animals[i].getY() + "/" + animals[i].getBlock());
            }
        }
        boardSize = (int)animals[1].getX()+1;
        db.close();
    }
}
