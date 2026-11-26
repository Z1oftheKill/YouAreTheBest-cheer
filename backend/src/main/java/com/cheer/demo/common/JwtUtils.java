package com.cheer.demo.common;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * 轻量级密码哈希与 JWT（JSON Web Token）签发/验签工具类
 *
 * 设计亮点：
 * 完全基于 JDK 21 内置加密库（SHA-256 + HmacSHA256）手写实现，零第三方黑盒依赖，
 * 方便从前端视角彻底看懂 Token 是如何生成的、又是如何防止前端篡改的！
 */
public class JwtUtils {

    // 密码加盐前缀（防止彩虹表暴力破解明文密码）
    private static final String PASSWORD_SALT = "cheer-salt:";

    // 服务端私密签名密钥（真实生产环境中应放在环境变量里，绝不能泄露给前端）
    private static final String JWT_SECRET = "CheerJavaWebDemoSecretKey2026!@#";

    // Token 默认有效时长：7 天（单位：毫秒）
    private static final long EXPIRE_MS = 7L * 24 * 60 * 60 * 1000;

    /**
     * 1. 将用户输入的明文密码转为加盐 SHA-256 十六进制字符串（64 位小写）
     * 数据库绝不存明文密码 "123456"，只存这个不可逆的哈希值！
     */
    public static String hashPassword(String rawPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest((PASSWORD_SALT + rawPassword).getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : digest) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException("密码加密异常", e);
        }
    }

    /**
     * 2. 登录成功后签发 JWT Token（标准三段式：Header.Payload.Signature）
     */
    public static String generateToken(Long userId, String username) {
        long exp = System.currentTimeMillis() + EXPIRE_MS;
        // 第一段 Header：声明算法为 HS256
        String headerJson = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        // 第二段 Payload：存放用户 ID、用户名与过期时间戳（注意：Payload 只是 Base64 编码，不能放密码等敏感信息）
        String payloadJson = String.format("{\"uid\":%d,\"sub\":\"%s\",\"exp\":%d}", userId, username, exp);

        String headerB64 = base64UrlEncode(headerJson.getBytes(StandardCharsets.UTF_8));
        String payloadB64 = base64UrlEncode(payloadJson.getBytes(StandardCharsets.UTF_8));
        String content = headerB64 + "." + payloadB64;

        // 第三段 Signature：用服务端私钥对前两段计算 HMAC-SHA256 签名（前端一旦篡改 uid，签名就会对不上！）
        String signature = signHmacSha256(content);
        return content + "." + signature;
    }

    /**
     * 3. 校验请求头传来的 Token 并解析出当前登录的用户 ID
     * 如果 Token 格式不对、签名被篡改、或者已过期，统一返回 null
     */
    public static Long verifyAndGetUserId(String token) {
        try {
            if (token == null || token.isBlank()) {
                return null;
            }
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return null;
            }
            // 重新计算签名并与第三段比对，防止伪造或篡改
            String expectedSig = signHmacSha256(parts[0] + "." + parts[1]);
            if (!MessageDigest.isEqual(
                    expectedSig.getBytes(StandardCharsets.UTF_8),
                    parts[2].getBytes(StandardCharsets.UTF_8))) {
                return null;
            }
            // 解码第二段 Payload，检查是否过期并提取 uid
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            long exp = extractLongField(payloadJson, "\"exp\":");
            if (System.currentTimeMillis() > exp) {
                return null;
            }
            return extractLongField(payloadJson, "\"uid\":");
        } catch (Exception e) {
            return null;
        }
    }

    private static String signHmacSha256(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(JWT_SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return base64UrlEncode(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException("JWT 签名计算失败", e);
        }
    }

    private static String base64UrlEncode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static Long extractLongField(String json, String key) {
        int start = json.indexOf(key);
        if (start < 0) {
            throw new IllegalArgumentException("Missing key: " + key);
        }
        start += key.length();
        int end = start;
        while (end < json.length() && Character.isDigit(json.charAt(end))) {
            end++;
        }
        return Long.parseLong(json.substring(start, end));
    }
}

