package com.pnam.services.impl;

import com.pnam.services.VnPayService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VnPayServiceImpl implements VnPayService {

    @Value("${vnpay.tmn-code}")
    private String tmnCode;

    @Value("${vnpay.hash-secret}")
    private String hashSecret;

    @Value("${vnpay.url}")
    private String vnpUrl;

    @Value("${vnpay.return-url}")
    private String returnUrl;

    @Override
    public String createPaymentUrl(Long enrollmentId, Long amount, String orderInfo, String ipAddr) {
        try {
            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", "2.1.0");
            vnp_Params.put("vnp_Command", "pay");
            vnp_Params.put("vnp_TmnCode", tmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", String.valueOf(enrollmentId));
            vnp_Params.put("vnp_OrderInfo", orderInfo);
            vnp_Params.put("vnp_OrderType", "other");
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", returnUrl);
            vnp_Params.put("vnp_IpAddr", ipAddr);

            String vnp_CreateDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);

            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();

            for (int i = 0; i < fieldNames.size(); i++) {
                String fieldName = fieldNames.get(i);
                String fieldValue = vnp_Params.get(fieldName);
                if (fieldValue != null && !fieldValue.isEmpty()) {
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8))
                         .append("=")
                         .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));

                    hashData.append(fieldName).append("=").append(fieldValue);

                    if (i < fieldNames.size() - 1) {
                        query.append("&");
                        hashData.append("&");
                    }
                }
            }

            String vnp_SecureHash = hmacSHA512(hashSecret, hashData.toString());
            query.append("&vnp_SecureHash=").append(vnp_SecureHash);

            return vnpUrl + "?" + query.toString();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi tạo URL VNPay", e);
        }
    }

    @Override
    public boolean verify(Map<String, String> params) {
        try {
            String receivedHash = params.get("vnp_SecureHash");
            params.remove("vnp_SecureHash");
            params.remove("vnp_SecureHashType");

            List<String> fieldNames = new ArrayList<>(params.keySet());
            Collections.sort(fieldNames);

            StringBuilder hashData = new StringBuilder();
            for (int i = 0; i < fieldNames.size(); i++) {
                String fieldName = fieldNames.get(i);
                String fieldValue = params.get(fieldName);
                if (fieldValue != null && !fieldValue.isEmpty()) {
                    hashData.append(fieldName).append("=").append(fieldValue);
                    if (i < fieldNames.size() - 1) {
                        hashData.append("&");
                    }
                }
            }

            String expectedHash = hmacSHA512(hashSecret, hashData.toString());
            return expectedHash.equals(receivedHash);
        } catch (Exception e) {
            return false;
        }
    }

    private String hmacSHA512(String key, String data) throws Exception {
        Mac hmac = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmac.init(secretKey);
        byte[] hashBytes = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder(2 * hashBytes.length);
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
}
