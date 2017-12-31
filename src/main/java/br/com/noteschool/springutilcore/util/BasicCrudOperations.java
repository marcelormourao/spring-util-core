package br.com.noteschool.springutilcore.util;

import java.io.Serializable;

import br.com.noteschool.springutilcore.repository.GenericRepository;

/**
 * @author marcelo.mourao
 * @since 19/01/2017
 */
public interface BasicCrudOperations<T, ID extends Serializable> extends GenericRepository<T, ID>{

}
