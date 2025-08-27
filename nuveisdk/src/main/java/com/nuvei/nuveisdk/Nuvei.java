package com.nuvei.nuveisdk;

public class Nuvei {
    private static String appCode;
    private static String appKey;
    private static String serverCode;
    private static String serverKey;
    private static boolean testMode =true;

    /**
     * Initializes the library environment with the provided configuration.
     * Codes and keys are provided by the nuvei team.
     *
     * @param appCode The application code.
     * @param appKey The application key.
     * @param serverCode The server code.
     * @param serverKey The server key.
     * @param testMode Indicates whether to use the test environment.
     */

    public static void initEnvironment(String appCode, String appKey, String serverCode, String serverKey, boolean testMode){
        Nuvei.appCode = appCode;
        Nuvei.appKey = appKey;
        Nuvei.serverCode = serverCode;
        Nuvei.serverKey = serverCode;
        Nuvei.testMode = testMode;
    }



    public static void listCards(){

    }



    public static boolean isTestMode(){
        return  Nuvei.testMode;
    }
}
