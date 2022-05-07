package nl.sikken.bertrik.hab.amateurSondehub;

import nl.sikken.bertrik.hab.UploadResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

/**
 * Interface definition for payload telemetry and listener telemetry towards
 * AmateurSondehub.
 */
public interface IAmateurSondehubRestApi {
    @PUT("/amateur/telemetry")
    Call<UploadResult> uploadDocument(@Body String document);

}
