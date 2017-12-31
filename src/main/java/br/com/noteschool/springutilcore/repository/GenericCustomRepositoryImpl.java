package br.com.noteschool.springutilcore.repository;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;

/**
 * @author marcelo.mourao
 * @since 19/01/2017
 */
public abstract class GenericCustomRepositoryImpl<T, ID extends Serializable> {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	public Session getSession() {
		return this.entityManager.unwrap(Session.class);
	}
	
    public void alterarParametrosDeComparacaoEOrdenacaoDaSessao(){
	    	getSession().createSQLQuery("alter session set nls_comp=linguistic").executeUpdate();
	    	getSession().createSQLQuery("alter session set nls_sort=binary_ai").executeUpdate();
	}
	
	public void retornarParametrosDeComparacaoEOrdenacaoDaSessao(){
		getSession().createSQLQuery("alter session set nls_comp=BINARY").executeUpdate();
		getSession().createSQLQuery("alter session set nls_sort=WEST_EUROPEAN").executeUpdate();
	}
}
