package database_utils;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.covert.verify360.BeanClasses.NewCasesBean;
import com.covert.verify360.BeanClasses.PendingCasesBean;

@Database(entities = {NewCasesBean.class, PendingCasesBean.class}, version = 1,exportSchema = false)
public abstract class DatabaseHandler extends RoomDatabase {
    private static DatabaseHandler INSTANCE;

    public abstract NewCasesDao newCasesDao();
    public abstract PendingCasesDao pendingCasesDao();

    public static DatabaseHandler getDatabaseInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (DatabaseHandler.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext()
                            , DatabaseHandler.class, "db_verify_360.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
