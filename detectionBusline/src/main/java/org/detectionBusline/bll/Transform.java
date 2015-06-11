package org.detectionBusline.bll;

import java.text.DecimalFormat;

import org.detectionBusline.model.LngLat;
import org.detectionBusline.model.XY;


public class Transform {
	
	private static int projNo = 0, ZoneWide = 6; //6度带宽
	private static double iPI = 0.0174532925199433; //3.1415926535898/180.0;
	private static double a = 6378245.0, f = 1.0 / 298.3; //54年北京坐标系参数
//    private static double a = 6378140.0, f = 1/298.257; //80年西安坐标系参数
	private static double e2 = 2*f - f*f;

    public static XY transToXY(LngLat latLng) {
        double lng1,lat1, lng0, X0,Y0, xval,yval;
        double ee, NN, T,C,A, M;
       
        projNo = (int) (latLng.getLng() / ZoneWide);
        lng0 = (projNo * ZoneWide + ZoneWide / 2) * iPI; 
        lng1 = latLng.getLng() * iPI ; //经度转换为弧度
        lat1 = latLng.getLat() * iPI ; //纬度转换为弧度
       
        ee = e2 * (1.0 - e2);
        NN = a / Math.sqrt(1.0 - e2 * Math.pow(Math.sin(lat1), 2));
        T = Math.pow(Math.tan(lat1), 2);
        C = ee * Math.pow(Math.cos(lat1), 2);
        A = (lng1 - lng0) * Math.cos(lat1);
        M = a*((1-e2/4-3*e2*e2/64-5*e2*e2*e2/256)*lat1-(3*e2/8+3*e2*e2/32+45*e2*e2*e2/1024)*Math.sin(2*lat1)
                +(15*e2*e2/256+45*e2*e2*e2/1024)*Math.sin(4*lat1)-(35*e2*e2*e2/3072)*Math.sin(6*lat1));
       
        xval = NN*(A+(1-T+C)*A*A*A/6+(5-18*T+T*T+72*C-58*ee)*A*A*A*A*A/120);
        yval = M+NN*Math.tan(lat1)*(A*A/2+(5-T+9*C+4*C*C)*A*A*A*A/24+(61-58*T+T*T+600*C-330*ee)*A*A*A*A*A*A/720);
        X0 = 1000000L*(projNo+1)+500000L;
        Y0 = 0;
        xval = xval + X0; 
        yval = yval + Y0;
        
        // 保留6位小数
        DecimalFormat df = new DecimalFormat("###.######");
        
        // 去除x坐标前的2位带号
        String xvalStr = df.format(xval);
        xval = Double.parseDouble(xvalStr.substring(2, xvalStr.length()));
        yval = Double.parseDouble(df.format(yval));
//        System.out.println("(" + latLng.getLng() + ", " + latLng.getLat() +"): (" + xval + ", " + yval + ")");
        return new XY(xval, yval);
    }
   
    public static LngLat transToLL(XY xy) {
        double lng1,lat1, lng0, X0,Y0, xval,yval;
        double e1, ee, NN, T,C, M, D,R,u,fai;
       
        projNo = (int)(xy.getX()/1000000L) ; //查找带号
        lng0 = (projNo-1) * ZoneWide + ZoneWide / 2;
        lng0 = lng0 * iPI ; //中央经线
        X0 = projNo*1000000L + 500000L;
        Y0 = 0;
        xval = xy.getX()-X0;
        yval = xy.getY()-Y0; //带内大地坐标
       
        e1 = (1.0-Math.sqrt(1-e2))/(1.0+Math.sqrt(1-e2));
        ee = e2/(1-e2);
        M = yval;
        u = M/(a*(1-e2/4-3*e2*e2/64-5*e2*e2*e2/256));
        fai = u+(3*e1/2-27*e1*e1*e1/32)*Math.sin(2*u)+(21*e1*e1/16-55*e1*e1*e1*e1/32)*Math.sin(4*u)
                +(151*e1*e1*e1/96)*Math.sin(6*u)+(1097*e1*e1*e1*e1/512)*Math.sin(8*u);
        C = ee*Math.pow(Math.cos(fai), 2);
        T = Math.pow(Math.tan(fai), 2);
        NN = a/Math.sqrt(1.0-e2*Math.pow(Math.sin(fai), 2));
        R = a*(1-e2)/Math.pow((1.0-e2*Math.pow(Math.sin(fai), 2)), 3/2);
        D = xval/NN;
       
        //计算经度(Longitude) 纬度(Latitude)
        lng1 = lng0+(D-(1+2*T+C)*D*D*D/6+(5-2*C+28*T-3*C*C+8*ee+24*T*T)*D*D*D*D*D/120)/Math.cos(fai);
        lat1 = fai -(NN*Math.tan(fai)/R)*(D*D/2-(5+3*T+10*C-4*C*C-9*ee)*D*D*D*D/24+(61+90*T+298*C+45*T*T-256*ee-3*C*C)*D*D*D*D*D*D/720);
        return new LngLat(lat1/iPI, lng1/iPI);
    }
}
