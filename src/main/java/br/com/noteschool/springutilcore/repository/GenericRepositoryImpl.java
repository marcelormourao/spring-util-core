package br.com.noteschool.springutilcore.repository;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import br.com.noteschool.springutilcore.util.ReflectionUtil;

/**
 * @author marcelo.mourao
 * @since 18/01/2017
 */
public class GenericRepositoryImpl <T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements GenericRepository<T, ID> {

	private EntityManager entityManager;
	
	public GenericRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
 		super(domainClass, entityManager);
		this.entityManager = entityManager;
	}
	
	public GenericRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
	}
	
	private Session getSession(){
		return this.entityManager.unwrap(Session.class);
	}
	
	@Override
	public void updateHibernate(T object) {
		getSession().update(object);
	}
	
	@Override
	@Transactional
	public void persistOrUpdateHibernate(T object){
		Field idField = ReflectionUtil.getIdField(object);
		
		Assert.notNull(idField, "Não foi possível encontrar campo anotado com @Id.");
		
		Object id = ReflectionUtil.getValueByField(idField, object);
		
		if(id == null){
			this.entityManager.persist(object);
		}else{
			updateHibernate(object);
		}
	}
	
	
	@Override
	@Transactional
	public void persist(T entity){
		this.entityManager.persist(entity);
	}
	
	@Transactional
	@Override
	public T persistOrUpdate(T object){
		return persistOrUpdate(object, true);
	}
	
	@Transactional
	@Override
	public T persistOrUpdate(T object, boolean atualizaCamposNulos){
		Field idField = ReflectionUtil.getIdField(object);
		
		Assert.notNull(idField, "Não foi possível encontrar campo anotado com @Id.");
		
		Object id = ReflectionUtil.getValueByField(idField, object);
		
		if(id == null){
			this.entityManager.persist(object);
		}else{
			update(object, atualizaCamposNulos);
		}
		
		return object;
	}
	
	@Transactional
	@Override
	public List<T> persistOrUpdate(Iterable<T> entities) {
		return persistOrUpdate(entities, true);
	}
	
	@Transactional
	@Override
	public List<T> persistOrUpdate(Iterable<T> entities, boolean atualizaCamposNulos) {

		List<T> result = new ArrayList<T>();

		if (entities == null) {
			return result;
		}

		for (T entity : entities) {
			result.add(persistOrUpdate(entity, atualizaCamposNulos));
		}

		return result;
	}
	
	@Transactional
	@Override
	public int deleteById(ID id){
		Field idField = ReflectionUtil.getIdField(super.getDomainClass());
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		
		CriteriaDelete<T> criteria = (CriteriaDelete<T>) builder.createCriteriaDelete(super.getDomainClass());
		
		Root<T> root = criteria.from(super.getDomainClass());
		
		criteria.where(builder.equal(root.get(idField.getName()), id));
		
		Query query = entityManager.createQuery(criteria);
		
		return query.executeUpdate();
	}
	
	
	/**
	 * @author marcelo.mourao
	 * @since 20/01/2017
	 * Atualiza apenas os campos que não são nulos...
	 */
	@Transactional
	public void updateNotNullFields(T entity) {
		update(entity, false);
	}
	
	/**
	 * Atualiza todos os campos inclusive os nulos
	 */
	@Transactional
	public void update(T entity) {
		update(entity, true);
	}
	
	@Transactional
	public void update(T entity, boolean atualizaCamposNulos){
		List<Field> camposAtualizar = obterCamposAtualizaveis(entity, atualizaCamposNulos);
		
		update(entity, camposAtualizar);
	}
	
	/**
	 * @author marcelo.mourao
	 * @since  30/11/2017
	 */
	@Transactional
	public void update(T entity, String... fields){
		
		List<Field> camposAtualizar = new ArrayList<Field>();
		
		for(String field : fields) {
			try {
				camposAtualizar.add(entity.getClass().getDeclaredField(field));
			} catch (NoSuchFieldException | SecurityException e) {
				throw new RuntimeException("Invalid field: " + e.getMessage());
			}
		}
		
		update(entity, camposAtualizar);
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	public void update(T entity, List<Field> camposAtualizar){
		Field idField = ReflectionUtil.getIdField(entity);
		
		Assert.notNull(idField, "Não foi possível encontrar campo anotado com @Id.");
		
		Object idValue = ReflectionUtil.getValueByField(idField, entity);
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		
		CriteriaUpdate<T> criteria = (CriteriaUpdate<T>) builder.createCriteriaUpdate(entity.getClass());
		
		Root<T> root = criteria.from((Class<T>) entity.getClass());
		
		camposAtualizar.forEach(f -> {
			if(ReflectionUtil.isEntity(f) && ReflectionUtil.isFieldNull(f, entity)){
				criteria.set(root.<Long> get(f.getName()), builder.nullLiteral(Long.class));
			}else{
				criteria.set(f.getName(), ReflectionUtil.getValueByField(f, entity));
			}
		});
		
		criteria.where(builder.equal(root.get(idField.getName()), idValue));
		
		Query query = entityManager.createQuery(criteria);
		
		query.executeUpdate();
	}

	/**
	 * @param entity - objeto a ser atualizado
	 * @param atualizaCamposNulos - atualizar campos nulos
	 * @return Campos que serão atualizados
	 */
	private List<Field> obterCamposAtualizaveis(T entity, boolean atualizaCamposNulos) {
		List<Field> fields = Arrays.asList(entity.getClass().getDeclaredFields());
		
		return fields.parallelStream().filter(field -> {
				return  !ReflectionUtil.isIgnoreOnGenericUpdate(field) &&
						!ReflectionUtil.isSerialVersionField(field) &&
					    !ReflectionUtil.isIdField(field) &&
					   	(atualizaCamposNulos || ReflectionUtil.isFieldNotNull(field, entity)) &&
					   	ReflectionUtil.isUpdatable(field) &&
					    !ReflectionUtil.isSubclassItarable(field) &&
					    !ReflectionUtil.isTransient(field) &&
					    !ReflectionUtil.isMappedByField(field);
		}).collect(Collectors.toList());
	}
	
	
}
