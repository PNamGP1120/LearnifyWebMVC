package com.pnam.services;

import java.util.Map;

public interface VnPayService {

    String createPaymentUrl(Long enrollmentId, Long amount, String orderInfo, String ipAddr);

    boolean verify(Map<String, String> params);
}
