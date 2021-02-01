package com.debug.pmp.server.service;

import java.util.Set;

/**
 * Created by Administrator on 2019/8/5.
 */
public interface CommonDataService {

    Set<Long> getCurrUserDataDeptIds();

    String getCurrUserDataDeptIdsStr();
}
