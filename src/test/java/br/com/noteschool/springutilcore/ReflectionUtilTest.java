package br.com.noteschool.springutilcore;

import static br.com.noteschool.springutilcore.util.ReflectionUtil.getIdField;
import static br.com.noteschool.springutilcore.util.ReflectionUtil.getValueByField;
import static br.com.noteschool.springutilcore.util.ReflectionUtil.isEntityIdNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.junit.Test;

import br.com.noteschool.springutilcore.util.ReflectionUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * @author marcelo.mourao
 * @since  05/10/2017
 */
public class ReflectionUtilTest {
	
	@Test
	public void testaSeIdDaEntidadeEhNull(){
		
		Funcionario funcionario = new Funcionario();
		
		assertTrue(isEntityIdNull(funcionario));
	}
	
	@Test
	public void testaSeConseguePegarFieldDoId(){
		
		Funcionario funcionario = new Funcionario();
		
		Field idField = getIdField(funcionario);
		
		assertEquals("idFuncionario", idField.getName());
	}
	
	@Test
	public void testaSeConseguePegarValorDoId(){
		
		Pai pai = new Pai();
		pai.setId(123L);
		
		Field idField = getIdField(pai);
		
		Object result = getValueByField(idField, pai);
		
		assertEquals(123L, result);
	}

	@Test
	public void testaSeConseguePegarValorDoIdSeEstiverNaClassePai(){
		
		Filho filho = new Filho();
		filho.setId(666L);
		
		Field idField = getIdField(filho);
		
		Object result = getValueByField(idField, filho);
		
		assertEquals(666L, result);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testaSeClasseSemIdLancaIllegalArgumentException(){
		
		ClasseSemId classe = new ClasseSemId();
		
		isEntityIdNull(classe);
	}
	
	@Test
	public void testaExistenciaTodosOsTiposDeMappedBy(){
		
		ClassComTodosCamposMappedBy clazz = new ClassComTodosCamposMappedBy();
		
		for(Field field : clazz.getClass().getDeclaredFields()){
			if(ReflectionUtil.isMappedByField(field) == false){
				throw new RuntimeException("Encontrado um campo que não é mappedBy!");
			}
		}
	}
}

@Entity
@Getter @Setter
class Pai {
	@Id
	private Long id;
}

@Entity
@Getter @Setter
class Filho extends Pai{
	private String nome;
}

@Entity
@Getter @Setter
class Funcionario {
	@Id
	private Long idFuncionario;
	
	private String celular;
}

class ClasseSemId{
	
}

class ClassComTodosCamposMappedBy{
	
	@OneToOne(mappedBy="teste")
	private Funcionario funcionario;
	
	@OneToMany(mappedBy="teste")
	private Filho filho;
	
	@ManyToMany(mappedBy="teste")
	private Pai pai;
}
