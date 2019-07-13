package database_utils;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.covert.verify360.BeanClasses.NewCasesBean;
import com.covert.verify360.BeanClasses.PendingCasesBean;

import java.util.List;

@Dao
public interface PendingCasesDao {
    @Query("Select * from pending_cases")
    LiveData<List<PendingCasesBean>> getCasesFromDB();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCases(List<PendingCasesBean> pendingCasesBeanList);

    @Query("delete from pending_cases")
    void deleteAllCases();
}
