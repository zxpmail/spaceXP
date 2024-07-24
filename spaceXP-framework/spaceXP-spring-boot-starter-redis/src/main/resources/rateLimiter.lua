--获取KEY,针对那个接口进行限流，Lua脚本中的数组索引默认是从1开始的而不是从零开始。
local key = KEYS[1]
--获取注解上标注的限流次数
local limit = tonumber(ARGV[1])

local currentLimit = tonumber(redis.call('get', key) or "0")

--超过限流次数直接返回零，否则再走else分支
if currentLimit + 1 > limit
then return 0
-- 首次直接进入
else
    -- 自增长 1
    redis.call('INCRBY', key, 1)
    -- 设置过期时间
    redis.call('EXPIRE', key, ARGV[2])
    return currentLimit + 1
end