package org.monora.uprotocol.client.android.service.web

import android.content.Context
import com.yanzhenjie.andserver.annotation.Config
import com.yanzhenjie.andserver.framework.config.WebConfig
import com.yanzhenjie.andserver.framework.website.AssetsWebsite

@Config
class WebShareConfig : WebConfig {
    override fun onConfig(context: Context, delegate: WebConfig.Delegate) {
        delegate.addWebsite(AssetsWebsite(context, "/web/static/"))
    }
}
