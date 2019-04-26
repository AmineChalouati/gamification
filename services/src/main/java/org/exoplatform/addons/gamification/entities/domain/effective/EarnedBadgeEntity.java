package org.exoplatform.addons.gamification.entities.domain.effective;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.exoplatform.commons.api.persistence.ExoEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@ExoEntity
@Table(name = "GAMIFICATION_EARNED_BADGE")
public class EarnedBadgeEntity implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_GAMIFICATION_EARNED_BADGE_ID", sequenceName="SEQ_GAMIFICATION_EARNED_BADGE_ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY, generator="SEQ_GAMIFICATION_EARNED_BADGE_ID")
    @Column(name = "ID")
    protected Long id;
    @Column(name = "TITLE", unique = false, nullable = false)
    protected String title;
    @Column(name = "DESCRIPTION")
    protected String description;
    @Column(name = "USER_SOCIAL_ID", nullable = false)
    protected String userSocialId;
    @Temporal(TemporalType.DATE)
    @Column(name = "EARNED_DATE")
    private Date earnedDate;
    @Column(name = "SCORE", nullable = false)
    private long score;
    @Column(name = "DOMAIN", nullable = false)
    protected String domain;
    @Column(name = "SENT", nullable = false)
    protected boolean sent;

    public EarnedBadgeEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserSocialId() {
        return userSocialId;
    }

    public void setUserSocialId(String userSocialId) {
        this.userSocialId = userSocialId;
    }

    public Date getEarnedDate() {
        return earnedDate;
    }

    public void setEarnedDate(Date earnedDate) {
        this.earnedDate = earnedDate;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EarnedBadgeEntity)) {
            return false;
        }
        EarnedBadgeEntity that = (EarnedBadgeEntity) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(title, that.title);
        eb.append(description, that.description);
        eb.append(userSocialId, that.userSocialId);
        eb.append(earnedDate, that.earnedDate);
        eb.append(score, that.score);
        eb.append(domain, that.domain);
        eb.append(sent, that.sent);
        return eb.isEquals();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(title);
        hcb.append(description);
        hcb.append(userSocialId);
        hcb.append(earnedDate);
        hcb.append(score);
        hcb.append(domain);
        hcb.append(sent);
        return hcb.toHashCode();
    }
    @Override
    public String toString() {
        return "EarnedBadgeEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", userSocialId='" + userSocialId + '\'' +
                ", earnedDate=" + earnedDate +
                ", score=" + score +
                ", domain='" + domain + '\'' +
                ", sent='" + sent + '\'' +
                '}';
    }
}
