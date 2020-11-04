package dev.coc12.identityvtraitmanager;

public class Constants {
    final static String SERVICE_STOPPED_ACTION = "ServiceStopped";
    final static String URI_PACKAGE_FORMAT = "package:%s";

    final static int COUNTDOWN_INTERVAL = 1000;

    final static int[] TRAIT_ICONS = {
            R.drawable.ic_abnormal_icon,
            R.drawable.ic_excitement_icon,
            R.drawable.ic_patroller_icon,
            R.drawable.ic_teleport_icon,
            R.drawable.ic_blink_icon,
    };
    final static int[] TRAIT_OPENING_TIMES = {
            40,     // abnormal
            40,     // excitement
            30,     // patroller
            45,     // teleport
            60,     // blink
    };
    final static int[] TRAIT_COOL_TIMES = {
            90,     // abnormal
            100,    // excitement
            90,     // patroller
            100,    // teleport
            150,    // blink
    };
}