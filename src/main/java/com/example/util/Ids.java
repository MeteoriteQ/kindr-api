package com.example.util;
import java.security.SecureRandom;
import java.util.HexFormat;
public final class Ids {
 private static final SecureRandom R=new SecureRandom();
 private Ids(){}
 public static String id(String prefix){byte[] b=new byte[6];R.nextBytes(b);return prefix+"_"+System.currentTimeMillis()+"_"+HexFormat.of().formatHex(b);}
 public static String token(){byte[] b=new byte[32];R.nextBytes(b);return HexFormat.of().formatHex(b);}
}
