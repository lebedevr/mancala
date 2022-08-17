import * as SockJS from "sockjs-client";
import * as Stomp from "stomp-websocket";

import { backendHost } from './Config';

const socket = new SockJS(backendHost + '/mancala-websocket');
const stompClient = Stomp.over(socket);

const callStomp = (func) => {
    if (stompClient.connected) {
        func()
    } else {
        stompClient.connect({}, function () {
            func()
        });
    }
};
export {callStomp};
export default stompClient;