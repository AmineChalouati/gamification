package org.exoplatform.gamification.service.configuration;

import org.exoplatform.commons.api.persistence.ExoTransactional;
import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.gamification.entities.domain.configuration.BadgeEntity;
import org.exoplatform.gamification.service.dto.configuration.BadgeDTO;
import org.exoplatform.gamification.service.mapper.BadgeMapper;
import org.exoplatform.gamification.storage.dao.BadgeDAO;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import java.util.List;

public class BadgeService {

    private static final Log LOG = ExoLogger.getLogger(BadgeService.class);

    protected final BadgeDAO badgeStorage;
    protected final BadgeMapper badgeMapper;

    public BadgeService() {
        this.badgeStorage = CommonsUtils.getService(BadgeDAO.class);
        this.badgeMapper = CommonsUtils.getService(BadgeMapper.class);

    }

    /**
     * Find a BadgeEntity by title
     * @param badgeTitle : badge title
     * @return an instance BadgeDTO
     */
    public BadgeDTO findBadgeByTitle(String badgeTitle) {

        try {
            //--- Get Entity from DB
            BadgeEntity entity = badgeStorage.findBadgeByTitle(badgeTitle);
            //--- Convert Entity to DTO
            if (entity != null) {
                return badgeMapper.badgeToBadgeDTO(entity);
            }

        } catch (Exception e) {
            LOG.error("Error to find Badge entity with title : {}", badgeTitle, e.getMessage());
        }
        return null;

    }

    /**
     * Return all badges within the DB
     * @return a list of BadgeDTO
     */
    public List<BadgeDTO> getAllBadges() {
        try {
            //--- load all Rules
            List<BadgeEntity> badges = badgeStorage.getAllBadges();
            if (badges != null) {
                return badgeMapper.badgesToBadgeDTOs(badges);
            }

        } catch (Exception e) {
            LOG.error("Error to find Badges", e.getMessage());
        }
        return null;

    }

    /**
     * Add Badge to DB
     * @param badgeDTO : an object of type RuleDTO
     * @return BadgeDTO object
     */
    @ExoTransactional
    public BadgeDTO addBadge (BadgeDTO badgeDTO) {

        BadgeEntity badgeEntity = null;

        try {

            badgeEntity = badgeStorage.create(badgeMapper.badgeDTOToBadge(badgeDTO));

        } catch (Exception e) {
            LOG.error("Error to create badge with title {}", badgeDTO.getTitle() , e);
        }

        return badgeMapper.badgeToBadgeDTO(badgeEntity);
    }

    /**
     * Update Badge to DB
     * @param badgeDTO : an instance of type BadgeDTO
     * @return BadgeDTO object
     */
    @ExoTransactional
    public BadgeDTO updateBadge (BadgeDTO badgeDTO) {

        BadgeEntity badgeEntity = null;

        try {

            badgeEntity = badgeStorage.update(badgeMapper.badgeDTOToBadge(badgeDTO));

        } catch (Exception e) {
            LOG.error("Error to update with title {}", badgeDTO.getTitle() , e);
        }

        return badgeMapper.badgeToBadgeDTO(badgeEntity);
    }

    /**
     * Delete a BadgeEntity using the title
     * @param badgeTitle : badge title
     */
    @ExoTransactional
    public void deleteBadge (String badgeTitle) {

        try {

            badgeStorage.deleteBadgeByTitle(badgeTitle);

        } catch (Exception e) {
            LOG.error("Error to delete badge with title {}", badgeTitle, e);
        }


    }
}
