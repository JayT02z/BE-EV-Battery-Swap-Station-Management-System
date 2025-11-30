package com.boilerplate.auth.utils;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class CacheKeyGeneratorUtil {

  // Khởi tạo ObjectMapper một lần và tái sử dụng
  private static final ObjectMapper objectMapper = JsonMapper.builder()
      .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
      .build();

  /**
   * Tạo cache key từ một object bất kỳ.
   *
   * @param prefix Tiền tố để phân loại key trong Redis (ví dụ: "users", "products").
   * @param object Object cần tạo key.
   * @param <T>    Kiểu của object.
   * @return Một chuỗi hash MD5 dùng làm key cho Redis.
   */
  public static <T> String generateKey(String prefix, T object) {
    if (object == null) {
      // Xử lý trường hợp object null nếu cần
      return prefix + ":null";
    }

    try {
      // Bước 1: Serialize object thành chuỗi JSON (đã được sắp xếp thuộc tính)
      String jsonString = objectMapper.writeValueAsString(object);

      // Bước 2: Hash chuỗi JSON bằng MD5
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] messageDigest = md.digest(jsonString.getBytes(StandardCharsets.UTF_8));

      // Bước 3: Chuyển byte array thành dạng hex string
      BigInteger no = new BigInteger(1, messageDigest);
      StringBuilder hashtext = new StringBuilder(no.toString(16));
      while (hashtext.length() < 32) {
        hashtext.insert(0, "0");
      }

      return prefix + ":" + hashtext;

    } catch (Exception e) {
      // Trong trường hợp lỗi, ném ra một RuntimeException để xử lý
      throw new RuntimeException("Could not generate cache key for object: " + object.toString(),
          e);
    }
  }
}
