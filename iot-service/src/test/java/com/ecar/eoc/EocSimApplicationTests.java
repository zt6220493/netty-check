//package com.ecar.eoc;
//
//import com.alibaba.fastjson.JSON;
//import com.ecar.eoc.entity.eoc.MsgTerminal;
//import com.ecar.eoc.service.TerminalService;
//import jdk.nashorn.internal.ir.Terminal;
//import org.assertj.core.util.Strings;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//public class EocSimApplicationTests {
//
////	private Logger logger = LoggerFactory.getLogger(this.getClass());
//
////	@Autowired
////	private RedisTemplate<String,Object>  redisTemple;
//	@Autowired
//    private MongoTemplate mongoTemplate;
//
//	@Test
//	public void contextLoads() throws Exception {
//		int terminalId = 1001;
////		Object str = redisTemple.opsForValue().get("thievery_"+terminalId);
////		System.out.println(str+"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
////		Long t = 11111l;
////		redisTemple.opsForValue().set("thievery_"+terminalId, t);
////		logger.info(""+System.currentTimeMillis());
////		Object str2 = redisTemple.opsForValue().get("thievery_"+terminalId);
//		System.out.println(terminalId+"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//	}
//
//	@Test
//    public void test1() {
//        String path = "F:\\护航翼IMEI.xml";
//        readFileByLines(path);
//
//    }
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    @Autowired
//    private TerminalService terminalService;
//    /**
//     * 以行为单位读取文件，常用于读面向行的格式化文件
//     */
//    public void readFileByLines(String fileName) {
//        File file = new File(fileName);
//        BufferedReader reader = null;
//        try {
//            System.out.println("以行为单位读取文件内容，一次读一整行：");
//            reader = new BufferedReader(new FileReader(file));
//            String tempString = null;
//            int line = 1;
//            int count = 1;
//            // 一次读入一行，直到读入null为文件结束
//            while ((tempString = reader.readLine()) != null) {
//                // 显示行号
//                if (line >= 70 && (line - 70) % 34 == 0) {
//                    System.out.println("line " + line + ": " + tempString);
//                    //<ss:Cell><Data ss:Type="String">35682502491600</Data></ss:Cell>
//                    String imei = tempString.split("String\">")[1].split("</Data>")[0];
//                    System.out.println(count + ": " + imei);
//
//                    MsgTerminal terminal = terminalService.getMsgTerminalByImei2(imei);
//                    System.out.println(JSON.toJSONString(terminal));
//                    if (terminal != null) {
//                        String recentRemind = (String) redisTemplate.opsForValue().get("FIRING_REMIND_" + terminal.getTerminalId());
//                        System.out.println("recentRemind :" + recentRemind);
//                        if (!Strings.isNullOrEmpty(recentRemind)) {
//                            redisTemplate.delete("FIRING_REMIND_" + terminal.getTerminalId());
//                        }
//                    } else {
//                        System.out.println("terminal is null!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                    }
//
//                    count++;
//                }
////                System.out.println("line " + line + ": " + tempString);
//                line++;
//            }
//            reader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (IOException e1) {
//                }
//            }
//        }
//    }
//
//}
