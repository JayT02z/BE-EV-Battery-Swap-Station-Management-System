package com.bill.billing.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
public class VnpaySignatureUtil {
  public static String buildQuery(Map<String, String> params) throws Exception {
    List<String> sortedKeys = new ArrayList<>(params.keySet());
    Collections.sort(sortedKeys);

    StringBuilder query = new StringBuilder();

    for (String key : sortedKeys) {
      String value = params.get(key);
      if (value != null) {
        query.append(URLEncoder.encode(key, StandardCharsets.US_ASCII))
            .append("=")
            .append(URLEncoder.encode(value, StandardCharsets.US_ASCII))
            .append("&");
      }
    }

    if (query.length() > 0) query.deleteCharAt(query.length() - 1);
    return query.toString();
  }

  public static String hmacSHA512(String key, String data) {
    try {
      Mac hmac = Mac.getInstance("HmacSHA512");
      SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
      hmac.init(secretKey);
      byte[] bytes = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));

      StringBuilder sb = new StringBuilder();
      for (byte b : bytes) sb.append(String.format("%02x", b));

      return sb.toString();
    } catch (Exception e) {
      throw new RuntimeException("Lỗi HMAC SHA512", e);
    }
  }
}
