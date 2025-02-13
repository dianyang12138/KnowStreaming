package com.xiaojukeji.know.streaming.km.common.bean.entity.zookeeper.fourletterword.parser;

import com.didiglobal.logi.log.ILog;
import com.didiglobal.logi.log.LogFactory;
import com.xiaojukeji.know.streaming.km.common.bean.entity.zookeeper.fourletterword.ServerCmdData;
import com.xiaojukeji.know.streaming.km.common.utils.zookeeper.FourLetterWordUtil;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Zookeeper version: 3.5.9-83df9301aa5c2a5d284a9940177808c01bc35cef, built on 01/06/2021 19:49 GMT
 * Latency min/avg/max: 0/0/2209
 * Received: 278202469
 * Sent: 279449055
 * Connections: 31
 * Outstanding: 0
 * Zxid: 0x20033fc12
 * Mode: leader
 * Node count: 10084
 * Proposal sizes last/min/max: 36/32/31260     leader特有
 */
@Data
public class ServerCmdDataParser implements FourLetterWordDataParser<ServerCmdData> {
    private static final ILog LOGGER = LogFactory.getLog(ServerCmdDataParser.class);

    @Override
    public String getCmd() {
        return FourLetterWordUtil.ServerCmd;
    }

    @Override
    public ServerCmdData parseAndInitData(Long clusterPhyId, String host, int port, String cmdData) {
        Map<String, String> dataMap = new HashMap<>();
        for (String elem : cmdData.split("\n")) {
            if (elem.isEmpty()) {
                continue;
            }

            int idx = elem.indexOf(':');
            if (idx >= 0) {
                dataMap.put(elem.substring(0, idx), elem.substring(idx + 1).trim());
            }
        }

        ServerCmdData serverCmdData = new ServerCmdData();
        dataMap.entrySet().stream().forEach(elem -> {
            try {
                switch (elem.getKey()) {
                    case "Zookeeper version":
                        serverCmdData.setZkVersion(elem.getValue().split("-")[0]);
                        break;
                    case "Latency min/avg/max":
                        String[] data = elem.getValue().split("/");
                        serverCmdData.setZkMinLatency(Long.valueOf(data[0]));
                        serverCmdData.setZkAvgLatency(Long.valueOf(data[1]));
                        serverCmdData.setZkMaxLatency(Long.valueOf(data[2]));
                        break;
                    case "Received":
                        serverCmdData.setZkPacketsReceived(Long.valueOf(elem.getValue()));
                        break;
                    case "Sent":
                        serverCmdData.setZkPacketsSent(Long.valueOf(elem.getValue()));
                        break;
                    case "Connections":
                        serverCmdData.setZkNumAliveConnections(Long.valueOf(elem.getValue()));
                        break;
                    case "Outstanding":
                        serverCmdData.setZkOutstandingRequests(Long.valueOf(elem.getValue()));
                        break;
                    case "Mode":
                        serverCmdData.setZkServerState(elem.getValue());
                        break;
                    case "Node count":
                        serverCmdData.setZkZnodeCount(Long.valueOf(elem.getValue()));
                        break;
                    case "Zxid":
                        serverCmdData.setZkZxid(Long.parseUnsignedLong(elem.getValue().trim().substring(2), 16));
                        break;
                    default:
                        LOGGER.warn(
                                "class=ServerCmdDataParser||method=parseAndInitData||name={}||value={}||msg=data not parsed!",
                                elem.getKey(), elem.getValue()
                        );
                }
            } catch (Exception e) {
                LOGGER.error(
                        "class=ServerCmdDataParser||method=parseAndInitData||clusterPhyId={}||host={}||port={}||name={}||value={}||errMsg=exception!",
                        clusterPhyId, host, port, elem.getKey(), elem.getValue(), e
                );
            }
        });

        return serverCmdData;
    }
}
