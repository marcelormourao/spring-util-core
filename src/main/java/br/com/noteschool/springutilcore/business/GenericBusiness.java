package br.com.noteschool.springutilcore.business;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import br.com.noteschool.springutilcore.repository.GenericRepository;
import br.com.noteschool.springutilcore.util.BasicCrudOperations;
import br.com.noteschool.springutilcore.util.ReflectionUtil;

/**
 * @author marcelo.mourao
 * @since 18/01/2017
 */
public class GenericBusiness<T, ID extends Serializable> implements BasicCrudOperations<T, ID> 
{
	
	private GenericRepository<T, ID> repository;
	
	@SuppressWarnings("unused")
	private GenericBusiness(){}

	public GenericBusiness(GenericRepository<T,ID> repository) {
		this.repository = repository;
	}
	
	@Override
	public List<T> findAll() {
		return repository.findAll();
	}

	@Override
	public List<T> findAll(Sort sort) {
		return repository.findAll(sort);
	}

	@Override
	public List<T> findAll(Iterable<ID> ids) {
		return repository.findAll(ids);
	}

	@Override
	public <S extends T> List<S> save(Iterable<S> entities) {
		return repository.save(entities);
	}

	@Override
	public void flush() {
		repository.flush();
	}
	
	@Override
	public <S extends T> S saveAndFlush(S entity) {
		return repository.saveAndFlush(entity);
	}

	@Override
	public void deleteInBatch(Iterable<T> entities) {
		repository.deleteInBatch(entities);
	}

	@Override
	public void deleteAllInBatch() {
		repository.deleteAllInBatch();
	}

	@Override
	public T getOne(ID id) {
		return repository.getOne(id);
	}

	@Override
	public <S extends T> List<S> findAll(Example<S> example) {
		return repository.findAll(example);
	}

	@Override
	public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
		return repository.findAll(example, sort);
	}

	@Override
	public Page<T> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public <S extends T> S save(S entity) {
		return repository.save(entity);
	}

	@Override
	public T findOne(ID id) {
		return repository.findOne(id);
	}

	@Override
	public boolean exists(ID id) {
		return repository.exists(id);
	}

	@Override
	public long count() {
		return repository.count();
	}

	@Override
	public void delete(ID id) {
		repository.delete(id);
	}

	@Override
	public void delete(T entity) {
		repository.delete(entity);
	}

	@Override
	public void delete(Iterable<? extends T> entities) {
		repository.delete(entities);
	}

	@Override
	public void deleteAll() {
		repository.deleteAll();
	}

	@Override
	public <S extends T> S findOne(Example<S> example) {
		return repository.findOne(example);
	}

	@Override
	public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
		return repository.findAll(example, pageable);
	}

	@Override
	public <S extends T> long count(Example<S> example) {
		return repository.count(example);
	}

	@Override
	public <S extends T> boolean exists(Example<S> example) {
		return repository.exists(example);
	}
	
	@Override
	public void updateHibernate(T object) {
		repository.updateHibernate(object);
	}
	
	@Override
	public void persistOrUpdateHibernate(T object) {
		this.repository.persistOrUpdateHibernate(object);
	}

	@Override
	public void updateNotNullFields(T object) {
		repository.updateNotNullFields(object);
	}

	@Override
	public void update(T entity) {
		repository.update(entity);
	}

	@Override
	public void update(T entity, boolean atualizaCamposNulos) {
		repository.update(entity, atualizaCamposNulos);
	}
	
	public void update(T entity, String... fields){
		repository.update(entity, fields);
	}

	@Override
	public T persistOrUpdate(T object) {
		return repository.persistOrUpdate(object);
	}
	
	public T persistOrUpdateAndFlush(T object) {
		T entity = repository.persistOrUpdate(object);
		flush();
		return entity;
	}

	@Override
	public T persistOrUpdate(T object, boolean atualizaCamposNulos) {
		return repository.persistOrUpdate(object, atualizaCamposNulos);
	}
	
	@Override
	public List<T> persistOrUpdate(Iterable<T> entities) {
		return repository.persistOrUpdate(entities);
	}
	
	@Override
	public List<T> persistOrUpdate(Iterable<T> entities, boolean atualizaCamposNulos) {
		return repository.persistOrUpdate(entities, atualizaCamposNulos);
	}

	@Override
	public int deleteById(ID id) {
		return repository.deleteById(id);
	}

	@Override
	public void persist(T entity) {
		this.repository.persist(entity);
	}
	
	@SuppressWarnings("unchecked")
	public Class<T> getClassType(){
		return (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	public GenericBusiness<T, ID> limparTransientes(Object entity){
		if(entity == null){
			return this;
		}
		try{
			for(Field field : entity.getClass().getDeclaredFields()){
				if(ReflectionUtil.isEntity(field)){
					
					Object object = ReflectionUtil.getValueByField(field, entity);
					
					if(object != null){
						Field idField = ReflectionUtil.getIdField(object);
						
						if(ReflectionUtil.getValueByField(idField, object) == null){
							field.setAccessible(true);
							
							field.set(entity, null);
						}
					}
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return this;
	}
}
