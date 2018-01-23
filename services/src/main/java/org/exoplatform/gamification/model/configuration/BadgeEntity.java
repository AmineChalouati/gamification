package org.exoplatform.gamification.model.configuration;

import org.exoplatform.commons.api.persistence.ExoEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "Badge")
@ExoEntity
@Table(name = "GAMIFICATION_BADGE")
@NamedQueries( {
        @NamedQuery(
                name = "Badge.getAllBadges",
                query = "SELECT badge FROM Badge badge"
        ),
        @NamedQuery(
                name = "Badge.getEnabledBadges",
                query = "SELECT badge FROM Badge badge where badge.isEnabled = :isEnabled "
        ),
        @NamedQuery(
                name = "Badge.getValidBadges",
                query = "SELECT badge FROM Badge badge where (badge.startValidityDate BETWEEN :stDate AND :edDate) AND (badge.endValidityDate BETWEEN :stDate AND :edDate) "
        ),
        @NamedQuery(
                name = "Badge.findBadgeByNeededScore",
                query = "SELECT badge FROM Badge badge where badge.neededScore = :neededScore"
        ),
        @NamedQuery(
                name = "Badge.findBadgeByTitle",
                query = "SELECT badge FROM Badge badge where badge.title = :badgeTitle"
        ),
        @NamedQuery(
                name = "Badge.deleteBadgeByTitle",
                query = "DELETE FROM Badge badge WHERE badge.title = :badgeTitle "
        ),
        @NamedQuery(
                name = "Badge.deleteBadgeById",
                query = "DELETE FROM Badge badge WHERE badge.id = :badgeId "
        )
})
public class BadgeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(name = "TITLE", unique = true,   nullable = false)
    protected String title;

    /**
    @Column(name = "MODULE", unique = true,   nullable = false)
    protected Module  module /**(module) List [Foreigh_Key to table CategoryType]
    */
    @Column(name = "DESCRIPTION")
    protected String description;

    @Column(name = "NEEDED_SCORE")
    protected int neededScore;

    @Column(name = "ICON")
    protected String icon;

    @Column(name = "VALIDITY_DATE_START")
    @Temporal(TemporalType.DATE)
    protected Date startValidityDate;

    @Column(name = "VALIDITY_DATE_END")
    @Temporal(TemporalType.DATE)
    protected Date endValidityDate;

    @Column(name = "BADGE_IS_ENABLE",nullable = false)
    protected boolean isEnabled;

    @Column(name = "CREATED_DATE")
    protected Date createdDate;


    public BadgeEntity() {
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

    public int getNeededScore() {
        return neededScore;
    }

    public void setNeededScore(int neededScore) {
        this.neededScore = neededScore;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Date getStartValidityDate() {
        return startValidityDate;
    }

    public void setStartValidityDate(Date startValidityDate) {
        this.startValidityDate = startValidityDate;
    }

    public Date getEndValidityDate() {
        return endValidityDate;
    }

    public void setEndValidityDate(Date endValidityDate) {
        this.endValidityDate = endValidityDate;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
