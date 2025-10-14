package cn.snowsoft.iot.module.cps.dal.redis.oauth2;

import org.springframework.stereotype.Repository;

import static cn.snowsoft.iot.module.cps.dal.redis.RedisKeyConstants.OAUTH2_ACCESS_TOKEN;

/**
 * {@link OAuth2AccessTokenDO} 的 RedisDAO
 *
 * @author chengang
 */
@Repository
public class OAuth2AccessTokenRedisDAO {

//    @Resource
//    private StringRedisTemplate stringRedisTemplate;
//
//    public OAuth2AccessTokenDO get(String accessToken) {
//        String redisKey = formatKey(accessToken);
//        return JsonUtils.parseObject(stringRedisTemplate.opsForValue().get(redisKey), OAuth2AccessTokenDO.class);
//    }
//
//    public void set(OAuth2AccessTokenDO accessTokenDO) {
//        String redisKey = formatKey(accessTokenDO.getAccessToken());
//        // 清理多余字段，避免缓存
//        accessTokenDO.setUpdater(null).setUpdateTime(null).setCreateTime(null).setCreator(null).setDeleted(null);
//        long time = LocalDateTimeUtil.between(LocalDateTime.now(), accessTokenDO.getExpiresTime(), ChronoUnit.SECONDS);
//        if (time > 0) {
//            stringRedisTemplate.opsForValue().set(redisKey, JsonUtils.toJsonString(accessTokenDO), time, TimeUnit.SECONDS);
//        }
//    }
//
//    public void delete(String accessToken) {
//        String redisKey = formatKey(accessToken);
//        stringRedisTemplate.delete(redisKey);
//    }
//
//    public void deleteList(Collection<String> accessTokens) {
//        List<String> redisKeys = CollectionUtils.convertList(accessTokens, OAuth2AccessTokenRedisDAO::formatKey);
//        stringRedisTemplate.delete(redisKeys);
//    }
//
//    private static String formatKey(String accessToken) {
//        return String.format(OAUTH2_ACCESS_TOKEN, accessToken);
//    }

}
