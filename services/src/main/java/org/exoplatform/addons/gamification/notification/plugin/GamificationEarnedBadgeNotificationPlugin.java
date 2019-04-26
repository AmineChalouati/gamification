package org.exoplatform.addons.gamification.notification.plugin;

import org.exoplatform.addons.gamification.GamificationConstant;
import org.exoplatform.commons.api.notification.NotificationContext;
import org.exoplatform.commons.api.notification.model.NotificationInfo;
import org.exoplatform.commons.api.notification.plugin.BaseNotificationPlugin;
import org.exoplatform.container.xml.InitParams;

public class GamificationEarnedBadgeNotificationPlugin extends BaseNotificationPlugin {

    public GamificationEarnedBadgeNotificationPlugin(InitParams initParams) {
        super(initParams);
    }

    @Override
    public String getId() {
        return GamificationConstant.GAMIFICATION_EARNED_BADGE_NOTIFICATION_ID;
    }

    @Override
    public boolean isValid(NotificationContext notificationContext) {
        /** Make some rules here*/
        return true;
    }

    @Override
    protected NotificationInfo makeNotification(NotificationContext notificationContext) {
        return NotificationInfo.instance()
                .key(getId())
                .end();
    }
}
