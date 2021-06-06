import React, { useEffect, useState } from 'react';
import { BellOutlined, LogoutOutlined, MessageOutlined } from '@ant-design/icons';
import '../assets/css/naslovna.css'
import { useHistory } from 'react-router';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import { notification } from 'antd';


const SERVER_URL = 'http://localhost:8083/ws';
let stompClient;


const HeaderNaslovna = (props) => {
    let history = useHistory();
    const [bellColor, setBellColor] = useState({ color: "grey" });

    const logout = () => {
        localStorage.setItem("token", "");
        localStorage.setItem("uloga", "");
        localStorage.setItem("id", -1);
        history.push("/")
    }

    const connectToWebSocket = () => {
        connect();
    };

    const disconnectFromWebSocket = () => {
        disconnect();
    };
    function connect() {
        var socket = new SockJS(SERVER_URL);
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);
    }

    const onConnected = (frame) => {
        console.log('Connected: ' + frame);
        stompClient.subscribe("/user/" + localStorage.getItem("id") + "/queue/notifikacije", onNotificationReceived);
        if (props.zaobidjiChatNotifikacije !== true)
            stompClient.subscribe("/user/" + localStorage.getItem("id") + "/queue/chat-notifikacije", onNotificationReceived);
    };

    const onError = (err) => {
        console.log(err);
    };

    const onNotificationReceived = (notificationRes) => {
        let recievedNotification = JSON.parse(notificationRes.body);
        if (!recievedNotification.naslov.includes("Obavještenje: Termin otkazan"))
            notification.open({
                message: recievedNotification.naslov,
                description: recievedNotification.tekst,
                placement: "bottomRight",
                duration: 7,
                icon: <MessageOutlined style={{ color: "#001529" }} />
            });
        else
            notification["warning"]({
                message: recievedNotification.naslov,
                description: recievedNotification.tekst,
                placement: "bottomRight",
                duration: 7,
            });
        setBellColor({ color: "red" });
        setTimeout(() => {
            setBellColor({ color: "grey" });
        }, 7000);
    }

    const disconnect = () => {
        if (stompClient !== null) {
            stompClient.disconnect();
        }
        console.log("Disconnected");
    }

    useEffect(() => {
        connectToWebSocket();
        return () => { disconnectFromWebSocket(); };
    }, [])

    return (
        <div className="naslovna-header">
            <h2 className="pocetna"> {props.stranica} </h2>
            <div className="header-elementi">
                <BellOutlined data-testid="notifikacija-zvonce" style={bellColor} onClick={() => { history.push("/sve-notifikacije") }} /> &nbsp;&nbsp;
                    <LogoutOutlined data-testid="logout" style={{ color: "grey" }} onClick={logout} /> &nbsp;
                </div>
        </div>
    );
}

export default HeaderNaslovna;