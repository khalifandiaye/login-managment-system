package de.uplinkgmbh.lms.business;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import de.uplinkgmbh.lms.presistence.MyPersistenceManager;


/**
 * @author alex
 *
 */
public class DBList {
	
	private MyPersistenceManager pm = MyPersistenceManager.getInstance();
	private EntityManager em = pm.getEntityManager();
	private List<?> resultList = null;
	public Query query = null;
	private Query maxCountQuery = null;
	private long maxCount = 0;

	@SuppressWarnings("unchecked")
	public void execute(){
		
		em.getTransaction().begin();
		resultList = query.getResultList();

		if( maxCountQuery != null ){	
			maxCount = (Long) maxCountQuery.getSingleResult();
		}
		em.getTransaction().commit();
	}

	public Query getQuery() {
		return query;
	}
	
	public Query getMaxCountQuery() {
		return maxCountQuery;
	}

	/**
	 * Set a query without the maxCount Query. getMaxCount returns ever 0.
	 * 
	 * This parameters are for the implementation's use.
	 * 
	 * @param sql string
	 */
	public void setQuery( String sql ) {
		setQuery( sql, null );
	}
	
	/**
	 * Set a query and the query for maxCount.
	 * Example: setQuery( "SELECT a FROM Application a", "SELECT count(a) FROM Application a" )
	 * 
	 * This parameters are for the implementation's use.
	 * 
	 * @param sql string
	 * @param countSql string
	 */
	public void setQuery( String sql, String countSql ) {
		this.query = em.createQuery( sql );
		if( countSql != null ) this.maxCountQuery = em.createQuery( countSql );
		else this.maxCountQuery = null;
	}
	
	/**
	 * Set a NamedQuery and the NamedQuery for maxCount.
	 * Example: setNamedQuery( "ApplicationByName", "ApplicationByNameCount" )
	 * 
	 * This parameters are for the implementation's use.
	 * 
	 * @param namedQuery string
	 * @param namedCountquery string
	 */
	public void setNamedQuery( String namedQuery, String namedQueryCount ) {
		this.query = em.createNamedQuery( namedQuery );
		if( namedQueryCount != null ) this.maxCountQuery = em.createNamedQuery( namedQueryCount );
		else this.maxCountQuery = null;
	}
	
	/**
	 * Set a NamedQuery without NamedQuery for maxCount. getMaxCount returns ever 0.
	 * Example: setNamedQuery( "ApplicationByName" )
	 * 
	 * This parameters are for the implementation's use.
	 * 
	 * @param namedQuery string
	 */
	public void setNamedQuery( String namedQuery ) {
		setNamedQuery( namedQuery, null );
	}

	public List<?> getResultList() {
		return resultList;
	}

	public long getMaxCount() {
		return maxCount;
	}

	@SuppressWarnings("unused")
	private void setResultList(List<Object> resultList) {
		this.resultList = resultList;
	}

	@SuppressWarnings("unused")
	private void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

}
