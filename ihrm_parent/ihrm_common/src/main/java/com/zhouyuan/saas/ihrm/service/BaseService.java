package com.zhouyuan.saas.ihrm.service;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class BaseService<T> {

    /**
     * 因为很多Service都要用到根据companyId来查询的判定条件，故将此判定条件抽为公共的
     * @param companyId
     * @return
     */
    protected Specification<T> getSpecification(String companyId){
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("companyId").as(String.class),companyId);
            }
        };
    }
}
