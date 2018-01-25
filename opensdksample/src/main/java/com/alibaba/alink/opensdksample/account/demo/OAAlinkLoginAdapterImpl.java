package com.alibaba.alink.opensdksample.account.demo;

import android.content.Context;

import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.Environment;
import com.alibaba.sdk.android.openaccount.OauthService;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.OpenAccountService;
import com.alibaba.sdk.android.openaccount.OpenAccountSessionService;
import com.alibaba.sdk.android.openaccount.callback.InitResultCallback;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.callback.LogoutCallback;
import com.alibaba.sdk.android.openaccount.model.OpenAccountSession;
import com.alibaba.sdk.android.openaccount.model.Result;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService;
import com.alibaba.sdk.android.pluto.Pluto;
import com.aliyun.alink.business.login.IAlinkLoginAdaptor;
import com.aliyun.alink.business.login.IAlinkLoginCallback;

/**
 * class for demo
 * not useful
 */
public class OAAlinkLoginAdapterImpl implements IAlinkLoginAdaptor {

    IAlinkLoginCallback initCallback;

    @Override
    public String getSessionID() {
        OpenAccountSessionService sessionService = OpenAccountSDK.getService(OpenAccountSessionService.class);
        if (sessionService != null) {
            return sessionService.getSessionId().data;
        }
        return null;
    }

    @Override
    public String getUserID() {
        OpenAccountSession session = getSession();
        return session != null ? session.getUserId() : null;
    }

    @Override
    public String getNickName() {
        OpenAccountSession session = getSession();
        if (session == null || session.getUser() == null) {
            return null;
        }

        return session.getUser().nick;
    }

    @Override
    public String getAvatarUrl() {
        OpenAccountSession session = getSession();
        if (session == null || session.getUser() == null) {
            return null;
        }

        return session.getUser().avatarUrl;
    }

    @Override
    public boolean isLogin() {
        OpenAccountSession session = getSession();
        return session != null && session.isLogin();
    }

    @Override
    public void refreshSession(Context context, int refreshSessionType, final IAlinkLoginCallback callback) {
        OpenAccountSessionService sessionService = OpenAccountSDK.getService(OpenAccountSessionService.class);
        if (sessionService != null) {
            Result<String> result = sessionService.refreshSession(true);
            if (result.code == 100) {
                Result<String> sessionResult = sessionService.getSessionId();
                if (sessionResult.code == 100) {
                    if (callback != null) {
                        callback.onSuccess();
                    }
                    return;
                }
            }
        }

        if (refreshSessionType == MANUALLY_REFRESH_SESSION) {
            if (context == null) {
                return;
            }

            OpenAccountUIService openAccountUIService = OpenAccountSDK.getService(OpenAccountUIService.class);
            openAccountUIService.showLogin(context, new LoginCallback() {

                @Override
                public void onSuccess(OpenAccountSession session) {
                    if (callback != null) {
                        callback.onSuccess();
                    }
                }

                @Override
                public void onFailure(int code, String msg) {
                    if (callback != null) {
                        callback.onFailure(code, msg);
                    }
                }
            });
        }
    }

    @Override
    public String getAccountType() {
        return "OA";
    }

    private OpenAccountSession getSession() {
        OpenAccountService service = OpenAccountSDK.getService(OpenAccountService.class);
        if (service != null) {
            return service.getSession();
        }
        return null;
    }

    //必须在Application初始化中调用
    public void init(Context context, final Environment env, final IAlinkLoginCallback callback) {
        ConfigManager.getInstance().setEnvironment(env);
        if (env != Environment.ONLINE) {
            OpenAccountSDK.turnOnDebug();
        }
        OpenAccountSDK.asyncInit(context, new InitResultCallback() {

            @Override
            public void onSuccess() {
                callback.onSuccess();
                if (initCallback != null) {
                    initCallback.onSuccess();
                }
            }

            @Override
            public void onFailure(int code, String message) {
                callback.onFailure(code, message);
                if (initCallback != null) {
                    initCallback.onFailure(code, message);
                }
            }

        });
    }

    public void setInitResultCallback(IAlinkLoginCallback callback) {
        initCallback = callback;
    }

    @Override
    public void login(Context context, final IAlinkLoginCallback callback) {
        OpenAccountService service = OpenAccountSDK.getService(OpenAccountService.class);
        OpenAccountSession session = service.getSession();
        if (session.isLogin()) {
            if (callback != null) {
                callback.onSuccess();
            }
            return;
        }

        OpenAccountUIService openAccountUIService = OpenAccountSDK.getService(OpenAccountUIService.class);
        openAccountUIService.showLogin(context, new LoginCallback() {
            @Override
            public void onSuccess(OpenAccountSession session) {
                if (callback != null) {
                    callback.onSuccess();
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                if (callback != null) {
                    callback.onFailure(code, msg);
                }
            }
        });
    }

    @Override
    public void logout(Context context, final IAlinkLoginCallback callback) {
        OpenAccountService service = OpenAccountSDK.getService(OpenAccountService.class);
        service.logout(context, new LogoutCallback() {
            @Override
            public void onSuccess() {
                if (callback != null) {
                    callback.onSuccess();
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                OauthService oauthService = Pluto.DEFAULT_INSTANCE.getBean(OauthService.class);
                if (oauthService != null) {
                    oauthService.logout(null, new LogoutCallback() {
                        @Override
                        public void onSuccess() {
                            if (callback != null) {
                                callback.onSuccess();
                            }
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            if (callback != null) {
                                callback.onFailure(code, msg);
                            }
                        }
                    });
                } else {
                    if (callback != null) {
                        callback.onFailure(code, msg);
                    }
                }
            }
        });
    }
}