package resources;

import dao.UserDAO;
import dao.factories.DAOAbstractFactory;
import dao.implementations.DAOImplJDBC;
import model.User;
import model.statistic.UserStatistic;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("/usuarios")
public class UsersResource {

    private static final Logger logger = Logger.getLogger(UsersResource.class.getName());

    @Context
    ServletContext sc;

    @Context
    UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUsersJSON() {
        UserDAO userDAO = DAOAbstractFactory.get().impl(DAOImplJDBC.class).forModel(User.class);

        List<User> allUsers = userDAO.getAll(); // Get all users

        // Clear all personal info, just leave the username

        allUsers.forEach(user -> user.setId(0));

        logger.info("[REST] Retrieving all users, sending only usernames");

        return allUsers;
    }

    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserJSON(@PathParam("username") String username, @Context HttpServletRequest req) {

        UserDAO userDAO = DAOAbstractFactory.get().impl(DAOImplJDBC.class).forModel(User.class);
        HttpSession session = req.getSession();

        // Validate the username

        if (username == null || username.trim().isEmpty())
            throw new WebApplicationException("Nombre de usuario inválido", Response.Status.BAD_REQUEST);

        // AUTHORISATION FILTER. The logged user can only retrieve himself

        User loggedUser = (User) session.getAttribute("user");

        if (loggedUser.getUsername().equals(username)) {
            User requestedUser = userDAO.getByUsername(username);

            if (requestedUser != null) {
                logger.info("[REST] Retrieving the user with username (" + username + ")");

                return requestedUser;
            } else { // The user couldn't be found at the backend
                throw new WebApplicationException("No se encontró al usuario solicitado", Response.Status.NOT_FOUND);
            }
        } else { // Insufficient privileges. The logged user can't retrieve the requested user
            throw new WebApplicationException("Su usuario no puede solicitar la información de este perfil",
                    Response.Status.UNAUTHORIZED);
        }
    }

    @GET
    @Path("/estadisticas")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserStatistic> usersStatisticsQueryHandler(@QueryParam("e") String requestedStat) {

        UserDAO userDAO = DAOAbstractFactory.get().impl(DAOImplJDBC.class).forModel(User.class);

        // Validate there's an user stat being requested

        if (requestedStat == null || requestedStat.trim().isEmpty())
            throw new WebApplicationException("No se ha solicitado ninguna estadística de usuario",
                    Response.Status.BAD_REQUEST);

        logger.info("[REST] Serving user statistics (" + requestedStat + ")");

        // Get the user stat being requested

        if (requestedStat.equals("top5UsuariosPorTopRutas")) { // Serve top 5 users by top monthly routes

            List<UserStatistic> top5UsersByTopMonthlyRoutes = userDAO.getTopUsersByTopMonthlyRoutes();

            // Get first 5 results

            top5UsersByTopMonthlyRoutes = top5UsersByTopMonthlyRoutes
                    .stream()
                    .limit(5)
                    .collect(Collectors.toList());

            return top5UsersByTopMonthlyRoutes;
        } else if (requestedStat.equals("top5UsuariosPorMediaKudos")) { // Serve top 5 users by average kudo ratings of their routes

            List<UserStatistic> top5UsersByAvgKudos = userDAO.getTopUsersByAvgKudos();

            // Get first 5 results

            top5UsersByAvgKudos = top5UsersByAvgKudos
                    .stream()
                    .limit(5)
                    .collect(Collectors.toList());

            return top5UsersByAvgKudos;
        } else { // Other unhandled user stats
            return null;
        }
    }

    @DELETE
    @Path("/{username}")
    public Response deleteUser(@PathParam("username") String username, @Context HttpServletRequest req) {

        UserDAO userDAO = DAOAbstractFactory.get().impl(DAOImplJDBC.class).forModel(User.class);
        ArrayList<String> validationMessages = new ArrayList<>();
        HttpSession session = req.getSession();

        // Validate the username

        if (username == null || username.trim().isEmpty())
            throw new WebApplicationException("Nombre de usuario inválido", Response.Status.BAD_REQUEST);

        // Check if the user could be found at the backend

        User registeredUser = userDAO.getByUsername(username);
        if (registeredUser != null) {

            // AUTHORISATION FILTER. The logged user is the only one who can delete his profile

            User loggedUser = (User) session.getAttribute("user");
            if (loggedUser.getUsername().equals(username)) {

                // Try deleting the requested user

                boolean deletionSuccessful = userDAO.deleteById(registeredUser.getId());

                if (deletionSuccessful) {
                    logger.info("[REST] DELETED THE USER WITH USERNAME (" + username + ")");
                    return Response.noContent().build(); // Return code 204 - No content
                } else { // An error occurred while deleting the requested user
                    throw new WebApplicationException("Ocurrió un error al eliminar el usuario solicitado",
                            Response.Status.INTERNAL_SERVER_ERROR);
                }
            } else { // Insufficient privileges
                throw new WebApplicationException("Este usuario no tiene permisos para eliminar el perfil solicitado",
                        Response.Status.UNAUTHORIZED);
            }
        } else { // User not found
            throw new WebApplicationException("No se encuentra el usuario solicitado", Response.Status.NOT_FOUND);
        }
    }
}
