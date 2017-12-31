package br.com.noteschool.springutilcore.util;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @author marcelo.mourao
 * @since 20/03/2017
 */
public class CriteriaUtil {

	public static Criteria setPageable(Criteria criteria, Pageable pageable) {
		criteria
		.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
	    .setMaxResults(pageable.getPageSize());
		
		return setOrderCriteria(criteria, pageable);
	}
	
	public static Criteria setOrderCriteria(Criteria criteria, Pageable pageable){
		if(pageable.getSort() != null){
			org.hibernate.criterion.Order[] orders = toOrders(pageable.getSort()).toArray(new org.hibernate.criterion.Order[0]);
			
			for (org.hibernate.criterion.Order o : orders) {
	            criteria.addOrder(o);
	        }
		}
		
		return criteria;
	}
	
	public static List<org.hibernate.criterion.Order> toOrders(Sort sort) {
        List<org.hibernate.criterion.Order> orders = new ArrayList<org.hibernate.criterion.Order>();

        for (org.springframework.data.domain.Sort.Order order : sort) {
            if (order.getDirection() == Sort.Direction.ASC)
                orders.add(org.hibernate.criterion.Order.asc(order.getProperty()));
            else
                orders.add(org.hibernate.criterion.Order.desc(order.getProperty()));
        }

        return orders;
    }
}
