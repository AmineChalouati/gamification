package org.exoplatform.addons.gamification.job;

import org.exoplatform.addons.gamification.entities.domain.configuration.BadgeEntity;
import org.exoplatform.addons.gamification.entities.domain.effective.EarnedBadgeEntity;
import org.exoplatform.addons.gamification.service.effective.GamificationService;
import org.exoplatform.addons.gamification.service.effective.ProfileReputation;
import org.exoplatform.commons.persistence.impl.EntityManagerService;
import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.management.annotations.Managed;
import org.exoplatform.management.annotations.ManagedDescription;
import org.exoplatform.management.jmx.annotations.NameTemplate;
import org.exoplatform.management.jmx.annotations.Property;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.profile.ProfileFilter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Managed
@NameTemplate(@Property(key = "service", value = "ComputeEarnedBadgesJob"))
@ManagedDescription("Gamification Compute Earned Badges jobs")
public class ComputeEarnedBadgesJob implements Job {
    private static final Log LOG = ExoLogger.getLogger(ComputeEarnedBadgesJob.class.getName());
    private int initialNbUsers;
    private int userLimit = 100;
    private boolean   runBuild = true;

    public ComputeEarnedBadgesJob() throws Exception {
    }

    public boolean isRunBuild() {
        return runBuild;
    }

    @Managed
    @ManagedDescription("Stop badges computation")
    public void setRunBuild(boolean runBuild) {
        this.runBuild = runBuild;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOG.info("Start processing Œ badges 2");
        try {
            ExoContainer currentContainer = ExoContainerContext.getCurrentContainer();
            EntityManagerService entityManagerService = CommonsUtils.getService(EntityManagerService.class);
            entityManagerService.startRequest(currentContainer);
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
                        double userPercent = ((double) countUser / userListAccess.getSize()) * 100;
                        if ((int) userPercent % 5 == 0 && lastUserLog != (int) userPercent) {
                            LOG.info("Processing users {}%", (int) userPercent);
                            lastUserLog = (int) userPercent;
                        }
                        // STEP1 : check for each user badges he can earn
                        List<ProfileReputation> badgesByDomain= CommonsUtils.getService(GamificationService.class).buildDomainScoreByUserId(identity.getId());
                        // Get Available Badges
                        for (ProfileReputation p : badgesByDomain) {

                            List<BadgeEntity> myBadges = CommonsUtils.getService(GamificationService.class).findEarnedBadgesByDomainUserIdByScore(p.getDomain(),(int)p.getScore());

                            // STEP 2 : Cehck if the current badges is already computed

                            for (BadgeEntity b : myBadges) {

                                EarnedBadgeEntity earnedBadgeEntity = new EarnedBadgeEntity();
                                earnedBadgeEntity.setUserSocialId(identity.getId());
                                earnedBadgeEntity.setScore(b.getNeededScore());
                                earnedBadgeEntity.setTitle(b.getTitle());
                                earnedBadgeEntity.setDescription(b.getDescription());
                                earnedBadgeEntity.setEarnedDate(new Date());
                                earnedBadgeEntity.setSent(true);
                                CommonsUtils.getService(GamificationService.class).saveEarnedBadge(earnedBadgeEntity);
                            }

                        }
                        // STEP2 : based on badges a user can earn check which one already registered in earnedBadeEntity
                        // STEP3 : save an entry on table earnedBadeEntity
                        //gamificationService.saveEarnedBadge();

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
