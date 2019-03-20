package team_2e.escape_farm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CreateDB extends SQLiteOpenHelper {
    private Context context;

    public CreateDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    /**
     * Database가 존재하지 않을 때, 딱 한번 실행된다.
     * DB를 만드는 역할을 한다.
     * * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // String 보다 StringBuffer가 Query 만들기 편하다.
        StringBuffer sb = new StringBuffer();
        sb.append(" CREATE TABLE Stage_Table( ");
        //sb.append(" _ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append(" Stage TEXT, ");
        sb.append(" Animal TEXT, ");
        sb.append(" X integer, ");
        sb.append(" Y integer, ");
        sb.append(" Block TEXT ) "); // SQLite Database로 쿼리 실행
        db.execSQL(sb.toString());
    }

    /**
     * Application의 버전이 올라가서
     * Table 구조가 변경되었을 때 실행된다. *
     *
     * @param db * @param oldVersion
     *           * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table Stage_Table");
        onCreate(db);

    }

    /** * */
    public void putInData() {
        SQLiteDatabase db = getReadableDatabase();
        StringBuffer sb = new StringBuffer();
        sb.append("Insert into Stage_Table(Stage, Animal, X, Y, Block) Values(1,'none',0,0,'none');");
        db.execSQL(sb.toString());
        db.close();
    }
}