package cn.zflzqy.test.email;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


public class EmailUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailUtil.class);
    // ip归属查询
    private static final String regionUrl = "http://whois.pconline.com.cn/ipJson.jsp?ip={}&json=true";
    // 节假日检测url;
    private static final String holidayUrl = "https://timor.tech/api/holiday/info/";
    // 配置监测mac地址
    private static final List<String> macs = new ArrayList<>();
    static {
        macs.add("00-D8-61-AB-73-AD");
//        macs.add("00-50-56-C0-00-01");
    }

    public static boolean isSendEmail(String macAddress, StringRedisTemplate stringRedisTemplate) {
        if (!macs.contains(macAddress)){
            return  false;
        }
        // 检测上次发送时间，每5分钟发送一次
        String lastTime = stringRedisTemplate.opsForValue().get(macAddress + "::lastSend");
        if (StrUtil.isNotBlank(lastTime)&&DateUtil.between(new Date(),DateUtil.parseDateTime(lastTime), DateUnit.MINUTE)<=5){
            return  false;
        }
        String format = DateUtil.format(new Date(), DatePattern.NORM_DATE_FORMATTER);
        // 判断是否是发送邮件，工作日不发送，节假日信息
        String dayInfo = stringRedisTemplate.opsForValue().get(macAddress+"::dayInfo::"+format);
        if (StrUtil.isBlank(dayInfo)){
            String body = HttpUtil.createGet(holidayUrl + format).execute().body();
            if (StrUtil.isNotBlank(body)&&JSONValidator.from(body).validate()){
                stringRedisTemplate.opsForValue().set(macAddress+"::dayInfo::"+format,body, 2,TimeUnit.DAYS);
            }else {
                LOGGER.error("获取节假日信息失败：{}",format);
                return false;
            }
        }
        // 检测邮件次数
        String times = stringRedisTemplate.opsForValue().get(macAddress+"::dayInfo::time::" + format);
        if (StrUtil.isBlank(times)){
            times = "0";
        }
        int anInt = Integer.parseInt(times);
        if (10 <= anInt){
            LOGGER.warn("今日发送邮件次数上限：{}",format);
            return  false;
        }
        // 信息
        JSONObject dayJson = JSONObject.parseObject(dayInfo);
        if (StrUtil.equalsAny(dayJson.getJSONObject("type").getString("type"),"1","2")){
            // 节假日发送登录信息
            ++anInt;
            stringRedisTemplate.opsForValue().set(macAddress+"::dayInfo::time::" + format, String.valueOf(anInt),2,TimeUnit.DAYS);
            // 设置本次发送时间
            stringRedisTemplate.opsForValue().set(macAddress + "::lastSend",DateUtil.now(),20,TimeUnit.MINUTES);
            return  true;
        }else {
            // 工作日判断时间
            DateTime am = DateUtil.parse(DateUtil.today() + " 08:30:00");
            DateTime pm = DateUtil.parse(DateUtil.today() + " 19:00:00");
            if (DateUtil.compare(new Date(),am)<0||DateUtil.compare(new Date(),pm)>0){
                ++anInt;
                stringRedisTemplate.opsForValue().set(macAddress+"::dayInfo::time::" + format, String.valueOf(anInt),2,TimeUnit.DAYS);
                // 设置本次发送时间
                stringRedisTemplate.opsForValue().set(macAddress + "::lastSend",DateUtil.now(),20,TimeUnit.MINUTES);
                return  true;
            }
        }
        return false;
    }

    /**
     * 发送电脑开机邮件
     *
     * @param hostName
     * @param loginIp
     * @throws MessagingException
     */
    public static void sendEmail(String hostName, String loginIp,String mac) throws MessagingException {


        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.qq.com");
        javaMailSender.setPort(465);
        javaMailSender.setUsername("1396954535@qq.com");
        javaMailSender.setPassword("skroottdmwowhjbd");
        javaMailSender.setDefaultEncoding("UTF-8");

        Properties properties = new Properties();
        properties.setProperty("mail.smtp.timeout", "30000");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        javaMailSender.setJavaMailProperties(properties);

        // 构建一个邮件对象
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        // true表示构建一个可以带附件的邮件对象
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        // 设置邮件主题
        mimeMessageHelper.setSubject("电脑开机邮件");
        // 设置邮件发送者
        mimeMessageHelper.setFrom("1396954535@qq.com");
        // 设置邮件接收者，可以有多个接收者
        mimeMessageHelper.addTo("1396954535@qq.com");
        // 设置邮件发送日期
        mimeMessageHelper.setSentDate(new Date());
        // 获取ip归属
        String region = "";
        String result = HttpUtil.get(StrUtil.format(regionUrl, loginIp));
        if (StrUtil.isNotBlank(result) && JSONValidator.from(result).validate()) {
            region = JSONObject.parseObject(result).getString("addr");
        }
        // 设置邮件的正文
        mimeMessageHelper.setText(StrUtil.format("<p>你的{}电脑已开机，mac地址：{},登录ip:{},登录属地：{}，时间：{}</p>", hostName, mac,loginIp, region, DateUtil.now()), true);

        // 发送邮件
        javaMailSender.send(mimeMessage);


    }
}

