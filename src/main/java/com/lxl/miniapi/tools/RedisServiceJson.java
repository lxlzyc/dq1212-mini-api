package com.lxl.miniapi.tools;

import com.lxl.miniapi.util.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;


@Repository
public class RedisServiceJson {

    @Qualifier("redisTemplate_API")
    @Autowired
    private RedisTemplate<String, Object> redisTemplate_API;

//    @Qualifier("redisTemplate_RCMD")
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate_RCMD;

    private RedisTemplate<String, Object> getTemplate() {
//        if (RedisSourceType.API_SOURCE.equals(redisSource)) {
//            return redisTemplate_API;
//        } else if (RedisSourceType.RCMD_SOURCE.equals(redisSource)) {
//            return redisTemplate_RCMD;
//        } else {
            return redisTemplate_API;
//        }
    }

    // =============================common============================

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        return this.expire(key, time);
    }

    public boolean expire(RedisSourceType redisSource, String key, long time) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return this.getExpire(key);
    }

    public long getExpire(RedisSourceType redisSource, String key) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        return this.hasKey(key);
    }

    public boolean hasKey(RedisSourceType redisSource, String key) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public void del(String... key) {
        this.del(key);
    }

    @SuppressWarnings("unchecked")
    public void del(RedisSourceType redisSource, String... key) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            if (key != null && key.length > 0) {
                if (key.length == 1) {
                    redisTemplate.delete(key[0]);
                } else {
                    redisTemplate.delete(CollectionUtils.arrayToList(key));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // ============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public String get(String key) {
        return this.get(key);
    }

    public String get(RedisSourceType redisSource, String key) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            return key == null ? null : (String) redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    // ============================Object=============================

    /**
     * 对象缓存获取
     *
     * @param key 键
     * @return 值
     */
    public <T> T get(String key, Class<T> c) {
        return this.get(key, c);
    }

    public <T> T get(RedisSourceType redisSource, String key, Class<T> c) {

        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            if (key == null)
                return null;
            T result;
            String json = (String) redisTemplate.opsForValue().get(key);
            if (json != null) {
                result = JSONUtil.toBean(json, c);
                return result;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 返回 list
     *
     * @param key
     * @param c
     * @return
     */
    public <T> List<T> getToList(String key, Class<T> c) {
        return this.getToList(key, c);
    }

    public <T> List<T> getToList(RedisSourceType redisSource, String key, Class<T> c) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            if (key == null)
                return null;
            List<T> result;
            String json = (String) redisTemplate.opsForValue().get(key);
            if (json != null) {
                result = JSONUtil.toList(json, c);
                return result;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 对象缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        return this.set(key, value);
    }

    public boolean set(RedisSourceType redisSource, String key, Object value) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            String json;
            if (value instanceof String) {
                json = (String) value;
            } else {
                json = JSONUtil.toJson(value);
            }
            redisTemplate.opsForValue().set(key, json);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 对象缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        return this.set(key, value, time);
    }

    public boolean set(RedisSourceType redisSource, String key, Object value, long time) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            String json;
            if (value instanceof String) {
                json = (String) value;
            } else {
                json = JSONUtil.toJson(value);
            }
            if (time > 0) {
                redisTemplate.opsForValue().set(key, json, time, TimeUnit.SECONDS);
            } else {
                set(key, json);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta) {
        return this.incr(key, delta);
    }

    public long incr(RedisSourceType redisSource, String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }

        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            return redisTemplate.opsForValue().increment(key, delta);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta) {
        return this.decr(key, delta);
    }

    public long decr(RedisSourceType redisSource, String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            return redisTemplate.opsForValue().increment(key, -delta);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    // ================================Map=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {
        return this.hget(key, item);
    }

    public Object hget(RedisSourceType redisSource, String key, String item) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            return redisTemplate.opsForHash().get(key, item);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public <T> T hget(String key, String item, Class<T> c) {
        return this.hget(key, item, c);
    }

    public <T> T hget(RedisSourceType redisSource, String key, String item, Class<T> c) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            if (key == null)
                return null;
            T result;
            String json = (String) redisTemplate.opsForHash().get(key, item);
            if (json != null) {
                result = JSONUtil.toBean(json, c);
                return result;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * hgetList
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public <T> List<T> hgetList(String key, String item, Class<T> c) {
        return this.hgetList(key, item, c);
    }

    public <T> List<T> hgetList(RedisSourceType redisSource, String key, String item, Class<T> c) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            if (key == null)
                return null;
            List<T> result;
            String json = (String) redisTemplate.opsForHash().get(key, item);
            if (json != null) {
                result = JSONUtil.toList(json, c);
                return result;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 获取整个哈希存储的值
     *
     * @param key
     * @param c
     * @return
     */
    public <T> List<T> hmvget(String key, Class<T> c) {
        return this.hmvget(key, c);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> hmvget(RedisSourceType redisSource, String key, Class<T> c) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            List<T> result = new ArrayList<>();
            if (c == String.class) {
                return (List<T>) redisTemplate.opsForHash().values(key);
            } else {
                List<Object> result_json = redisTemplate.opsForHash().values(key);
                for (int i = 0; i < result_json.size(); i++) {
                    result.add(JSONUtil.toBean((String) result_json.get(i), c));
                }
                if (result.size() == 0) {
                    return null;
                }
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        return this.hset(key, item, value);
    }

    public boolean hset(RedisSourceType redisSource, String key, String item, Object value) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            String json;
            if (value instanceof String) {
                json = (String) value;
            } else {
                json = JSONUtil.toJson(value);
            }

            redisTemplate.opsForHash().put(key, item, json);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        return this.hset(key, item, value, time);
    }

    public boolean hset(RedisSourceType redisSource, String key, String item, Object value, long time) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            String json;
            if (value instanceof String) {
                json = (String) value;
            } else {
                json = JSONUtil.toJson(value);
            }

            redisTemplate.opsForHash().put(key, item, json);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet
     *
     * @param <T>
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public <T> boolean hmset(String key, Map<String, T> map) {
        return this.hmset(key, map);
    }

    public <T> boolean hmset(RedisSourceType redisSource, String key, Map<String, T> map) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            Map<String, String> json = new HashMap<String, String>();
            for (Map.Entry<String, T> entry : map.entrySet()) {
                if (entry.getValue() instanceof String) {
                    json.put(entry.getKey(), (String) entry.getValue());
                } else {
                    json.put(entry.getKey(), JSONUtil.toJson(entry.getValue()));
                }
            }
            redisTemplate.opsForHash().putAll(key, json);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public <T> boolean hmset(String key, Map<String, T> map, long time) {
        return this.hmset(key, map, time);
    }

    public <T> boolean hmset(RedisSourceType redisSource, String key, Map<String, T> map, long time) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            Map<String, String> json = new HashMap<String, String>();
            for (Map.Entry<String, T> entry : map.entrySet()) {
                if (entry.getValue() instanceof String) {
                    json.put(entry.getKey(), (String) entry.getValue());
                } else {
                    json.put(entry.getKey(), JSONUtil.toJson(entry.getValue()));
                }
            }
            redisTemplate.opsForHash().putAll(key, json);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param <T>
     * @param key 键
     * @return 对应的多个键值
     */
    public <T> Map<String, T> hmget(String key, Class<T> c) {
        return this.hmget(key, c);
    }

    public <T> Map<String, T> hmget(RedisSourceType redisSource, String key, Class<T> c) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            String json = JSONUtil.toJson(redisTemplate.opsForHash().entries(key));
            if (json == null) {
                return null;
            } else {
                return JSONUtil.toMap(json, c);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        this.hdel(key, item);
    }

    public void hdel(RedisSourceType redisSource, String key, Object... item) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            redisTemplate.opsForHash().delete(key, item);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return this.hHasKey(key, item);
    }

    public boolean hHasKey(RedisSourceType redisSource, String key, String item) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            return redisTemplate.opsForHash().hasKey(key, item);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item, double by) {
        return this.hincr(key, item, by);
    }

    public double hincr(RedisSourceType redisSource, String key, String item, double by) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            return redisTemplate.opsForHash().increment(key, item, by);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item, double by) {
        return this.hdecr(key, item, by);
    }

    public double hdecr(RedisSourceType redisSource, String key, String item, double by) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            return redisTemplate.opsForHash().increment(key, item, -by);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    // ============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key) {
        return this.sGet(key);
    }

    public Set<Object> sGet(RedisSourceType redisSource, String key) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        return this.sSet(key, values);
    }

    public long sSet(RedisSourceType redisSource, String key, Object... values) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object... values) {
        return this.sSetAndTime(key, time, values);
    }

    public long sSetAndTime(RedisSourceType redisSource, String key, long time, Object... values) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0)
                expire(key, time);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        return this.sHasKey(key, value);
    }

    public boolean sHasKey(RedisSourceType redisSource, String key, Object value) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        return this.setRemove(key, values);
    }

    public long setRemove(RedisSourceType redisSource, String key, Object... values) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key) {
        return this.sGetSetSize(key);
    }

    public long sGetSetSize(RedisSourceType redisSource, String key) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     */
    public <T> List<T> lGet(String key, long start, long end, final Class<T> c) {
        return this.lGet(key, start, end, c);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> lGet(RedisSourceType redisSource, String key, long start, long end, final Class<T> c) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            List<T> result = new ArrayList<>();
            if (c == String.class) {
                return (List<T>) redisTemplate.opsForList().range(key, start, end);
            } else {
                List<Object> result_json = redisTemplate.opsForList().range(key, start, end);
                for (int i = 0; i < result_json.size(); i++) {
                    result.add(JSONUtil.toBean((String) result_json.get(i), c));
                }
                if (result.size() == 0) {
                    return null;
                }
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public <T> T lGetIndex(String key, long index, final Class<T> c) {
        return this.lGetIndex(key, index, c);
    }

    @SuppressWarnings("unchecked")
    public <T> T lGetIndex(RedisSourceType redisSource, String key, long index, final Class<T> c) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            if (c == String.class) {
                return (T) redisTemplate.opsForList().index(key, index);
            } else {
                return (T) JSONUtil.toBean((String) redisTemplate.opsForList().index(key, index), c);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long lGetListSize(String key) {
        return this.lGetListSize(key);
    }

    public long lGetListSize(RedisSourceType redisSource, String key) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, Object value) {
        return this.lSet(key, value);
    }

    public boolean lSet(RedisSourceType redisSource, String key, Object value) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            String json;
            if (value instanceof String) {
                json = (String) value;
            } else {
                json = JSONUtil.toJson(value);
            }

            if (json == null) {
                return false;
            }

            redisTemplate.opsForList().rightPush(key, json);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, Object value, long time) {
        return this.lSet(key, value, time);
    }

    public boolean lSet(RedisSourceType redisSource, String key, Object value, long time) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            String json;
            if (value instanceof String) {
                json = (String) value;
            } else {
                json = JSONUtil.toJson(value);
            }
            if (json == null) {
                return false;
            }

            redisTemplate.opsForList().rightPush(key, json);
            if (time > 0)
                expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key  键
     * @param list 值
     * @return
     */
    public <T> boolean lSetList(String key, final List<T> list) {
        return this.lSetList(key, list);
    }

    public <T> boolean lSetList(RedisSourceType redisSource, String key, final List<T> list) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            List<String> json = new ArrayList<>();

            for (Object model : list) {
                if (model instanceof String) {
                    json.add((String) model);
                } else {
                    json.add(JSONUtil.toJson(model));
                }
            }
            if (json.size() == 0) {
                return false;
            }
            Object[] jsonArray = json.toArray(new String[json.size()]);
            redisTemplate.opsForList().rightPushAll(key, jsonArray);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key  键
     * @param list 值
     * @param time 时间(秒)
     * @return
     */
    public <T> boolean lSetList(String key, final List<T> list, long time) {
        return this.lSetList(key, list, time);
    }

    public <T> boolean lSetList(RedisSourceType redisSource, String key, final List<T> list, long time) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            List<String> json = new ArrayList<>();

            for (Object model : list) {
                if (model instanceof String) {
                    json.add((String) model);
                } else {
                    json.add(JSONUtil.toJson(model));
                }
            }
            if (json.size() == 0) {
                return false;
            }
            Object[] jsonArray = json.toArray(new String[json.size()]);
            redisTemplate.opsForList().rightPushAll(key, jsonArray);

            if (time > 0)
                expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        return this.lUpdateIndex(key, index, value);
    }

    public boolean lUpdateIndex(RedisSourceType redisSource, String key, long index, Object value) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            String json;
            if (value instanceof String) {
                json = (String) value;
            } else {
                json = JSONUtil.toJson(value);
            }
            if (json == null) {
                return false;
            }
            redisTemplate.opsForList().set(key, index, json);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count count > 0 : 从表头开始向表尾搜索，移除与 VALUE 相等的元素，数量为 COUNT 。 count < 0 :
     *              从表尾开始向表头搜索，移除与 VALUE 相等的元素，数量为 COUNT 的绝对值。 count = 0 : 移除表中所有与
     *              VALUE 相等的值。
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        return this.lRemove(key, count, value);
    }

    public long lRemove(RedisSourceType redisSource, String key, long count, Object value) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            String json;
            if (value instanceof String) {
                json = (String) value;
            } else {
                json = JSONUtil.toJson(value);
            }
            Long remove = redisTemplate.opsForList().remove(key, count, json);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ===============================有序集合=================================

    /**
     * Redis 有序集合和集合一样也是string类型元素的集合,且不允许重复的成员。
     * 不同的是每个元素都会关联一个double类型的分数。redis正是通过分数来为集合中的成员进行从小到大的排序。
     * 有序集合的成员是唯一的,但分数(score)却可以重复。
     */

    /**
     * 向集合中增加一条记录,如果这个值已存在，这个值对应的score将被置为新的score
     *
     * @param key
     * @param value
     * @param score
     * @return
     */
    public boolean zSet(String key, Object value, double score) {
        return this.zSet(key, value, score);
    }

    /**
     * 向集合中增加一条记录,如果这个值已存在，这个值对应的score将被置为新的score
     *
     * @param key
     * @param value
     * @param score
     * @return
     */
    public boolean zSet(RedisSourceType redisSource, String key, Object value, double score) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            redisTemplate.opsForZSet().add(key, value, score);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 如果member是有序集key的成员，返回value的排名。如果value不是有序集key的成员，返回nil。 排名以0为底 存在返回 >-1
     * 不存在返回-1
     *
     * @param key
     * @param value
     * @return long 位置
     */
    public long zRank(String key, Object value) {
        return this.zRank(key, value);
    }

    public long zRank(RedisSourceType redisSource, String key, Object value) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            if (redisTemplate.opsForZSet().rank(key, value) == null) {
                return -1;
            }
            return redisTemplate.opsForZSet().rank(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取指定成员的score值
     *
     * @param key
     * @param value
     * @return
     */
    public double zScore(String key, Object value) {
        return this.zScore(key, value);
    }

    public double zScore(RedisSourceType redisSource, String key, Object value) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            return redisTemplate.opsForZSet().score(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 从有序集合中移除一个或者多个元素
     *
     * @param key
     * @param values
     * @return
     */
    public long zRemove(String key, Object... values) {
        return this.zRemove(key, values);
    }

    public long zRemove(RedisSourceType redisSource, String key, Object... values) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            return redisTemplate.opsForZSet().remove(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 删除给score区间的元素
     *
     * @param key
     * @param min score下限(包含)
     * @param max score上限(包含)
     * @return 删除的数量
     */
    public long zRemoveRangeByScore(String key, double min, double max) {
        return this.zRemoveRangeByScore(key, min, max);
    }

    public long zRemoveRangeByScore(RedisSourceType redisSource, String key, double min, double max) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取有序集合的成员数
     *
     * @param key
     * @return
     */
    public long zCard(String key) {
        return this.zCard(key);
    }

    public long zCard(RedisSourceType redisSource, String key) {
        try {
            RedisTemplate<String, Object> redisTemplate = this.getTemplate(redisSource);

            return redisTemplate.opsForZSet().zCard(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
