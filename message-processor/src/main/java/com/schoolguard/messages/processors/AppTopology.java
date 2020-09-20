package com.schoolguard.messages.processors;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import com.schoolguard.messages.processors.bolts.*;
import com.schoolguard.messages.processors.guice.InjectorProvider;
import com.schoolguard.messages.processors.spouts.SpoutFactory;
import com.schoolguard.messages.processors.spouts.SpoutId;
import com.schoolguard.messages.producers.Topic;
import org.I0Itec.zkclient.ZkClient;
import org.apache.log4j.Logger;
import kafka.utils.ZKStringSerializer$;

import java.util.Properties;

/**
 * Created by Rogers on 15-4-20.
 */
public class AppTopology {
    private static Logger logger = Logger.getLogger(AppTopology.class);

    public static void main(String[] args) throws Exception{

        if (args != null && args.length > 0) {
            StormSubmitter.submitTopology(
                    args[0],
                    createConfig(false),
                    createTopology());

        } else {
            final LocalCluster cluster = new LocalCluster();
            cluster.submitTopology(
                    "local-school-message-topology",
                    createConfig(true),
                    createTopology());

            Runtime.getRuntime().addShutdownHook(new Thread(){
                @Override
                public void run() {
                    System.out.println("Shutting down local-school-message-topology");
                    cluster.killTopology("local-school-message-topology");
                    cluster.shutdown();
                }
            });

            while ( !Thread.interrupted()) {
                Thread.sleep(6000);
            }
        }

    }

    public static StormTopology createTopology() {

        Injector injector = InjectorProvider.get();
        String zHost = injector.getInstance(Key.get(String.class, Names.named("zookeeper.host")));
        String zPort = injector.getInstance(Key.get(String.class, Names.named("zookeeper.port")));

        // create kafka topic
        //createKafkaTopic(zHost + ":" + zPort);

        // Topology definition
        TopologyBuilder builder = new TopologyBuilder();

        SpoutFactory spoutFactory = new SpoutFactory(zHost + ":" + zPort);
        builder.setSpout(SpoutId.PRE_NOTICE, spoutFactory.buildSpout(Topic.NOTICE_TOPIC.toString()), 1);
        builder.setSpout(SpoutId.PRE_ASSIGNMENT, spoutFactory.buildSpout(Topic.ASSIGNMENT_TOPIC.toString()), 1);
        builder.setSpout(SpoutId.PRE_EXAM, spoutFactory.buildSpout(Topic.EXAM_TOPIC.toString()), 1);
        builder.setSpout(SpoutId.PRE_OA_MSG, spoutFactory.buildSpout(Topic.OA_TOPIC.toString()), 1);
        builder.setSpout(SpoutId.PRE_ATTENDANCE, spoutFactory.buildSpout(Topic.ATTENDANCE_TOPIC.toString()), 1);

        // step 1: splitter
        builder.setBolt(BoltId.NOTICE_SPLITTER,
                injector.getInstance(NoticeSplitterBolt.class), 3)
                .shuffleGrouping(SpoutId.PRE_NOTICE);

        builder.setBolt(BoltId.ASSIGNMENT_SPLITTER,
                injector.getInstance(AssignmentSplitterBolt.class), 3)
                .shuffleGrouping(SpoutId.PRE_ASSIGNMENT);

        builder.setBolt(BoltId.OA_MSG_SPLITTER,
                injector.getInstance(OASplitterBolt.class))
                .shuffleGrouping(SpoutId.PRE_OA_MSG);

        builder.setBolt(BoltId.EXAM_SPLITTER,
                injector.getInstance(ExamSplitterBolt.class))
                .shuffleGrouping(SpoutId.PRE_EXAM);

        builder.setBolt(BoltId.ATTENDANCE_SPLITTER,
                injector.getInstance(AttendanceSplitterBolt.class))
                .shuffleGrouping(SpoutId.PRE_ATTENDANCE);


        // step 3: permission checker
        builder.setBolt(BoltId.PERMISSION_FILTER,
                injector.getInstance(PermissionCheckBolt.class), 3)
                .shuffleGrouping(BoltId.NOTICE_SPLITTER)
                .shuffleGrouping(BoltId.ASSIGNMENT_SPLITTER)
                .shuffleGrouping(BoltId.OA_MSG_SPLITTER)
                .shuffleGrouping(BoltId.EXAM_SPLITTER)
                .shuffleGrouping(BoltId.ATTENDANCE_SPLITTER);

        // step 4: duplication filter
        builder.setBolt(BoltId.DUPLICATE_FILTER,
                injector.getInstance(DuplicateFilterBolt.class), 4)
                .fieldsGrouping(BoltId.PERMISSION_FILTER, new Fields("data"));

        // step 5: persistence
        builder.setBolt(BoltId.PERSISTENCE,
                injector.getInstance(PersistenceBolt.class), 5)
                .shuffleGrouping(BoltId.DUPLICATE_FILTER);

        // ----------- 第二部分：从kafka再次取出处理好的消息，进行发送 ------------
        builder.setSpout(SpoutId.NOTICE, spoutFactory.buildSpout(Topic.PRE_SEND_NOTICE.toString()), 1);
        builder.setSpout(SpoutId.ASSIGNMENT, spoutFactory.buildSpout(Topic.PRE_SEND_ASSIGNMENT.toString()), 1);
        builder.setSpout(SpoutId.EXAM, spoutFactory.buildSpout(Topic.PRE_SEND_EXAM.toString()), 1);
        builder.setSpout(SpoutId.OA_MSG, spoutFactory.buildSpout(Topic.PRE_SEND_OA_MSG.toString()), 1);
        builder.setSpout(SpoutId.ATTENDANCE, spoutFactory.buildSpout(Topic.PRE_SEND_ATTENDANCE.toString()), 1);


        builder.setBolt(BoltId.SORT_OUT, injector.getInstance(MsgSortOutBolt.class), 5)
                .shuffleGrouping(SpoutId.NOTICE)
                .shuffleGrouping(SpoutId.ASSIGNMENT)
                .shuffleGrouping(SpoutId.EXAM)
                .shuffleGrouping(SpoutId.OA_MSG)
                .shuffleGrouping(SpoutId.ATTENDANCE);

        // step 4: message sender
        builder.setBolt(BoltId.SMS_SENDER,
                injector.getInstance(SendSMSBolt.class))
                .shuffleGrouping(BoltId.SORT_OUT, MsgSortOutBolt.SMS_STREAM);

        builder.setBolt(BoltId.WEIXIN_SENDER,
                injector.getInstance(SendWeixinBolt.class))
                .shuffleGrouping(BoltId.SORT_OUT, MsgSortOutBolt.WEIXIN_STREAM);

        /* ----业务无关的短信通道 ----*/

        builder.setSpout(SpoutId.COMMON_SMS, spoutFactory.buildSpout(Topic.COMMON_SMS_TOPIC.toString()), 1);

        builder.setBolt(BoltId.COMMON_SMS_SENDER,
                injector.getInstance(SendCommonSMSBolt.class))
                .shuffleGrouping(SpoutId.COMMON_SMS);

        return builder.createTopology();
    }

    private static Config createConfig(boolean local){
        Injector injector = InjectorProvider.get();
        int workerNumbers = Integer.valueOf(injector.getInstance(Key.get(String.class, Names.named("storm.workers"))));
        Config config = new Config();
        config.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
        config.put(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS, 60);
        if (local) {
            config.setDebug(true);
            config.setMaxTaskParallelism(3);
        } else {
            config.setNumWorkers(workerNumbers);
            config.setDebug(false);
        }
        return config;
    }

    public static void createKafkaTopic(String zkServer){
        ZkClient client = new ZkClient(zkServer,10000, 10000, ZKStringSerializer$.MODULE$);    //使用ZKStringSerializer$.MODULE$很重要，否则kafka broker不知道此处创建的topic信息。
        for(Topic t: Topic.values()){
            try {
                // 1 partition and 1 replica
                kafka.admin.AdminUtils.createTopic(client, t.toString(), 10, 1, new Properties());
            }catch (kafka.common.TopicExistsException e){
                logger.warn("Topic已存在，跳过：", e);
            }
        }
    }
}
