package com.xwp.jt809.wzjtj.tools;

import java.io.UnsupportedEncodingException;
import java.util.List;

import redis.clients.jedis.ShardedJedis;

import com.xwp.jt809.wzjtj.model.Apply;
import com.xwp.jt809.wzjtj.model.DriverMsg;
import com.xwp.jt809.wzjtj.model.EwayBill;
import com.xwp.jt809.wzjtj.model.ExgMsg;
import com.xwp.jt809.wzjtj.model.VehicleLocation;
import com.xwp.jt809.wzjtj.model.VehicleLocations;
import com.xwp.jt809.wzjtj.model.VehicleRegisterMSG;

public class DataExplain {

	byte[] plaNoBytes = new byte[21];
	byte[] dataLengthByte = new byte[4];
	int dataLength;
	byte[] megId = new byte[2];
	String plaNo;
	Byte plaColor;
	byte[] dataTypeBytes = new byte[2];
	int dataType;
	ExgMsg exg;
	MyJedisPool myJedisPool = new MyJedisPool();
	ShardedJedis redis;

	public void explainEXG(List<Byte> blist) throws UnsupportedEncodingException {

		exg = new ExgMsg(blist);
		dataType = exg.getDataType();
		blist = blist.subList(28,blist.size());
		
		//子业务类型
		if(dataType == TabCode.UP_EXG_MSG_REGISTER){			//车辆注册信息
			VehicleRegisterMSG vregister = new VehicleRegisterMSG(blist,exg);
			System.out.println("接收到车辆注册信息："+vregister.getExg().getVehivleNo());
		}else if(dataType == TabCode.UP_EXG_MSG_REAL_LOCATON){	//车辆定位信息
			VehicleLocation vlocation = new VehicleLocation(blist,exg);
			redis = myJedisPool.getJedis();
			redis.lpush("gps", JsonUtil.objectToJson(vlocation));
			myJedisPool.returnJedis(redis);
			System.out.println("接收到车辆定位信息："+vlocation.getExg().getVehivleNo()+"("+vlocation.getLongi()+","+vlocation.getLati()+")");
		}else if(dataType == TabCode.UP_EXG_MSG_HISTORY_LOCATION){
			VehicleLocations vlocations = new VehicleLocations(blist,exg);
			System.out.println("接收到一大批历史遗留车辆定位信息。数量:"+vlocations.getGnssCnt());
		}else if(dataType == TabCode.UP_EXG_MSG_RETURN_STARTUP_ACK){
			//启动车辆定位信息交换应答消息
			System.out.println("启动车辆定位信息交换应答消息的应答");
		}else if(dataType == TabCode.UP_EXG_MSG_RETURN_END_ACK){
			//结束车辆定位信息交换应答消息
			System.out.println("停止车辆定位信息交换应答消息的应答");
		}else if(dataType == TabCode.UP_EXG_MSG_APPLY_FOR_MINITOR_STARTUP){
			Apply apply = new Apply(blist,exg);
			System.out.println("申请交换指定车辆定位信息请求消息:"+apply.getExg().getVehivleNo());
		}else if(dataType == TabCode.UP_EXG_MSG_APPLY_FOR_MINITOR_END){
			//取消交换指定车辆定位信息请求消息
			System.out.println("取消交换指定车辆定位信息请求消息");
		}else if(dataType == TabCode.UP_EXG_MSG_APPLY_HISGNSSDATA_REQ){
			//补发车辆定位信息请求消息
			Apply apply = new Apply(blist,exg);
			System.out.println("申请补发交换指定车辆定位信息请求消息:"+apply.getExg().getVehivleNo());
		}else if(dataType == TabCode.UP_EXG_MSG_REPORT_DRIVER_INFO_ACK){
			DriverMsg driver = new DriverMsg(blist,exg);
			System.out.println("接收到上报驾驶员身份识别信息应答:"+driver.getDriverName());
		}else if(dataType == TabCode.UP_EXG_MSG_TAKE_EWAYBILL_ACK){
			EwayBill eway = new EwayBill(blist,exg);
			System.out.println("接收到电子路单："+eway.getEwaybillInfo());
		}
		
//		for(int i=23;i<27;i++){
//			dataLengthByte[i-23] = blist.get(i);
//		}
//		dataLength = ChangeType.bytesTo4Int(dataLengthByte, 0);
	}

	public void explainPLATFORM(List<Byte> blist) {

		for(int i=0;i<21;i++){
			plaNoBytes[i] = blist.get(i);
		}
		plaNo = new String(plaNoBytes);
		plaColor = blist.get(21);
		dataTypeBytes[0] = blist.get(21);
		dataTypeBytes[1] = blist.get(22);
		dataType = ChangeType.bytesTo2Int(dataTypeBytes, 0);
		//子业务类型
		if(dataType == TabCode.UP_PLATFORM_MSG_POST_QUERY_ACK){
			
		}
		
		for(int i=23;i<27;i++){
			dataLengthByte[i-23] = blist.get(i);
		}
		dataLength = ChangeType.bytesTo4Int(dataLengthByte, 0);
	}

	public void explainWARN(List<Byte> blist) {

		for(int i=0;i<21;i++){
			plaNoBytes[i] = blist.get(i);
		}
		plaNo = new String(plaNoBytes);
		plaColor = blist.get(21);
		dataTypeBytes[0] = blist.get(21);
		dataTypeBytes[1] = blist.get(22);
		dataType = ChangeType.bytesTo2Int(dataTypeBytes, 0);
		//子业务类型
		if(dataType == TabCode.UP_WARN_MSG_URGE_TODO_ACK){
			
		}else if(dataType == TabCode.UP_WARN_MSG_ADPT_INFO){
			
		}
		
		for(int i=23;i<27;i++){
			dataLengthByte[i-23] = blist.get(i);
		}
		dataLength = ChangeType.bytesTo4Int(dataLengthByte, 0);
	}
	
	public void explainCTRL(List<Byte> blist) {

		for(int i=0;i<21;i++){
			plaNoBytes[i] = blist.get(i);
		}
		plaNo = new String(plaNoBytes);
		plaColor = blist.get(21);
		dataTypeBytes[0] = blist.get(21);
		dataTypeBytes[1] = blist.get(22);
		dataType = ChangeType.bytesTo2Int(dataTypeBytes, 0);
		//子业务类型
		if(dataType == TabCode.UP_CTRL_MSG_MONITOR_VEHICLE_ACK){
			
		}else if(dataType == TabCode.UP_CTRL_MSG_TAKE_PHONE_ACK){
			
		}else if(dataType == TabCode.UP_CTRL_MSG_TEXT_INFO_ACK){
			
		}else if(dataType == TabCode.UP_CTRL_MSG_TAKE_TRAVE_ACK){
			
		}else if(dataType == TabCode.UP_CTRL_MSG_EMERGENCY_MONITORING_ACK){
			
		}
		
		for(int i=23;i<27;i++){
			dataLengthByte[i-23] = blist.get(i);
		}
		dataLength = ChangeType.bytesTo4Int(dataLengthByte, 0);
	}
	
	public void explainBASE(List<Byte> blist) {

		for(int i=0;i<21;i++){
			plaNoBytes[i] = blist.get(i);
		}
		plaNo = new String(plaNoBytes);
		plaColor = blist.get(21);
		dataTypeBytes[0] = blist.get(21);
		dataTypeBytes[1] = blist.get(22);
		dataType = ChangeType.bytesTo2Int(dataTypeBytes, 0);
		//子业务类型
		if(dataType == TabCode.UP_BASE_MSG_VEHICLE_ADDED_ACK){
			
		}
		
		for(int i=23;i<27;i++){
			dataLengthByte[i-23] = blist.get(i);
		}
		dataLength = ChangeType.bytesTo4Int(dataLengthByte, 0);
	}
}
