package com.ff.shop.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.ff.common.base.BaseServiceImpl;
import com.ff.shop.dao.FinancialNgineeringMapper;
import com.ff.shop.model.FinancialNgineering;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class FinancialNgineeringServiceImpl extends BaseServiceImpl<FinancialNgineering> implements FinancialNgineeringService {

    @Autowired
    FinancialNgineeringMapper mapper;
    @Override
    public int insert(FinancialNgineering financialNgineering) {
        mapper.insert(financialNgineering);
        return financialNgineering.getId();
    }
}
