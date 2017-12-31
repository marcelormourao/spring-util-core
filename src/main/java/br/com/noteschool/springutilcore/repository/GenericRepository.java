package br.com.noteschool.springutilcore.repository;

import java.io.Serializable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author marcelo.mourao
 * @since 18/01/2017
 */
@NoRepositoryBean
public interface GenericRepository<T, ID extends Serializable> extends JpaRepository<T, ID>{
	
	public void updateHibernate(T object);
	
	void persistOrUpdateHibernate(T object);
	
	public void updateNotNullFields(T object);
	
	/**
	 * Atualiza todos os campos inclusive os nulos
	 */
	public void update(T entity);
	
	public void update(T entity, boolean atualizaCamposNulos);
	
	public void update(T entity, String... fields);
	
	public T persistOrUpdate(T object);
	
	public T persistOrUpdate(T object, boolean atualizaCamposNulos);
	
	public List<T> persistOrUpdate(Iterable<T> entities);
	
	public List<T> persistOrUpdate(Iterable<T> entities, boolean atualizaCamposNulos) ;
	
	public int deleteById(ID id);

	void persist(T entity);
	
}
