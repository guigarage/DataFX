package org.datafx.reader;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

class OAuth {

    public static final String SIGNATURE = "oauth_signature";
    public static final String NONCE = "oauth_nonce";
    public static final String VERSION = "oauth_version";
    public static final String CONSUMER_KEY = "oauth_consumer_key";
    public static final String SIGNATURE_METHOD = "oauth_signature_method";
    public static final String TIMESTAMP = "oauth_timestamp";

    public static String getHeader(String method, String url,
           MultiValuedMap requestParams, String consumerKey, String consumerSecret)
            throws UnsupportedEncodingException, GeneralSecurityException {
        TreeSet<String> params = new TreeSet<>();
        for (Map.Entry<String, List<String>> entry: requestParams.entrySet()) {
            String key = entry.getKey();
            for (String val : entry.getValue()) {
                params.add(percentEncode(key)+"="+ percentEncode(val));

            }
        }
        String nonce = getNonce();
        params.add(percentEncode(NONCE)+"="+percentEncode(nonce));
        String version = "1.0";
        params.add(percentEncode(VERSION)+"="+percentEncode(version));
        String timeStamp = Long.toString(System.currentTimeMillis() / 1000);
        params.add(percentEncode(TIMESTAMP)+"="+percentEncode(timeStamp));
        String signatureMethod = "HMAC-SHA1";
        params.add(percentEncode(SIGNATURE_METHOD)+"="+percentEncode(signatureMethod));
        params.add(percentEncode(CONSUMER_KEY)+"="+percentEncode(consumerKey));
        int idx = 0;
        int psize = params.size();
        StringBuilder sb = new StringBuilder();
        for (String part : params) {
            sb.append(part);
            if (++idx < psize) {
                sb.append("&");
            }
        }
        String baseString = method + "&" + percentEncode(url) + "&" + percentEncode(sb.toString());
        String signKey = percentEncode(consumerSecret) + "&";
        String signature = computeSignature(baseString, signKey);
        StringBuilder sh = new StringBuilder("OAuth ");
        sh.append(percentEncode(SIGNATURE)).append("=\"").append(percentEncode(signature)).append("\"");
        sh.append(", ");
        sh.append(percentEncode(NONCE)).append("=\"").append(percentEncode(nonce)).append("\"");
        sh.append(", ");
        sh.append(percentEncode(VERSION)).append("=\"").append(percentEncode(version)).append("\"");
        sh.append(", ");
        sh.append(percentEncode(CONSUMER_KEY)).append("=\"").append(percentEncode(consumerKey)).append("\"");
        sh.append(", ");
        sh.append(percentEncode(SIGNATURE_METHOD)).append("=\"").append(percentEncode(signatureMethod)).append("\"");
        sh.append(", ");
        sh.append(percentEncode(TIMESTAMP)).append("=\"").append(percentEncode(timeStamp)).append("\"");
        return sh.toString();
    }

    private static String getNonce() {
        String answer = UUID.randomUUID().toString();
        return answer;
    }

    private static String percentEncode(String s) {
        if (s == null) {
            return "";
        }
        try {
            return URLEncoder.encode(s, "UTF-8")
                    .replace("+", "%20").replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (UnsupportedEncodingException wow) {
            throw new RuntimeException(wow.getMessage(), wow);
        }
    }

    private static String computeSignature(String baseString, String secret) throws GeneralSecurityException, UnsupportedEncodingException {
        byte[] sb = secret.getBytes();
        SecretKey secretKey = new SecretKeySpec(sb, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(secretKey);
        byte[] text = baseString.getBytes();
        byte[] digest = mac.doFinal(text);
        String answer = Base64.encode(digest);
        return answer;
    }
}
