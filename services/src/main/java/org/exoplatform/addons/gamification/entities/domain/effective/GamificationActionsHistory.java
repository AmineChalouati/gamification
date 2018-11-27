package org.exoplatform.addons.gamification.entities.domain.effective;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.exoplatform.addons.gamification.entities.domain.configuration.AbstractAuditingEntity;
import org.exoplatform.commons.api.persistence.ExoEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@ExoEntity
@Entity(name = "GamificationActionsHistory")
@Table(name = "GAMIFICATION_ACTIONS_HISTORY")
@NamedQueries({
        @NamedQuery(
                name = "GamificationActionsHistory.findAllActionsHistory",
                query = "SELECT new org.exoplatform.addons.gamification.service.effective.StandardLeaderboard(g.userSocialId, SUM(g.actionScore) as total) FROM GamificationActionsHistory g GROUP BY g.userSocialId ORDER BY total DESC"
        ),
        @NamedQuery(
                name = "GamificationActionsHistory.findAllActionsHistoryByDateByDomain",
                query = "SELECT new org.exoplatform.addons.gamification.service.effective.StandardLeaderboard(g.userSocialId, SUM(g.actionScore) as total) FROM GamificationActionsHistory g  WHERE g.date >= :date AND g.domain = :domain GROUP BY g.userSocialId ORDER BY total DESC"
        ),
        @NamedQuery(
                name = "GamificationActionsHistory.findActionsHistoryByUserId",
                query = "SELECT a FROM GamificationActionsHistory a where a.userSocialId = :socialUserId ORDER BY a.globalScore DESC"
        ),
        @NamedQuery(
                name = "GamificationActionsHistory.findAllActionsHistoryByDomain",
                query = "SELECT new org.exoplatform.addons.gamification.service.effective.StandardLeaderboard(g.userSocialId, SUM(g.actionScore) as total) FROM GamificationActionsHistory g WHERE g.domain = :domain GROUP BY g.userSocialId ORDER BY total DESC"
        ),
        @NamedQuery(
                name = "GamificationActionsHistory.findActionHistoryByDateBySocialId",
                query = "SELECT a FROM GamificationActionsHistory a where a.date = :date and a.userSocialId = :socialId ORDER BY a.globalScore DESC"
        ),
        @NamedQuery(
                name = "GamificationActionsHistory.findActionsHistoryByDate",
                query = "SELECT new org.exoplatform.addons.gamification.service.effective.StandardLeaderboard(g.userSocialId, SUM(g.actionScore) as total) FROM GamificationActionsHistory g WHERE g.date >= :date GROUP BY g.userSocialId ORDER BY total DESC"
        ),
        @NamedQuery(
                name = "GamificationActionsHistory.findActionsHistoryByDateByDomain",
                query = "SELECT new org.exoplatform.addons.gamification.service.effective.StandardLeaderboard(g.userSocialId, SUM(g.actionScore) as total) FROM GamificationActionsHistory g WHERE g.date >= :date and g.domain = :domain GROUP BY g.userSocialId ORDER BY total DESC"
        ),
        @NamedQuery(
                name = "GamificationActionsHistory.findStatsByUser",
                query = "SELECT new org.exoplatform.addons.gamification.service.effective.PiechartLeaderboard(g.domain,SUM(g.actionScore)) FROM GamificationActionsHistory g WHERE g.userSocialId = :userSocialId GROUP BY g.domain"
        ),
        @NamedQuery(
                name = "GamificationActionsHistory.findDomainScoreByUserId",
                query = "SELECT new org.exoplatform.addons.gamification.service.effective.ProfileReputation(g.domain,SUM(g.actionScore)) FROM GamificationActionsHistory g WHERE g.userSocialId = :userSocialId GROUP BY g.domain"
        ),
        @NamedQuery(
                name = "GamificationActionsHistory.findUserReputationScoreBetweenDate",
                query = "SELECT SUM(g.actionScore) as total FROM GamificationActionsHistory g WHERE g.userSocialId = :userSocialId AND g.date BETWEEN :fromDate AND :toDate"
        ),
        @NamedQuery(
                name = "GamificationActionsHistory.findUserReputationScoreByMonth",
                query = "SELECT SUM(g.actionScore) as total FROM GamificationActionsHistory g WHERE g.userSocialId = :userSocialId AND g.date >= :currentMonth"
        ),
        @NamedQuery(
                name = "GamificationActionsHistory.findUserReputationScoreByDomainBetweenDate",
                query = "SELECT SUM(g.actionScore) as total FROM GamificationActionsHistory g WHERE g.userSocialId = :userSocialId AND g.domain = :domain AND g.date BETWEEN :fromDate AND :toDate"
        )

})
public class GamificationActionsHistory extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name="SEQ_GAMIFICATION_ACTIONS_HISTORY_ID", sequenceName="SEQ_GAMIFICATION_ACTIONS_HISTORY_ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY, generator="SEQ_GAMIFICATION_ACTIONS_HISTORY_ID")
    @Column(name = "ID")
    protected Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "ACTION_DATE")
    private Date date;

    @Column(name = "USER_SOCIAL_ID", nullable = false)
    private String userSocialId;

    @Column(name = "GLOBAL_SCORE", nullable = false)
    protected long globalScore;

    @Column(name = "ACTION_TITLE", nullable = false)
    private String actionTitle;

    @Column(name = "DOMAIN", nullable = false)
    private String domain;

    @Column(name = "CONTEXT", nullable = true)
    private String context;

    @Column(name = "ACTION_SCORE", nullable = false)
    private long actionScore;

    public GamificationActionsHistory() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUserSocialId() {
        return userSocialId;
    }

    public void setUserSocialId(String userSocialId) {
        this.userSocialId = userSocialId;
    }

    public long getGlobalScore() {
        return globalScore;
    }

    public void setGlobalScore(long globalScore) {
        this.globalScore = globalScore;
    }

    public String getActionTitle() {
        return actionTitle;
    }

    public void setActionTitle(String actionTitle) {
        this.actionTitle = actionTitle;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public long getActionScore() {
        return actionScore;
    }

    public void setActionScore(long actionScore) {
        this.actionScore = actionScore;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GamificationActionsHistory)) {
            return false;
        }
        GamificationActionsHistory that = (GamificationActionsHistory) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(date, that.date);
        eb.append(userSocialId, that.userSocialId);
        eb.append(globalScore, that.globalScore);
        eb.append(actionTitle, that.actionTitle);
        eb.append(domain, that.domain);
        eb.append(context, that.context);
        eb.append(actionScore, that.actionScore);
        return eb.isEquals();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(date);
        hcb.append(userSocialId);
        hcb.append(globalScore);
        hcb.append(actionTitle);
        hcb.append(domain);
        hcb.append(actionScore);
        return hcb.toHashCode();
    }

    @Override
    public String toString() {
        return "Gamification History{" +
                "date=" + date +
                ", social_id=" + userSocialId +
                ", global_score=" + globalScore +
                ", action_title=" + actionTitle +
                ", domain=" + domain +
                ", context=" + context +
                ", action_score=" + actionScore +
                '}';
    }
}
