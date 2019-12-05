package com.blxt.clipboardmanager;


import com.blxt.clipboardmanager.bean.ClipTextBean;

public class Model {

    public static ClipTextBean clipboardStr_now = new ClipTextBean("");

    public static int ClipboardContentId = Model.CCID_DO_OTHER;
    public static final int CCID_DO_WEB = 0;
    public static final int CCID_DO_PHONE = 1;
    public static final int CCID_DO_OTHER = 2;

}
