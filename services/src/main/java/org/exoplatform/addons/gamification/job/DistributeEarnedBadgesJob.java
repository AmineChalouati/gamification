package org.exoplatform.addons.gamification.job;

import org.exoplatform.addons.gamification.service.effective.GamificationService;
import org.exoplatform.addons.gamification.service.effective.ProfileReputation;
import org.exoplatform.commons.persistence.impl.EntityManagerService;
import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.profile.ProfileFilter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

public class DistributeEarnedBadgesJob implements Job {
    private static final Log LOG = ExoLogger.getLogger(ComputeEarnedBadgesJob.class.getName());
    private int initialNbUsers;
    private int userLimit = 100;
    private boolean   runBuild = true;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOG.info("Start processing Œ badges 2");
        try {
            ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
            EntityManagerService entityManagerService = CommonsUtils.getService(EntityManagerService.class);
            entityManagerService.startRequest(currentContainer);
            // STEP1: Find all dbadges from EarnedBadges table to be sent
            ListAccess<Identity> userListAccess = CommonsUtils.getService(IdentityManager.class).getIdentitiesByProfileFilter(OrganizationIdentityProvider.NAME,
                    new ProfileFilter(),
                    false);
            this.initialNbUsers = userListAccess.getSize();

            LOG.debug("Total users to process : {}", this.initialNbUsers);
            int offset = 0;
            int size = 100;
            boolean hasNextUser = true;
            int countUser = 1;
            int lastUserLog = 0;

            while (userLimit != 0 && hasNextUser) {
                // Extract all users by limit
                Identity[] identities = userListAccess.load(offset, size);
                if (identities.length == 0) {
                    break;
                } else if (identities.length < size) {
                    hasNextUser = false;
                }

                LOG.debug("Identities array size : {}", identities.length);

                // Extract all activities by user ID
                for (Identity identity : identities) {
                    if (runBuild) {
                        entityManagerService.endRequest(currentContainer);
                        entityManagerService.startRequest(currentContainer);
                        // STEP 2 : Loop on badges and send them to

                        // Stream Notification

                        // Onsite/Mail/Push Notiifcation process

                        countUser++;
                        LOG.debug("Users treaten : {}/{}", countUser, this.initialNbUsers);
                        if (userLimit != -1 && countUser > userLimit)
                            break;
                    } else {
                        LOG.info("processing was stopped explicitly");
                        return;
                    }
                }
                offset += size;
                if (userLimit != -1 && countUser > userLimit)
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            LOG.info("Processing earned badge finished");
        }
    }
}
