package com.sourceplusplus.marker.source.mark.inlay

import com.sourceplusplus.marker.source.mark.api.SourceMark
import com.sourceplusplus.marker.source.mark.inlay.config.InlayMarkConfiguration
import org.slf4j.LoggerFactory

/**
 * A [SourceMark] which adds visualizations inside source code text.
 *
 * @since 0.1.0
 * @author [Brandon Fergerson](mailto:bfergerson@apache.org)
 */
interface InlayMark : SourceMark {

    companion object {
        private val log = LoggerFactory.getLogger(InlayMark::class.java)
    }

    override val type: SourceMark.Type
        get() = SourceMark.Type.INLAY
    override val configuration: InlayMarkConfiguration
}
