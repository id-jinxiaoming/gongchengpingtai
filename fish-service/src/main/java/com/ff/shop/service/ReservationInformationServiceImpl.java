package com.ff.shop.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.ff.common.base.BaseServiceImpl;
import com.ff.shop.dao.ReservationInformationMapper;
import com.ff.shop.model.ReservationInformation;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ReservationInformationServiceImpl extends BaseServiceImpl<ReservationInformation> implements ReservationInformationService{


}
