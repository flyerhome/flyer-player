import axios from "axios";
import {logger} from "./Global.js";

const https = axios

function get(url, success, error, execute) {
    https.get(url)
        .then(function (response) {
            if (!!success) {
                success(response.data)
            }
        })
        .catch(function (e) {
            logger.error(e);
            if (!!error) {
                error(e)
            }
        })
        .finally(function () {
            // always executed
            if (!!execute) {
                execute()
            }
        });
}

async function aget(url, success, error, execute) {
    try {
        const response = await https.get(url);
        if (!!success) {
            success(response.data)
        }
    } catch (e) {
        logger.error(e);
        if (!!error) {
            error(e)
        }
    } finally {
        if (!!execute) {
            execute()
        }
    }
}

function post(url, data, success, error, execute) {
    https.post(url, data)
        .then(function (response) {
            if (!!success) {
                success(response.data)
            }
        })
        .catch(function (e) {
            logger.error(e);
            if (!!error) {
                error(e)
            }
        })
        .finally(function () {
            // always executed
            if (!!execute) {
                execute()
            }
        });
}


async function apost(url, data, success, error, execute) {
    try {
        const response = await https.post(url, data);
        if (!!success) {
            success(response.data)
        }
    } catch (e) {
        logger.error(e);
        if (!!error) {
            error(e)
        }
    } finally {
        if (!!execute) {
            execute()
        }
    }
}
export {get,post,aget,apost}