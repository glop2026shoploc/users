package com.glop.users.config.security;


import com.glop.users.domain.model.AuthContext;
import com.glop.users.domain.model.UserModel;
import com.glop.users.domain.port.out.TokenPortOut;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtUtils implements TokenPortOut {
    @Value("${app.secret-key}")
    private String secretKey;
    private final JwtAuthMapper jwtAuthMapper;

    @Value("${app.expiration-time}")
    private Long expirationTime;

    @Override
    public String generateToken(AuthContext authContext){
        Map<String,Object> claims = new HashMap<>();

        claims.put("userId", authContext.getUserId().toString());
        claims.put("cityId", authContext.getCityId().toString());
        switch (authContext.getUserRole()){
            case CLIENT ->{
                claims.put("clientId", authContext.getClientId().toString());
                claims.put("role", JWTRole.CLIENT);
            } case ADMIN -> {
                    claims.put("adminId",authContext.getAdminId());
                    claims.put("role",JWTRole.ADMIN);
            } case MERCHANT -> {
                claims.put("shopId",authContext.getShopId());
                claims.put("employeeId",authContext.getEmployeeId());
                claims.put("employeeRole",jwtAuthMapper.toJWTMerchantRole(authContext.getEmployeeRole()));
                claims.put("role",JWTRole.MERCHANT);
            }
        }

        return createToken(claims,authContext.getEmail());
    }


    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = secretKey.getBytes();
        return new SecretKeySpec(keyBytes,SignatureAlgorithm.HS512.getJcaName());
    }

    public Boolean validateToken(String token, CustomUserDetails customUserDetails){
        String username = extractUsername(token);
        return (username.equals(customUserDetails.getEmail())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpirationDate(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSignKey())
                .parseClaimsJws(token)
                .getBody();
    }
}

