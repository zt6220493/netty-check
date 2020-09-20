package com.ecar.eoc.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecar.eoc.config.CollectionConfig;
import com.ecar.eoc.entity.vo.CmdInfo;

public class CmdUtil {

	private static Map<String, CmdInfo> cmdIdSpringMap = null;

	private static void inntSpringCMDIDMap() {

		synchronized (CmdUtil.class) {
			if (null != cmdIdSpringMap) {
				return;
			}
			
			List<Map<String,String>> list = SpringContextUtils.getBean(CollectionConfig.class).getGpsCmd();
			
			cmdIdSpringMap = new HashMap<String, CmdInfo>(list.size());
			for (int i = 0; i < list.size(); i++) {
				Map<String,String> cmdMap = list.get(i);
				String cmdId = cmdMap.get("cmdId");
				String springBeanName = cmdMap.get("rf-springBeanName");
				String cmdName = cmdMap.get("cmdName");

				cmdIdSpringMap.put(cmdId, new CmdInfo(springBeanName, cmdName));
			}
		}
	}

	public static String getSpringBeanName(String cmdId) {

		CmdInfo info = getCmdInfo(cmdId);
		return null == info ? null : info.getSpringBeanName();
	}

	public static CmdInfo getCmdInfo(String cmdId) {
		if (null == cmdIdSpringMap) {
			inntSpringCMDIDMap();
		}
		CmdInfo info = cmdIdSpringMap.get(cmdId);
		return info;
	}

	/**
	 * @Title: getMessage
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param cmdId
	 * @return
	 * @author zlf
	 * @date 2015-1-4 下午04:40:43
	 * @version V1.0
	 */
	public static String getMessage(String cmdId) {
		CmdInfo info = getCmdInfo(cmdId);
		return null == info ? "未知" : info.getCmdName();
	}
	
	public static void main(String[] args) {
		System.out.println(getCmdInfo("410"));
	}
}
