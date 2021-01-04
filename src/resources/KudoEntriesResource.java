package resources;

import dao.KudoEntryDAO;
import dao.factories.DAOAbstractFactory;
import dao.implementations.DAOImplJDBC;
import model.KudoEntry;
import model.User;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@Path("/kudos")
public class KudoEntriesResource {

    private static final Logger logger = Logger.getLogger(UsersResource.class.getName());

    @Context
    ServletContext sc;

    @Context
    UriInfo uriInfo;

    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<KudoEntry> getUserKudoEntriesJSON(
            @PathParam("username") String username, @Context HttpServletRequest req) {

        KudoEntryDAO kudoEntryDAO = DAOAbstractFactory.get().impl(DAOImplJDBC.class).forModel(KudoEntry.class);
        HttpSession session = req.getSession();

        // AUTHORISATION FILTER. The logged user can only retrieve his kudo entries

        User loggedUser = (User) session.getAttribute("user");
        if (loggedUser.getUsername().equals(username)) {
            logger.info("[REST] Serving kudo entries by user with ID (" + username + ")");
            return kudoEntryDAO.getAllByUser(username); // Return the collection of kudo entries for the requested user
        } else { // Insufficient privileges
            throw new WebApplicationException("Su usario no tiene permisos para recuperar las entradas kudo del usuario solicitado",
                    Response.Status.UNAUTHORIZED);
        }
    }

    @GET
    @Path("/{username}/{routeId : [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public KudoEntry getUserKudoEntryForRouteJSON(
            @PathParam("username") String username, @PathParam("routeId") long routeId, @Context HttpServletRequest req) {

        KudoEntryDAO kudoEntryDAO = DAOAbstractFactory.get().impl(DAOImplJDBC.class).forModel(KudoEntry.class);
        HttpSession session = req.getSession();

        // AUTHORISATION FILTER. The logged user can only retrieve his kudo entries

        User loggedUser = (User) session.getAttribute("user");
        if (loggedUser.getUsername().equals(username)) {
            logger.info("[REST] Serving kudo entry by user with ID (" + username + ") to route with ID (" + routeId + ")");
            return kudoEntryDAO.getByPKey(username, routeId); // Return the kudo entry by the requested user to the requested route
        } else { // Insufficient privileges
            throw new WebApplicationException("Su usario no tiene permisos para recuperar las entradas kudo del usuario solicitado",
                    Response.Status.UNAUTHORIZED);
        }
    }
}
