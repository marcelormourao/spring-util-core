package br.com.noteschool.springutilcore.repository.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import br.com.noteschool.springutilcore.repository.GenericCustomRepositoryImpl;

/**
 * @author marcelo.mourao
 * @since  18/09/2017
 * @see AcentuacaoInterceptor
 * 
 * Altera parâmetros de comparação e ordenação da session. Após o fim do método interceptado, mesmo que
 * ocorra algum erro os parametros são novamente alterados para os valores padrões.
 */
@Aspect
@Component
public class AcentuacaoInterceptorHandler extends GenericCustomRepositoryImpl<Object, Long>{
	
	@Around("@annotation(AcentuacaoInterceptor)")
	public Object alterarParametrosAcentuacaoOrdenacao(ProceedingJoinPoint joinPoint) throws Throwable {
		
		try{
			alterarParametrosDeComparacaoEOrdenacaoDaSessao();
		 
		    Object proceed = joinPoint.proceed();
		    
		    return proceed;
		}finally {
			retornarParametrosDeComparacaoEOrdenacaoDaSessao();
		}
	}
}


