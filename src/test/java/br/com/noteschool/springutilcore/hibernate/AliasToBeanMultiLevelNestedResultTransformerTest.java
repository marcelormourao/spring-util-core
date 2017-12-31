package br.com.noteschool.springutilcore.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;

import org.junit.Test;

import br.com.noteschool.springutilcore.util.hibernate.AliasToBeanMultiLevelNestedResultTransformer;
import lombok.Getter;
import lombok.Setter;

/**
 * @author marcelo.mourao
 * @since  18/10/2017
 */
public class AliasToBeanMultiLevelNestedResultTransformerTest {
	
	private static final AliasToBeanMultiLevelNestedResultTransformer transformer = new AliasToBeanMultiLevelNestedResultTransformer(Nivel3.class);

	@Test
	public void testaCapacidadePreencherObjetoComVariosNiveis(){
		
		Object[] tuples = new Object[]{
				new BigDecimal(14),
				new BigDecimal(123),
				new BigDecimal(6454),
				"descricaoDoNivel1",
				new BigDecimal(666),
			};
		
		String[] aliases = new String[]{
			"id",
			"nivel2.id",
			"nivel2.nivel1.id",
			"nivel2.nivel1.descricao",
			"nivel2.nivel1.nivel3.nivel2.nivel1.id"
		};
		
		Nivel3 result = (Nivel3) transformer.transformTuple(tuples, aliases);
		
		assertNotNull(result);
		assertNull(result.getDescricao());
		assertNotNull(result.getNivel2().getId());
		assertEquals((Object) result.getNivel2().getId(), Integer.parseInt("123"));
		assertEquals(result.getNivel2().getNivel1().getId(), new BigDecimal(6454));
		assertEquals(result.getNivel2().getNivel1().getDescricao(), "descricaoDoNivel1");
		assertEquals(result.getNivel2().getNivel1().getNivel3().getNivel2().getNivel1().getId(), new BigDecimal(666));
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testaFalhaSeQuantidadeCamposMenorAliases(){
		
		Object[] tuples = new Object[]{
				new BigDecimal(14),
				new BigDecimal(123),
			};
		
		String[] aliases = new String[]{
			"id",
			"nivel2.id",
			"nivel2.nivel1.id"
		};
		
		transformer.transformTuple(tuples, aliases);
	}
}

@Getter @Setter
class Nivel3{
	private Long id;
	private Nivel2 nivel2;
	private String descricao;
}

@Getter @Setter
class Nivel2{
	private Integer id;
	private Nivel1 nivel1;
	private String descricao;
}

@Getter @Setter
class Nivel1{
	private BigDecimal id;
	private String descricao;
	private Nivel3 nivel3;
}