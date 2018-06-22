package org.exoplatform.gamification.rest;

import com.google.api.client.util.IOUtils;
import org.apache.commons.io.FileUtils;
import org.exoplatform.common.http.HTTPStatus;
import org.exoplatform.commons.file.model.FileItem;
import org.exoplatform.commons.file.services.FileService;
import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.gamification.service.configuration.BadgeService;
import org.exoplatform.gamification.service.configuration.RuleService;
import org.exoplatform.gamification.service.dto.configuration.BadgeDTO;
import org.exoplatform.gamification.service.dto.configuration.RuleDTO;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.resource.ResourceContainer;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.social.core.identity.model.Profile;
import org.exoplatform.social.core.model.AvatarAttachment;
import org.exoplatform.upload.UploadResource;
import org.exoplatform.upload.UploadService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Path("/gamification/badges")
@Produces(MediaType.APPLICATION_JSON)
public class ManageBadgesEndpoint implements ResourceContainer {

    private static final Log LOG = ExoLogger.getLogger(ManageBadgesEndpoint.class);

    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    private final CacheControl cacheControl;

    protected BadgeService badgeService = null;

    protected RuleService ruleService = null;

    protected FileService fileService = null;

    protected UploadService uploadService = null;

    public ManageBadgesEndpoint() {

        this.cacheControl = new CacheControl();

        cacheControl.setNoCache(true);

        cacheControl.setNoStore(true);

        badgeService = CommonsUtils.getService(BadgeService.class);

        ruleService = CommonsUtils.getService(RuleService.class);

        fileService = CommonsUtils.getService(FileService.class);
        uploadService = CommonsUtils.getService(UploadService.class);

    }

    @GET
    @RolesAllowed("users")
    @Path("/all")
    public Response getAllBadges(@Context UriInfo uriInfo) {

        ConversationState conversationState = ConversationState.getCurrent();

        if (conversationState != null) {

            try {
                List<BadgeDTO> allBadges = badgeService.getAllBadges();

                return Response.ok(allBadges, MediaType.APPLICATION_JSON).cacheControl(cacheControl).build();

            } catch (Exception e) {

                LOG.error("Error listing all badges ", e);

                return Response.serverError()
                        .cacheControl(cacheControl)
                        .entity("Error listing all badges")
                        .build();
            }

        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .cacheControl(cacheControl)
                    .entity("Unauthorized user")
                    .build();
        }

    }

    @POST
    @RolesAllowed("administrators")
    @Path("/add")
    public Response addBadge(@Context UriInfo uriInfo, BadgeDTO badgeDTO) {

        ConversationState conversationState = ConversationState.getCurrent();

        if (conversationState != null) {

            String currentUserName = conversationState.getIdentity().getUserId();

            try {

                // Compute rule's data
                badgeDTO.setId(null);
                badgeDTO.setCreatedBy(currentUserName);
                badgeDTO.setLastModifiedBy(currentUserName);
                badgeDTO.setCreatedDate(formatter.format(new Date()));
                /** Gamification rely on FileService, thus we don't need to persist icon in Gamification DB*/
                //badgeDTO.setIcon(badgeDTO.getIcon());

                badgeDTO.setLastModifiedDate(formatter.format(new Date()));

                if (badgeDTO.getUploadId() != null) {
                    UploadResource uploadResource = uploadService.getUploadResource(badgeDTO.getUploadId());

                    /** Upload badge's icon into DB */
                    FileItem fileItem = null;

                    fileItem = new FileItem(null,
                            badgeDTO.getTitle().toLowerCase(),
                            uploadResource.getMimeType(),
                            "gamification",
                            (long)uploadResource.getUploadedSize(),
                            new Date(),
                            currentUserName,
                            false,
                            new FileInputStream(uploadResource.getStoreLocation()));
                    fileItem = fileService.writeFile(fileItem);
                    /** END upload */

                    badgeDTO.setIconFileId(fileItem.getFileInfo().getId());
                }

                //--- Add badge
                badgeDTO = badgeService.addBadge(badgeDTO);

                return Response.ok().cacheControl(cacheControl).entity(badgeDTO).build();


            } catch (Exception e) {

                LOG.error("Error adding new badge {} by {} ", badgeDTO.getTitle(), currentUserName, e);

                return Response.serverError()
                        .cacheControl(cacheControl)
                        .entity("Error adding new badge")
                        .build();

            }

        } else {

            return Response.status(Response.Status.UNAUTHORIZED)
                    .cacheControl(cacheControl)
                    .entity("Unauthorized user")
                    .build();
        }

    }

    @PUT
    @RolesAllowed("administrators")
    @Path("/update")
    public Response updateBadge(@Context UriInfo uriInfo, @Context HttpServletRequest request, BadgeDTO badgeDTO) {

        ConversationState conversationState = ConversationState.getCurrent();

        if (conversationState != null) {

            String currentUserName = conversationState.getIdentity().getUserId();
            try {

                //TODO : Load locale
                Locale lc = request.getLocale();

                // Compute rule's data
                badgeDTO.setCreatedBy(currentUserName);
                badgeDTO.setLastModifiedBy(currentUserName);
                badgeDTO.setLastModifiedDate(formatter.format(new Date()));

                //--- Add rule
                badgeDTO = badgeService.updateBadge(badgeDTO);

                return Response.ok().cacheControl(cacheControl).entity(badgeDTO).build();

            } catch (Exception e) {

                LOG.error("Error updating badge {} by {} ", badgeDTO.getTitle(), currentUserName, e);

                return Response.serverError()
                        .cacheControl(cacheControl)
                        .entity("Error adding new badge")
                        .build();
            }

        } else {

            return Response.status(Response.Status.UNAUTHORIZED)
                    .cacheControl(cacheControl)
                    .entity("Unauthorized user")
                    .build();
        }


    }

    @DELETE
    @RolesAllowed("administrators")
    @Path("/delete")
    public Response deleteBadge(@Context UriInfo uriInfo, @QueryParam("badgeTitle") String badgeTitle) {

        ConversationState conversationState = ConversationState.getCurrent();

        if (conversationState != null) {
            try {
                //--- Remove the rule
                badgeService.deleteBadge(badgeTitle);


                return Response.ok("Deleted " + badgeTitle, MediaType.APPLICATION_JSON).cacheControl(cacheControl).build();

            } catch (Exception e) {

                LOG.error("Error deleting badge {} by {} ", badgeTitle, conversationState.getIdentity().getUserId(), e);

                return Response.status(HTTPStatus.INTERNAL_ERROR).cacheControl(cacheControl).build();
            }

        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .cacheControl(cacheControl)
                    .entity("Unauthorized user")
                    .build();
        }



    }

}
