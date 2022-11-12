package cn.zflzqy.test.netty;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import io.netty.channel.Channel;
 
/**
 * netty活跃连接
 */
public class ChannelMap {
    public static int channelNum=0;
    // concurrentHashmap以解决多线程冲突
    private static TimedCache<String,Channel> channelHashMap=null;
 
    public static TimedCache<String, Channel> getChannelHashMap() {
        // 获取一个过期淘汰策略
        TimedCache<String, Channel> timedCache = CacheUtil.newTimedCache(30000);
        timedCache.schedulePrune(30*10000);
        return timedCache;
    }
 
    public static Channel getChannelByName(String name){

        if(channelHashMap==null||channelHashMap.isEmpty()){
            return null;
        }
        return channelHashMap.get(name);
    }
    public static void addChannel(String name,Channel channel){
        if(channelHashMap==null){
            channelHashMap=new TimedCache<String,Channel>(8);
        }
        channelHashMap.put(name,channel);
        channelNum++;
    }
    public static int removeChannelByName(String name){
        if(channelHashMap.containsKey(name)){
            channelHashMap.remove(name);
            return 0;
        }else{
            return 1;
        }
    }
}
 