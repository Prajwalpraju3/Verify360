package Services;

import com.covert.verify360.BeanClasses.CancelledReasonsList;

import java.util.List;

public interface PassCancelReasons {

    void ReasonsObtained(List<CancelledReasonsList> cancelledReasonsLists);
    void Error(String error);
}
