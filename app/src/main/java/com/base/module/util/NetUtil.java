/**
 * *程序功能：网络工具类
 * *内部方法：
 * *编程人员：wells
 * *最后修改日期：2015年7月13日
 */
package com.base.module.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.regex.Pattern;


/**
 * @author wells
 *
 */
public class NetUtil {
    /**
     *
     * 检查是否网络连接
     * @param context
     * @return
     */
    public static boolean isNetWork(Context context) {
        boolean status = false;
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            status = mNetworkInfo.isAvailable();
        }
        return status;
    }

    /**
     * 是否wifi连接
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
        boolean status = false;
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            // 网线
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            // wifi
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                status = mWiFiNetworkInfo.isAvailable();
            } else if (mNetworkInfo != null) {
                status = mNetworkInfo.isAvailable();
            }
        }
        return status;
    }

    /**
     * @param @param  context
     * @param @return 参数
     * @return boolean 返回类型
     * @Title: isNetworkConnected
     * @Description: 检测网络是否可用
     * @author huangyc
     * @date 2014-10-16 下午12:03:27
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * @param @param  context
     * @param @return 参数
     * @return int 返回类型 0：没有网络 1：WIFI网络 2：MOBILE
     * @Title: getNetworkType
     * @Description: 获取当前网络类型
     * @author huangyc
     * @date 2014-10-16 下午12:02:59
     */
    public static int getNetworkType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return 0;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            return 2;
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            return 1;
        } else {
            return 0;
        }
    }

//    public static String getUniquNO(Context context){
//        try {
//            String key="__uino_ecp_";
//            String fileName="._unio_ecp_";
//            String uuid = SharedPreferencesHelper.getInstance().getString(key, null);
//            if (CommHelper.checkNull(uuid)) {
//                byte[] data = FileHelper.readFile(EnvironmentHelper.getSdcardDir().getAbsolutePath() + File.separator+fileName);
//                if (data==null){
//                    StringBuilder time=new StringBuilder();
//                    time.append(TimeHelper.getCurrentStamp(7));
//                    int len=11;
//                    byte[] buffer=new byte[len];
//                    for (int i=0;i<len;i++){
//                        byte a=(byte)((byte) ((Math.random()*255)+1) &0xff);
//                        buffer[i]=a;
//                    }
//                    time.append(CommHelper.hexEncode(buffer));
//                    FileHelper.bytes2File(EnvironmentHelper.getSdcardDir().getAbsolutePath(),fileName,time.toString().getBytes("utf-8"),false);
//                    SharedPreferencesHelper.getInstance().put(key,time.toString());
//                    return time.toString();
//                }else{
//                    uuid=new String(data,"utf-8");
//                    SharedPreferencesHelper.getInstance().put(key,uuid);
//                    return uuid;
//                }
//            } else {
//                FileHelper.bytes2File(EnvironmentHelper.getSdcardDir().getAbsolutePath(), fileName, uuid.getBytes("utf-8"),false);
//            }
//            return uuid;
//        }catch (Exception e){
//            e.printStackTrace();
//            return getMacAddress(context);
//        }
//    }

    /**
     * 获取无线mac地址
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        String mac = null;
//        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
//        return (null != info ? info.getMacAddress() : null);
        Enumeration<NetworkInterface> interfaceEnumeration = null;
        try {
            interfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            while ((interfaceEnumeration.hasMoreElements())) {
                NetworkInterface networkInterface = interfaceEnumeration.nextElement();
                String name = networkInterface.getName();
                if (!"wlan0".equals(name)) {
                    continue;
                }
                byte[] macAddress = networkInterface.getHardwareAddress();
                if (macAddress != null && macAddress.length > 0) {
                    mac = bytesToHexString(macAddress);
                    break;
                }

            }

            return mac;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    public static String bytesToHexString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {

            String hex = Integer.toHexString(bytes[i] & 0xff);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            builder.append(hex.toUpperCase());
        }
        return builder.toString();
    }


    /**
     * 获取无线IP地址
     * @param context
     * @return
     */
    public static String getIpAddress(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
        return (null != info ? int2ip(info.getIpAddress()) : null);
    }

    /**
     * 将long类型的ip地址，传成常见的192.168.0.1类型
     * @param ipInt
     * @return
     */
    public static String int2ip(long ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    /**
     * lisa add 2015-7-28.
     */
    /** 通过报文获取域名 */
    public static String getDnsName(String dnsMessage) {
        return dnsMessage;
    }

    /** 检查传入字段是否为规范的点分十进制的IP */
    public static boolean isValidIP(String addr) {
        if (CommHelper.checkNull(addr)) {
            return false;
        }
        Pattern pattern = Pattern
                .compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]"
                        + "|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$");
        return pattern.matcher(addr).matches();
    }

    /** DNS解析传入的域名字段，返回解析之后的IP地址 */
    public static String resolveDNS(String hostName) throws Exception {
        try {
            if (!CommHelper.checkNull(hostName)) {
                return InetAddress.getByName(hostName).getHostAddress();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public static String getHostName(String host) {
        try {
            if (!CommHelper.checkNull(host)) {
                return InetAddress.getByName(host).getHostName();
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    public static boolean compareIpAddress(String baseAddress, String secondAddress) {
        if (CommHelper.checkNull(baseAddress) || CommHelper.checkNull(secondAddress)) {
            return false;
        }
        // try {

        // 比较地址是否相同
        if (baseAddress.equalsIgnoreCase(secondAddress)) {
            return true;
        }
        // // 传入的如果是域名，则比较获取后ip是否相同
        // if (resolveDNS(baseAddress).equalsIgnoreCase(
        // resolveDNS(secondAddress))) {
        // return true;
        // }

        // } catch (Exception e) {
        // return false;
        // }
        return false;
    }

//	/** DNS解析传入的域名字段，返回解析以后的IP地址,默认返回第一个ip */
//	public static String dnsResolve(String domainName, String serverIp)throws Exception {
//		List<String> ips = searchIps(domainName, serverIp);
//		if (!ips.isEmpty()) {
//			return ips.get(0);
//		}
//		return "";
//	}

//	/** DNS解析传入的域名字段，返回解析以后的IP地址列表 */
//	public static List<String> searchIps(String domainName, String serverIp)throws Exception {
//		List<String> ips = new ArrayList<String>();
//		if ((!CommHelper.checkNull(serverIp) && isValidIP(serverIp))
//				&&!CommHelper.checkNull(domainName)) {
//			Lookup lookup = new Lookup(domainName, Type.A, DClass.IN);
//			lookup.setResolver(new SimpleResolver(serverIp));
//			lookup.run();
//			if (lookup.getResult() == Lookup.SUCCESSFUL) {
//				Record[] records = lookup.getAnswers();
//				for (Record record : records) {
//					try {
//						String ip = ((ARecord) record).getAddress()
//								.getHostAddress();
//						if ((CommHelper.checkNull(ip) && isValidIP(ip))) {
//							ips.add(ip);
//						}
//					} catch (Exception e) {
//						continue;
//					}
//				}
//
//			}
//		}
//		return ips;
//
//	}
}
