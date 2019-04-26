package org.exoplatform.addons.gamification.notification.builder;

import org.exoplatform.commons.api.notification.NotificationContext;
import org.exoplatform.commons.api.notification.channel.template.AbstractTemplateBuilder;
import org.exoplatform.commons.api.notification.channel.template.TemplateProvider;
import org.exoplatform.commons.api.notification.model.MessageInfo;

import java.io.Writer;

public class GamificationTemplateBuilder extends AbstractTemplateBuilder {

    private TemplateProvider templateProvider;

    private boolean          pushNotification;

    public GamificationTemplateBuilder(TemplateProvider templateProvider, boolean pushNotification) {
        this.templateProvider = templateProvider;
        this.pushNotification = pushNotification;
    }
    @Override
    protected MessageInfo makeMessage(NotificationContext notificationContext) {
        return null;
    }

    @Override
    protected boolean makeDigest(NotificationContext notificationContext, Writer writer) {
        return false;
    }
}
