package com.week2gate2.fixtures;
import com.week2gate2.api.config.TestEnvConfig;
import com.week2gate2.api.config.BaseConfig;
import com.week2gate2.api.support.Specfactory;
import com.week2gate2.api.support.FetchToken;
public class credentials {
//    private static final String TOKEN_URL_CLIENT_ID = TestEnvConfig.configenv("OAUTH_CLIENT_ID",);
//    private static final String TOKEN_URL_CLIENT_SECRET = System.getenv("OAUTH_CLIENT_SECRET");
//    private static final String VIEWER_CLIENT_ID = System.getenv("OAUTH_VIEWER_CLIENT_ID");
//    private static final String VIEWER_CLIENT_SECRET = System.getenv("OAUTH_VIEWER_CLIENT_SECRET");
//    private static final String EXPIRED_CLIENT_ID = System.getenv("OAUTH_EXPIRED_CLIENT_ID");
//    private static final String EXPIRED_CLIENT_SECRET = System.getenv("OAUTH_EXPIRED_CLIENT_SECRET");
//    private static final String API_KEY = System.getenv("RETAIL_API_KEY");

    private static final String TOKEN_URL_CLIENT_ID = TestEnvConfig.configenv("OAUTH_CLIENT_ID","retail-ops-client");
    private static final String TOKEN_URL_CLIENT_SECRET = TestEnvConfig.configenv("OAUTH_CLIENT_SECRET", "ops-secret");
    private static final String VIEWER_CLIENT_ID = TestEnvConfig.configenv("OAUTH_VIEWER_CLIENT_ID", "retail-viewer-client");
    private static final String VIEWER_CLIENT_SECRET = TestEnvConfig.configenv("OAUTH_VIEWER_CLIENT_SECRET", "viewer-secret");
    private static final String EXPIRED_CLIENT_ID = TestEnvConfig.configenv("OAUTH_EXPIRED_CLIENT_ID", "retail-expired-client");
    private static final String EXPIRED_CLIENT_SECRET = TestEnvConfig.configenv("OAUTH_EXPIRED_CLIENT_SECRET", "expired-secret");
    private static final String API_KEY = TestEnvConfig.configenv("RETAIL_API_KEY", "retail-demo-key");
    public static final String client_token=FetchToken.gettoken(TOKEN_URL_CLIENT_ID,TOKEN_URL_CLIENT_SECRET);
    public static final String viewer_token=FetchToken.gettoken(VIEWER_CLIENT_ID,VIEWER_CLIENT_SECRET);
    public static final String expired_token=FetchToken.gettoken(EXPIRED_CLIENT_ID,EXPIRED_CLIENT_SECRET);


}
