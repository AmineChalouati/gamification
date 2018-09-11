package org.exoplatform.addons.gamification.service.effective;

import org.exoplatform.addons.gamification.entities.domain.effective.GamificationContextEntity;
import org.exoplatform.addons.gamification.entities.domain.effective.GamificationContextItemEntity;
import org.exoplatform.addons.gamification.service.dto.effective.GamificationContextHolder;
import org.exoplatform.addons.gamification.storage.dao.GamificationDAO;
import org.exoplatform.addons.gamification.storage.dao.GamificationItemDAO;
import org.exoplatform.commons.api.persistence.ExoTransactional;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GamificationService {

    private static final Log LOG = ExoLogger.getLogger(GamificationService.class);

    protected final GamificationDAO gamificationDAO;

    protected final GamificationItemDAO gamificationItemDAO;

    public GamificationService(GamificationDAO gamificationDAO, GamificationItemDAO gamificationItemDAO) {

        this.gamificationDAO = gamificationDAO;

        this.gamificationItemDAO = gamificationItemDAO;
    }

    /**
     * Find a GamificationContext by username
     *
     * @param username : gamification's username param
     * @return an instance of GamificationContextDTO
     */
    @ExoTransactional
    public GamificationContextEntity findGamificationContextByUsername(String username) {

        GamificationContextEntity entity = null;

        try {
            //--- Get Entity from DB
            entity = gamificationDAO.findGamificationContextByUsername(username);


        } catch (Exception e) {
            LOG.error("Error to find Gamification entity with username : {}", username, e);
        }
        return entity;

    }

    /**
     * Add GamificationContext to DB
     *
     * @param gamificationContextHolder : GamificationContext to be saved
     */
    @ExoTransactional
    public void saveGamificationContext(GamificationContextHolder gamificationContextHolder) {

        try {

            if (gamificationContextHolder.isNew()) {

                gamificationDAO.create(gamificationContextHolder.getGamificationContextEntity());

            } else {

                gamificationDAO.update(gamificationContextHolder.getGamificationContextEntity());

            }

        } catch (Exception e) {
            LOG.error("Error to save GamificationUserReputation for user {}", gamificationContextHolder.getGamificationContextEntity().getUsername(), e);
        }
    }

    /**
     *
     * @param gamificationContextEntity
     */
    @ExoTransactional
    public void updateUserGamificationContextScore(GamificationContextEntity gamificationContextEntity) {

        try {

            gamificationDAO.update(gamificationContextEntity);

        } catch (Exception e) {
            LOG.error("Error to update GamificationUserReputation for username {}", gamificationContextEntity.getUsername(), e);
        }
    }

    /**
     * @param username : username to load
     * @return long score user
     */
    @ExoTransactional
    public long getUserGlobalScore(String username) {

        long userScore = 0;

        GamificationContextEntity entity = null;

        try {
            //--- Get Entity from DB
            entity = gamificationDAO.getUserGlobalScore(username);


        } catch (Exception e) {
            LOG.error("Error to find Gamification entity with username : {}", username, e.getMessage());
        }

        if (entity != null) {
            userScore = entity.getScore();
        }

        return userScore;
    }

    /**
     * @param gamificationSearch : search context
     * @return Lits of GamificationContextEntity
     */
    @ExoTransactional
    public List<GamificationContextEntity> filter(GamificationSearch gamificationSearch) {

        List<GamificationContextEntity> gamificationContextEntities = null;

        if (LOG.isDebugEnabled()) {
            LOG.debug("Filtering leaderboard based on domain name : {}", gamificationSearch.getDomain());
        }


        try {

            // Get overall leaderboard
            if (gamificationSearch.getDomain().equalsIgnoreCase("all")) {

                gamificationContextEntities = gamificationDAO.findOverallLeaderboard();


            } else { // Get leaderboard by domain

                List<GamificationContextEntity> leaderboardList = gamificationDAO.findLeaderboardByDomain(gamificationSearch.getDomain());

                gamificationContextEntities = new ArrayList<GamificationContextEntity>();

                if (leaderboardList != null && !leaderboardList.isEmpty()) {

                    GamificationContextEntity gamificationContextEntity = null;

                    List<String> red = null;

                    for (GamificationContextEntity leaderBoard : leaderboardList) {

                        Set<GamificationContextItemEntity> items = leaderBoard.getGamificationItems();

                        if (items != null && !items.isEmpty()) {

                            red = new ArrayList<>();

                            int userScore = 0;

                            for (GamificationContextItemEntity gamificationContextItemEntity : items) {

                                if (gamificationContextItemEntity.getZone().equalsIgnoreCase(gamificationSearch.getDomain())) {


                                    if (!red.contains(gamificationContextItemEntity.getOpType())) {

                                        userScore += gamificationContextItemEntity.getScore();
                                        red.add(gamificationContextItemEntity.getOpType());

                                    }

                                }

                            }

                            gamificationContextEntity = new GamificationContextEntity();
                            gamificationContextEntity.setUsername(leaderBoard.getUsername());
                            gamificationContextEntity.setScore(userScore);
                            gamificationContextEntities.add(gamificationContextEntity);

                        }
                    }

                    gamificationContextEntities.sort((GamificationContextEntity g1, GamificationContextEntity g2) -> (int) g2.getScore() - (int) g1.getScore());

                }

            }

        } catch (Exception e) {
            LOG.error("Error to filter leaderboard by domain : {} and by netowrk : {}", gamificationSearch.getDomain(), gamificationSearch.getNetwork(), e);
        }

        return gamificationContextEntities;
    }

    @ExoTransactional
    public Set<GamificationContextItemEntity> getUserGamification(String userId) {

        Set<GamificationContextItemEntity> gamificationContextItemEntitySet = null;

        try {
            //--- Get Entity from DB
            GamificationContextEntity gamificationContextEntity = gamificationDAO.getUserGamification(userId);

            // Get Context items if exists
            if (gamificationContextEntity != null) {

                gamificationContextItemEntitySet = new HashSet<>();

                for (GamificationContextItemEntity item : gamificationContextEntity.getGamificationItems()) {

                    if (checkElementExist(gamificationContextItemEntitySet, item.getOpType())) {
                        gamificationContextItemEntitySet.add(item);
                    }

                }

            }


        } catch (Exception e) {
            LOG.error("Error to load effective gamification for user {} ", userId, e);
        }

        return gamificationContextItemEntitySet;
    }

    private boolean checkElementExist(Set<GamificationContextItemEntity> gamificationContextItemEntities, String opType) {

        for (GamificationContextItemEntity item : gamificationContextItemEntities) {

            if (item.getOpType().equalsIgnoreCase(opType)) return false;

        }
        return true;

    }

    @ExoTransactional
    public List<Piechart> findStatsByUserId(String userId) {

        List<Piechart> leaderboardList = null;

        try {
            //--- Get Stats
            leaderboardList = gamificationDAO.findStatsByUserId(userId);

        } catch (Exception e) {
            LOG.error("Error to load gamification stats for user {} ", userId, e);
        }

        return leaderboardList;
    }

    @ExoTransactional
    public List<GamificationContextItemEntity> findGamificationItemsByUserIdAndDomain(String userId, String domain) {

        List<GamificationContextItemEntity> gamificationItems = null;

        try {
            //--- Get Stats
            gamificationItems = gamificationItemDAO.findGamificationItemsByUserIdAndDomain(userId, domain);

        } catch (Exception e) {
            LOG.error("Error to load gamification items for user {} ", userId, e);
        }

        return gamificationItems;
    }

    @ExoTransactional
    public boolean deleteItem(GamificationContextItemEntity gamificationContextItemEntity) {

        try {

            return gamificationItemDAO.deleteItem(gamificationContextItemEntity);

        } catch (Exception e) {
            LOG.error("Error to delete gamification item with Optype {} for user {} ", gamificationContextItemEntity.getOpType(), gamificationContextItemEntity.getGamificationUserEntity().getUsername(), e);
            return false;
        }

    }

    /**
     * Get UserReputation object by name
     * @param username username to load
     * @return int number of gamification
     */
    @ExoTransactional
    public int loadGamification(String username) {

        int rank = 0;

        GamificationContextEntity entity = null;

        try {
            // Get all users from DB
            List<GamificationContextEntity> entities = gamificationDAO.findAllLeaderboard();

            // Get username
            GamificationContextEntity item = entities.stream()
                    .filter(g -> username.equals(g.getUsername()))
                    .findAny()
                    .orElse(null);

            return (entities.indexOf(item)+1);
        } catch (Exception e) {
            LOG.error("Error to find Gamification entity with username : {}", username, e.getMessage());

        }
        return rank;

    }


}
