package org.droba.mutantSpringloadedSupport

import kotlinx.html.FlowContent
import kotlinx.html.script
import kotlinx.html.unsafe

fun FlowContent.reloadScript() = script {
    unsafe {
        //language=JavaScript
        +"""
                    var exampleSocket = new WebSocket(url("/mutantws"));
                    exampleSocket.onmessage = function(e) {

                        console.log(e);

                        if (e.data === 'MutantWS Reload')
                            window.location.reload(true);

                    };

                    function url(s) {
                        var l = window.location;
                        return ((l.protocol === "https:") ? "wss://" : "ws://") + l.host + s;
                    }
                """
    }
}
