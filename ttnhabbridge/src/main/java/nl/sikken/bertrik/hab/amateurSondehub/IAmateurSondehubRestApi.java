package nl.sikken.bertrik.hab.amateurSondehub;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface definition for payload telemetry and listener telemetry towards AmateurSondehub.
 */
public interface IAmateurSondehubRestApi {

    @PUT("/habitat/_design/payload_telemetry/_update/add_listener/{doc_id}")
    Call<String> updateListener(@Path("doc_id") String docId, @Body String json);

    @GET("/_uuids")
    Call<UuidsList> getUuids(@Query("count") int count);
    
    @PUT("/habitat/{doc_id}")
    Call<UploadResult> uploadDocument(@Path("doc_id") String docId, @Body String document);

}
