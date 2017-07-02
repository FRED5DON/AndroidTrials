/**
 * Created by fred on 2017/6/7.
 * ===========       Cotjs 1.0 made by fred           ===========
 * This a solution of using html and js on local filesystem,
 * which create a imagination that looks like js can handle request without limits
 * of Allow Original Request.You did know,that's impossible or js can rule the world.
 */
(function (jsHandler) {
    'use strict';

    var jsHandlerCall;

    if (window.hasOwnProperty(jsHandler)) {
        jsHandlerCall = window[jsHandler];
    }

    var CotJSConfig = {
        allowMethods: ['GET', 'PUT', 'POST', 'DELETE', 'HEAD', 'OPTIONS', 'UPDATE'],
        debug: true,
        config: {
            //requestUrl
            url: '',
            //request Method
            method: 'GET',
            //it marks data's construction
            json: true,
            //As default ,data is a json object
            data: {},
            //Timeout period ,unit : seconds
            timeout: 30,
            cookies: null,
            //request Headers
            header: {},
            //this should be set a special flag to mark this request is unique in a long period
            what: 0
        },
    };

    function CotJS(config) {
        this.stack = [];
        this.before = null;
        this.after = null;
        this.config = CotJSConfig.extendProperty({}, CotJSConfig.config);
        this.config = CotJSConfig.extendProperty(this.config, config);
        this.asyncEvent = function (config) {
            if (typeof jsHandlerCall === 'object') {
                var argus = config;
                setTimeout(function () {
                    CotJS.log(">>> asyncEvent");
                    if (typeof jsHandlerCall.async === 'function') {
                        jsHandlerCall.async(JSON.stringify(argus));
                    }
                }, 0);
            }
        };
        this.syncEvent = function (config) {
            CotJS.log(">>> syncEvent");
            if (typeof jsHandlerCall === 'object') {
                if (typeof jsHandlerCall.sync === 'function') {
                    return jsHandlerCall.sync(JSON.stringify(config));
                }
            }
        };
        this.$callback = function (callback) {
            if (typeof callback === 'function') {
                CotJS.response.callback = callback;
            }
            return this;
        };

        this.$request = function (config, isSync, syncCallBack) {
            if (config) {
                CotJSConfig.extendProperty(this.config, config);
            }
            if (isSync && typeof syncCallBack === 'function') {
                syncCallBack(this.syncEvent(this.config));
            } else {
                this.asyncEvent(this.config);
            }
            return this;
        };

        this.$next = function (func, callback) {
            this.stack.push(func);
            this.stack.push(callback);
            return this;
        };
    }

    CotJS.response = {
        callback: function (what, data, message) {
            var that = this;
            if (what === that.config.what) {
                if (that.stack.length > 0) {
                    var func = that.stack.pop();
                    var callback = that.stack.pop();
                    that.$callback(callback);
                    if (typeof func === 'function') {
                        func(what, data, message);
                    }
                }
            }
        }
    }

    /**
     * Properties Extension of Object
     * @param $this
     * @param extCopy
     * @returns {*}
     */
    CotJSConfig.extendProperty = function ($this, extCopy) {
        var extension = {};
        for (var s in extCopy) {
            extension[s] = extCopy[s];
        }
        if (typeof extension === 'object') {
            for (var k in $this) {
                if (extension.hasOwnProperty(k)) {
                    if (typeof extension[k] === typeof $this[k]) {
                        if (typeof extension[k] === 'object') {
                            $this[k].extend(extension[k]);
                        } else {
                            $this[k] = extension[k];
                            delete extension[k];
                        }
                    }
                }
            }
            for (var d in extension) {
                if (!$this.hasOwnProperty(d)) {
                    $this[d] = extension[d];
                }
            }
        }
        return $this;
    };


    CotJSConfig.log = function () {
        if (CotJSConfig.debug) {
            console.log(arguments[0], arguments[1]);
        }
    };


    CotJS.log = CotJSConfig.log;


    if (CotJS) {
        window.CotJS = CotJS;
    }


    window.CotJSWEBHandler = CotJS.response;

})('JSHandler');