package in.therenter.app.main;

import android.content.Context;
import android.webkit.WebChromeClient;

public class CustomWebChromeClient extends WebChromeClient{

    private Context context;

    public CustomWebChromeClient(Context context) {
        this.context = context;
    }
}
