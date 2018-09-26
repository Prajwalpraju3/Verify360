package database_utils;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.covert.verify360.BeanClasses.NewCasesBean;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

@Dao
public interface NewCasesDao {
    @Query("Select * from new_cases")
    LiveData<List<NewCasesBean>> getCasesFromDB();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCases(List<NewCasesBean> newCasesBeans);

    @Query("delete from new_cases")
    void deleteAllCases();
}
