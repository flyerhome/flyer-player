import {message} from "ant-design-vue";

function serviceError(exec) {
    message.warn("服务出错啦，请稍后重试").then(r => {if (exec) exec(r)})
}

function noPermission(exec) {
    message.warn("权限不足，请联系管理员！").then(r => {if (exec) exec(r)})
}


const logger = {
    close:false,
    log : (...data)=> {
        if (!logger.close)
            window.console.log.apply(null,data)
    },
    debug : (...data)=> {
        if (!logger.close)
            window.console.debug.apply(null,data)
    },
    error : (...data)=> {
        if (!logger.close)
            window.console.error.apply(null,data)
    },
    warn : (...data)=> {
        if (!logger.close)
            window.console.warn.apply(null,data)
    },
    info : (...data)=> {
        if (!logger.close)
            window.console.info.apply(null,data)
    },
    trace : (...data)=> {
        if (!logger.close)
            window.console.trace.apply(null,data)
    }
}


export {
    serviceError,noPermission, logger
}