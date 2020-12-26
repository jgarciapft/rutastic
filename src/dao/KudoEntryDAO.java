package dao;

import model.KudoEntry;

import java.util.List;
import java.util.Map;

/**
 * Extended public interface for all DAO implementations for model class {@code KudoEntry}. Specifies additional
 * methods all DAO implementations should support
 *
 * @see dao.implementations.DAOImplementation
 * @see KudoEntry
 */
public interface KudoEntryDAO extends DAO<KudoEntry> {

    /**
     * @param userId The user identifier
     * @return Returns all kudo votes issued by the user identified by its user id
     */
    List<KudoEntry> getAllByUser(long userId);

    /**
     * @param username The username that identifies an user
     * @return Returns all kudo votes issued by the user identified by its username
     */
    List<KudoEntry> getAllByUser(String username);

    /**
     * @param routeId The route identifier
     * @return Returns all kudo votes ever given to a specific route
     */
    List<KudoEntry> getAllByRoute(long routeId);

    /**
     * Request a map of route IDs mapped to the kudo entries generated by a certain user
     *
     * @param userId The ID of the user to who the kudo entries belong
     * @return Map of route IDs to kudo entries by user or null if the user couldn't be found
     */
    Map<Long, KudoEntry> getRouteIDMappedKudoEntriesForUser(long userId);

}
