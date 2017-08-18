package nl.sikken.bertrik.hab.habitat;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Interface definition for payload telemetry
 * 
 * Publish this on "/habitat" for example.
 *
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface IHabitatRestApi {

    @Path("/habitat/_design/payload_telemetry/_update/add_listener/{doc_id}")
    @PUT
    public String updateListener(@PathParam("doc_id") String docId, String json);

    @Path("/_uuids")
    @GET
    public UuidsList getUuids(@QueryParam("count") int count);
    
    @Path("/habitat/{doc_id}")
    @PUT
    public UploadResult uploadDocument(@PathParam("doc_id") String docId, String document);

}
