package org.exoplatform.addons.gamification.storage.dao;

import org.exoplatform.addons.gamification.entities.domain.configuration.RuleEntity;
import org.exoplatform.commons.api.persistence.GenericDAO;
import org.exoplatform.commons.persistence.impl.GenericDAOJPAImpl;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import java.util.List;

public class RuleDAO extends GenericDAOJPAImpl<RuleEntity, Long> implements GenericDAO<RuleEntity, Long> {

    public RuleDAO() {
    }

    public RuleEntity findEnableRuleByTitle(String ruleTitle) throws PersistenceException {

        TypedQuery<RuleEntity> query = getEntityManager().createNamedQuery("Rule.findEnabledRuleByTitle", RuleEntity.class)
                .setParameter("ruleTitle", ruleTitle);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    public RuleEntity findRuleByTitle(String ruleTitle) throws PersistenceException {

        TypedQuery<RuleEntity> query = getEntityManager().createNamedQuery("Rule.findRuleByTitle", RuleEntity.class)
                .setParameter("ruleTitle", ruleTitle);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    public List<RuleEntity> getAllRules() throws PersistenceException {

        TypedQuery<RuleEntity> query = getEntityManager().createNamedQuery("Rule.getAllRules", RuleEntity.class);

        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }

    }

    public List<RuleEntity> getAllRulesByDomain(String domain) throws PersistenceException {

        TypedQuery<RuleEntity> query = getEntityManager().createNamedQuery("Rule.getAllRulesByDomain", RuleEntity.class)
                .setParameter("domain", domain);

        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }

    }

    public List<RuleEntity> getAllRulesWithNullDomain() throws PersistenceException {

        TypedQuery<RuleEntity> query = getEntityManager().createNamedQuery("Rule.getAllRulesWithNullDomain", RuleEntity.class);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }

    }

    public int deleteRuleById(int ruleId) throws PersistenceException {
        return getEntityManager().createNamedQuery("Rule.deleteRuleById")
                .setParameter("ruleId", ruleId)
                .executeUpdate();

    }


    public int deleteRuleByTitle(String ruleTitle) throws PersistenceException {
        return getEntityManager().createNamedQuery("Rule.deleteRuleByTitle")
                .setParameter("ruleTitle", ruleTitle)
                .executeUpdate();

    }


    public List<String>  getDomainList() throws PersistenceException {
        TypedQuery<String> query = getEntityManager().createNamedQuery("Rule.getDomainList", String.class);

        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }

    }

    public void clear() {
        getEntityManager().clear();
    }
}
