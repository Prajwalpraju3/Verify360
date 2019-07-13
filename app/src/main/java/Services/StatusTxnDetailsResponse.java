package Services;

public interface StatusTxnDetailsResponse {

    void responseDetails(StatusTxnResponse statusTxnResponse);
    void errorResponse(String error);
}
