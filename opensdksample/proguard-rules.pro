# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/zoro/Alibaba/Android/DevTools/adt-bundle-mac-x86_64-20140702/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


#通用
-keep class android.app.*$*{*;}
-keep class * extends android.app.Application{*;}
-keep class * extends android.app.Activity
-keep class * extends android.app.Service
-keep class * extends android.os.IInterface
-keep class * extends android.content.BroadcastReceiver
-keep class * extends android.content.ContentProvider
-keep class * extends android.appwidget.AppWidgetProvider
-keep class * extends android.support.v4.app.Fragment{*;}
-keep class * extends android.webkit.**{*;}
-keep public class android.webkit.*
-keep class * extends android.widget.**{*;}
-keep class * extends android.view.View{*;}
-keep class * implements android.os.Handler.Callback{*;}
-keep class * implements android.os.IBinder{*;}
-keep class * implements java.io.Serializable
-keep class * implements Handler.Callback{*;}
-keep interface * extends android.os.IInterface{*;}
-keep interface **{*;}
-keep class org.json.**{*;}

#accs
-keep class com.taobao.accs.utl.UtilityImpl{
    private <init>(...);
    public <init>(...);
    <init>(...);}
-keep class com.taobao.accs.internal.ACCSManagerImpl{
    private <init>(...);
    public <init>(...);
    <init>(...);}
-keep class com.taobao.accs.internal.ServiceImpl{
    private <init>(...);
    public <init>(...);
    <init>(...);}
-keep class com.taobao.accs.internal.ReceiverImpl{
    private <init>(...);
    public <init>(...);
    <init>(...);}
-keep class com.taobao.accs.ChannelService{*;}
-keep class com.taobao.accs.antibrush.CheckCodeDO{*;}
-keep class com.taobao.accs.flowcontrol.FlowControl$FlowControlInfo{*;}
-keep class com.taobao.accs.flowcontrol.FlowControl$FlowCtrlInfoHolder{*;}
-keep class com.taobao.accs.ACCSManager{*;}
-keep class com.taobao.accs.ACCSManager$*{*;}
-keep class com.taobao.accs.ErrorCode{*;}
-keep class com.taobao.accs.IAliyunAppReceiver{*;}
-keep class com.taobao.accs.IAppReceiver{*;}
-keep class com.taobao.accs.IAppReceiverV1{*;}
-keep class com.taobao.accs.ILoginInfo{*;}
-keep class com.taobao.accs.IServiceReceiver{*;}
-keep class com.taobao.accs.client.AccsConfig{*;}
-keep class com.taobao.accs.utl.ALog{*;}
-keep class com.taobao.accs.base.**{*;}
-keep class com.taobao.accs.base.**$*{*;}
-keep class com.taobao.accs.common.Constants{*;}
-keep class com.taobao.accs.ErrorCode{*;}
-keep class com.taobao.accs.client.GlobalClientInfo{
    public <methods>;
}
-keep class com.taobao.accs.utl.UTMini{*;}
-keep class com.taobao.accs.antibrush.AntiBrush{*;}

#xiaomi
-keep class com.xiaomi.**{*;}

#huawei
-keep class com.huawei.android.pushselfshow.**{*;}
-keep class com.huawei.android.pushagent.**{*;}

# open sdk
#-------------------
# 打包时忽略以下类的警告
#-------------------
-dontwarn java.awt.**
-dontwarn android.test.**

# Java
-keep class * implements java.io.Serializable{*;}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
    **[] $VALUES;
}

# ALinkApp Buiness
## ALinkBusiness
-keep class com.aliyun.alink.business.alink.ALinkConstant{*;}
-keep enum com.aliyun.alink.business.alink.ALinkEnv{*;}
-keep class com.aliyun.alink.business.alink.ALinkConfigure{*;}
-keep class com.aliyun.alink.business.alink.ALinkBusiness{
    public <fields>;
    public <methods>;
}
-keep class com.aliyun.alink.business.alink.ALinkBusinessEx{*;}
-keep public interface com.aliyun.alink.business.alink.ALinkBusiness$IListener{*;}
-keep public interface com.aliyun.alink.business.alink.ALinkBusinessEx$IListener{*;}
-keep class com.aliyun.alink.business.alink.ALinkRequest{
    public <methods>;
}
-keep class com.aliyun.alink.business.alink.ALinkResponse{*;}
-keep class com.aliyun.alink.business.alink.ALinkResponse$Result{*;}
-keep class com.aliyun.alink.business.alink.IWSFConnectWrapper{*;}
-keep interface com.aliyun.alink.business.alink.IRequestWatcher{*;}
-keep class com.aliyun.alink.business.alink.DefaultGlobalRequestWatcher{*;}

## LoginBusiness
-keep class com.aliyun.alink.business.login.IAlinkLoginAdaptor{*;}
-keep class com.aliyun.alink.business.login.AlinkLoginBusiness{*;}
-keep class com.aliyun.alink.business.login.IAlinkLoginCallback{*;}
-keep class com.aliyun.alink.business.login.IAlinkLoginStatusListener{*;}

# ALinkApp SDK
#-keep class com.aliyun.alink.sdk.net.anet.api.INet{*;}
#-keep class com.aliyun.alink.sdk.net.anet.api.AConnect{*;}
#-keep class com.aliyun.alink.sdk.net.anet.PersistentNet.AConnectResponseRunnable{
#    public <fields>;
#    public <methods>;
#}

##helper
-keep class com.aliyun.alink.business.helper.AlinkSenderHelper{*;}
-keep class com.aliyun.alink.business.helper.ChannelBindHelper{
    public <methods>;
}

##account
-keep class com.aliyun.alink.business.account.OALoginBusiness{*;}
-keep class com.aliyun.alink.business.account.TaobaoLoginBusiness{*;}

##downstream
-keep class com.aliyun.alink.business.downstream.DeviceBusiness{*;}
-keep class com.aliyun.alink.business.downstream.DownStreamBusiness{*;}
-keep class com.aliyun.alink.business.downstream.IDownstreamCommandListener{*;}
-keep class com.aliyun.alink.business.downstream.DeviceData{*;}

##anet
-keep class com.aliyun.alink.sdk.net.anet.api.persistentnet.PersistentRequest{*;}
-keep class com.aliyun.alink.sdk.net.anet.api.persistentnet.IOnPushListener{*;}
-keep class com.aliyun.alink.sdk.net.anet.api.persistentnet.EventDispatcher{*;}
-keep class com.aliyun.alink.sdk.net.anet.api.persistentnet.INetSessionStateListener{*;}
-keep class com.aliyun.alink.sdk.net.anet.api.persistentnet.IConnectionStateListener{*;}
-keep class com.aliyun.alink.sdk.net.anet.api.transitorynet.TransitoryRequest{*;}
-keep class com.aliyun.alink.sdk.net.anet.api.transitorynet.TransitoryResponse{*;}

-keep class com.aliyun.alink.sdk.net.anet.api.AConnect{*;}
-keep class com.aliyun.alink.sdk.net.anet.api.AError{*;}
-keep class com.aliyun.alink.sdk.net.anet.api.ARequest{*;}
-keep class com.aliyun.alink.sdk.net.anet.api.AResponse{*;}
-keep class com.aliyun.alink.sdk.net.anet.api.IOnCallListener{*;}
-keep class com.aliyun.alink.sdk.net.anet.api.INet{*;}


## WSFNet
-keep class com.aliyun.alink.sdk.net.anet.wsf.WSFConfigure{*;}
-keep class com.aliyun.alink.sdk.net.anet.wsf.WSFNet{
    public <methods>;
}
-keep interface com.aliyun.alink.sdk.net.anet.wsf.IWSFNetDownstreamCommandListener{*;}
-keep class com.aliyun.alink.sdk.net.anet.wsf.IWSFNetDownstreamCommandListener{*;}

# ALinkApp Utils
-keep class com.aliyun.alink.tool.ALog{
    public <fields>;
    public <methods>;
}
-keep class com.aliyun.alink.tool.ThreadTools{
    public <fields>;
    public <methods>;
}
-keep class com.aliyun.alink.tool.NetTools{
    public <fields>;
    public <methods>;
}

# SDK Entry
-keep class com.aliyun.alink.LinkSDK{
    public <fields>;
    public <methods>;
}

# Mtop
-keep class com.aliyun.alink.business.mtop.MTopBusiness{*;}
-keep interface com.aliyun.alink.business.mtop.MTopBusiness$IListener{*;}
-keep interface com.aliyun.alink.business.mtop.IMTopRequest{*;}
-keep class com.aliyun.alink.business.mtop.MTopResponse{*;}

-keep public class * implements mtopsdk.mtop.domain.IMTOPDataObject {*;}
-keep public class mtopsdk.mtop.domain.MtopResponse
-keep public class mtopsdk.mtop.domain.MtopRequest
-keep class mtopsdk.mtop.domain.**{*;}
-keep class mtopsdk.common.util.**{*;}
-keep class com.taobao.tao.connectorhelper.*
-keep public class org.android.spdy.**{*; }
-keep class mtop.sys.newDeviceId.Request{*;}

#push
-keepclasseswithmembernames class ** {
    native <methods>;
}
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.taobao.** {*;}
-keep class com.alibaba.** {*;}
-keep class com.alipay.** {*;}
-keep class com.ut.** {*;}
-keep class com.ta.** {*;}
-keep class anet.**{*;}
-keep class anetwork.**{*;}
-keep class org.android.spdy.**{*;}
-keep class org.android.agoo.**{*;}
-keep class android.os.**{*;}
-dontwarn com.taobao.**
-dontwarn com.alibaba.**
-dontwarn com.alipay.**
-dontwarn anet.**
-dontwarn org.android.spdy.**
-dontwarn org.android.agoo.**
-dontwarn anetwork.**
-dontwarn com.ut.**
-dontwarn com.ta.**

# account
-dontwarn  com.aliyun.alink.linksdk.*
-dontwarn  com.aliyun.alink.business.account.**

# device center
-dontwarn com.aliyun.alink.business.devicecenter.**
-keep class com.aliyun.alink.business.devicecenter.**{*;}

# if not use BoneKit, please add follow config
-dontwarn com.aliyun.alink.open.a
-dontwarn com.aliyun.alink.open.f
-dontwarn com.aliyun.alink.utils.PackageConfigHelper
-dontwarn com.aliyun.alink.utils.PackageConfigHelper$a
-dontwarn com.aliyun.alink.utils.PackageConfigHelper$b
-dontwarn com.aliyun.alink.AlinkSDK
-dontwarn com.aliyun.alink.open.a
-dontwarn com.aliyun.alink.open.f
-dontwarn com.aliyun.alink.page.pageroutor.ARouterUtil
-dontwarn com.aliyun.alink.page.rn.RNActivity

# for React Native @ Start

-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient
-dontwarn android.util.FloatMath
-dontwarn okio.**
-dontwarn com.facebook.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.internal.huc.DelegatingHttpsURLConnection
-dontwarn okhttp3.internal.huc.OkHttpsURLConnection

-keep class com.facebook.**{*;}

# keep native module
-keep  class * implements com.facebook.react.bridge.NativeModule {
    public <methods>;
    protected <methods>;
}

# keep view manager
-keep  class * extends com.facebook.react.uimanager.ViewManager {
    public <methods>;
    protected <methods>;
}

# keep js module
-keep  class * extends com.facebook.react.bridge.JavaScriptModule {
    public <methods>;
}

# keep shadow node
-keep class * extends com.facebook.react.uimanager.ReactShadowNode{
    public <methods>;
    protected <methods>;
}

# for React Native @ End

# for BoneBridge @ Start

# keep bone api
-keep  class * extends com.aliyun.alink.sdk.jsbridge.methodexport.BaseBonePlugin {
     @com.aliyun.alink.sdk.jsbridge.methodexport.MethodExported <methods>;
     public <fields>;
}

# for BoneBridge @ End

# for offline package
-keep class com.aliyun.alink.business.acache.ocache.OCacheConfig{*;}
-keep class com.aliyun.alink.business.acache.ocache.OCacheConfig$Package{*;}
-keep class com.aliyun.alink.business.acache.ocache.OCacheEntry{*;}
-keep class com.aliyun.alink.business.acache.ocache.OCacheEntry$OCacheEntity{*;}
-keep class com.aliyun.alink.business.acache.ocache.OCacheEntry$OCacheFolderEntity{*;}
-keep class com.aliyun.alink.business.acache.ocache.OCacheEntry$OCacheZipEntity{*;}
-keep class com.aliyun.alink.business.acache.ocache.Result{*;}
-keep class com.aliyun.alink.business.acache.AOfflinePackage{*;}

#arouter混淆配置
-keep class com.aliyun.alink.page.pageroutor.** { *; }
-keep class mtopclass.mtop.openalink.alinkapp.dsir.get.routers.**{*;}

#openAccount
-keepattributes Signature

-keep class sun.misc.Unsafe { *; }

-keep class com.taobao.** {*;}

-keep class com.alibaba.** {*;}

-keep class com.alipay.** {*;}

-dontwarn com.taobao.**

-dontwarn com.alibaba.**

-dontwarn com.alipay.**

-keep class com.ut.** {*;}

-dontwarn com.ut.**

-keep class com.ta.** {*;}

-dontwarn com.ta.**