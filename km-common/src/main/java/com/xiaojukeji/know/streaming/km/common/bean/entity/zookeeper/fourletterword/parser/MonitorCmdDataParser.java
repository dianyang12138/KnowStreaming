package com.xiaojukeji.know.streaming.km.common.bean.entity.zookeeper.fourletterword.parser;

import com.didiglobal.logi.log.ILog;
import com.didiglobal.logi.log.LogFactory;
import com.xiaojukeji.know.streaming.km.common.bean.entity.zookeeper.fourletterword.MonitorCmdData;
import com.xiaojukeji.know.streaming.km.common.utils.zookeeper.FourLetterWordUtil;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * zk_version      3.4.6-1569965, built on 02/20/2014 09:09 GMT
 * zk_avg_latency  0
 * zk_max_latency  399
 * zk_min_latency  0
 * zk_packets_received     234857
 * zk_packets_sent 234860
 * zk_num_alive_connections        4
 * zk_outstanding_requests 0
 * zk_server_state follower
 * zk_znode_count  35566
 * zk_watch_count  39
 * zk_ephemerals_count     10
 * zk_approximate_data_size        3356708
 * zk_open_file_descriptor_count   35
 * zk_max_file_descriptor_count    819200
 */
@Data
public class MonitorCmdDataParser implements FourLetterWordDataParser<MonitorCmdData> {
    private static final ILog LOGGER = LogFactory.getLog(MonitorCmdDataParser.class);

    @Override
    public String getCmd() {
        return FourLetterWordUtil.MonitorCmd;
    }

    @Override
    public MonitorCmdData parseAndInitData(Long clusterPhyId, String host, int port, String cmdData) {
        Map<String, String> dataMap = new HashMap<>();
        for (String elem : cmdData.split("\n")) {
            if (elem.isEmpty()) {
                continue;
            }

            int idx = elem.indexOf('\t');
            if (idx >= 0) {
                dataMap.put(elem.substring(0, idx), elem.substring(idx + 1).trim());
            }
        }

        MonitorCmdData monitorCmdData = new MonitorCmdData();
        dataMap.entrySet().stream().forEach(elem -> {
            try {
                switch (elem.getKey()) {
                    case "zk_version":
                        monitorCmdData.setZkVersion(elem.getValue().split("-")[0]);
                        break;
                    case "zk_avg_latency":
                        monitorCmdData.setZkAvgLatency(Long.valueOf(elem.getValue()));
                        break;
                    case "zk_max_latency":
                        monitorCmdData.setZkMaxLatency(Long.valueOf(elem.getValue()));
                        break;
                    case "zk_min_latency":
                        monitorCmdData.setZkMinLatency(Long.valueOf(elem.getValue()));
                        break;
                    case "zk_packets_received":
                        monitorCmdData.setZkPacketsReceived(Long.valueOf(elem.getValue()));
                        break;
                    case "zk_packets_sent":
                        monitorCmdData.setZkPacketsSent(Long.valueOf(elem.getValue()));
                        break;
                    case "zk_num_alive_connections":
                        monitorCmdData.setZkNumAliveConnections(Long.valueOf(elem.getValue()));
                        break;
                    case "zk_outstanding_requests":
                        monitorCmdData.setZkOutstandingRequests(Long.valueOf(elem.getValue()));
                        break;
                    case "zk_server_state":
                        monitorCmdData.setZkServerState(elem.getValue());
                        break;
                    case "zk_znode_count":
                        monitorCmdData.setZkZnodeCount(Long.valueOf(elem.getValue()));
                        break;
                    case "zk_watch_count":
                        monitorCmdData.setZkWatchCount(Long.valueOf(elem.getValue()));
                        break;
                    case "zk_ephemerals_count":
                        monitorCmdData.setZkEphemeralsCount(Long.valueOf(elem.getValue()));
                        break;
                    case "zk_approximate_data_size":
                        monitorCmdData.setZkApproximateDataSize(Long.valueOf(elem.getValue()));
                        break;
                    case "zk_open_file_descriptor_count":
                        monitorCmdData.setZkOpenFileDescriptorCount(Long.valueOf(elem.getValue()));
                        break;
                    case "zk_max_file_descriptor_count":
                        monitorCmdData.setZkMaxFileDescriptorCount(Long.valueOf(elem.getValue()));
                        break;
                    default:
                        LOGGER.warn(
                                "class=MonitorCmdDataParser||method=parseAndInitData||name={}||value={}||msg=data not parsed!",
                                elem.getKey(), elem.getValue()
                        );
                }
            } catch (Exception e) {
                LOGGER.error(
                        "class=MonitorCmdDataParser||method=parseAndInitData||clusterPhyId={}||host={}||port={}||name={}||value={}||errMsg=exception!",
                        clusterPhyId, host, port, elem.getKey(), elem.getValue(), e
                );
            }
        });

        return monitorCmdData;
    }
}
