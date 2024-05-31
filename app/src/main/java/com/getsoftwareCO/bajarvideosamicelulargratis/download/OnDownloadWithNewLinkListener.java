/*
 * Copyright (c) 2024.  Andres Mora getSoftware CO
 */

package com.getsoftwareCO.bajarvideosamicelulargratis.download;

//interface created outside DownloadsInactive in a different file to avoid cyclic inheritance
public interface OnDownloadWithNewLinkListener {
    void onDownloadWithNewLink(DownloadVideo download);
}
