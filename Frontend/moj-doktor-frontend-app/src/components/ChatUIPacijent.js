import React, { useEffect, useState, useRef } from 'react';
import HeaderNaslovna from './HeaderNaslovna';
import { ChatFeed, Message } from 'react-chat-ui'
import '../assets/css/chat.css'
import { Button, Input, message } from 'antd';
import { SendOutlined } from '@ant-design/icons';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import { useParams } from 'react-router';
import PacijentSideBar from './PacijentSideBar';
import Loader from './Loader';

const SERVER_URL = 'http://localhost:8084/ws';
const SERVER_URL_NOTIFICATIONS = 'http://localhost:8083/ws';

let stompClient;
let stompClientNotifications;

let typingTimeout = null;

const ChatUIPacijent = (props) => {

    const { idDoktoraSagovornika } = useParams();

    //let messagesList = [];

    let [loading, setLoading] = useState(true);
    const [imePrezimeSagovornika, setImePrezimeSagovornika] = useState('');
    const [messages, setMessages] = useState([]);
    const [currentMessage, setCurrentMessage] = useState('');
    const [isTyping, setIsTyping] = useState(false);

    const divRef = useRef();

    useEffect(() => {
        scrollChatToBottom();
        setImePrezimeSagovornika(props.location.state.imePrezimeSagovornika);
        let URL = 'http://localhost:8080/chat/poruke';
        const headers = new Headers({ "Authorization": 'Bearer ' + localStorage.getItem("token") });
        fetch(URL, { method: "get", headers })
            .then((res) => {
                return res.json();
            })
            .then((res) => {
                console.log("evo me");
                console.log(res);
                const fetchedMessages = res;
                console.log("dosadasnje: ", fetchedMessages);
                let messagesArray = [];
                fetchedMessages.forEach(fetchedMessage => {
                    let newMessage = null;
                    if (fetchedMessage.posiljalac.id == localStorage.getItem("id") && fetchedMessage.primalac.id == idDoktoraSagovornika) {
                        newMessage = new Message({
                            id: 0,
                            message: fetchedMessage.sadrzaj,
                        });
                        messagesArray.push(newMessage);
                    }
                    else if (fetchedMessage.posiljalac.id == idDoktoraSagovornika && fetchedMessage.primalac.id == localStorage.getItem("id")) {
                        newMessage = new Message({
                            id: 1,
                            message: fetchedMessage.sadrzaj,
                        });
                        messagesArray.push(newMessage);
                    }
                });
                localStorage.setItem("messages", JSON.stringify(messagesArray));
                setMessages(messagesArray);
                setLoading(false);
                connectToWebSocket();
                connectToWebSocketNotifications();
                scrollChatToBottom();
            })
            .catch(() => {
                message.error("Došlo je do greške pri učitavanju podataka. Pokušajte ponovo.");
            });
            return () => {
                disconnectFromWebSocket();
                disconnectFromWebSocketNotifications();
            };
    }, []);

    const scrollChatToBottom = () => {
        var element = document.getElementById("scrollable-chat-div");
        if (element === null) return;
        element.scrollTop = element.scrollHeight;
    };

    const handleMessageInput = e => setCurrentMessage(e.target.value);

    const connectToWebSocket = () => {
        connect();
    };

    const connectToWebSocketNotifications = () => {
        connectNotifications();
    }

    const disconnectFromWebSocket = () => {
        setMessages([]);
        disconnect();
    };

    const disconnectFromWebSocketNotifications = () => {
        disconnectNotifications();
    };

    const handleTyping = (event) => {
        if (event.code == "Enter") return;
        const typingMessage = {
            senderId: localStorage.getItem("id"),
            recipientId: idDoktoraSagovornika.toString()
        };
        stompClient.send("/typing/isTyping", {}, JSON.stringify(typingMessage));
    };

    function connect() {
        var socket = new SockJS(SERVER_URL);
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);
    }

    function connectNotifications() {
        var socketNotifications = new SockJS(SERVER_URL_NOTIFICATIONS);
        stompClientNotifications = Stomp.over(socketNotifications);
        stompClientNotifications.connect({}, onConnectedNotifications, onErrorNotifications);
    }

    const onConnected = (frame) => {
        console.log('Connected: ' + frame);
        stompClient.subscribe("/user/" + localStorage.getItem("id") + "/queue/messages", onMessageReceived);
        stompClient.subscribe("/user/" + localStorage.getItem("id") + "/queue/typingMessages", onTypingMessageReceived);
    };

    const onError = (err) => {
        console.log(err);
    };

    const onConnectedNotifications = (frame) => {
        console.log('Connected: ' + frame);
    };

    const onErrorNotifications = (err) => {
        console.log(err);
    };

    const onMessageReceived = (message) => {
        let receivedMessage = JSON.parse(message.body);
        if (receivedMessage.senderId != idDoktoraSagovornika) return;
        let mes = JSON.parse(localStorage.getItem("messages"));
        const newMessage = new Message({
            id: 1,
            message: receivedMessage.content,
        });
        mes.push(newMessage);
        localStorage.setItem("messages", JSON.stringify(mes));
        setMessages(mes);
        scrollChatToBottom();
    }

    const onTypingMessageReceived = (message) => {
        let receivedMessage = JSON.parse(message.body);
        if (receivedMessage.senderId != idDoktoraSagovornika) return;
        setIsTyping(true);
        if (typingTimeout != null) clearTimeout(typingTimeout);
        typingTimeout = setTimeout(() => {
            setIsTyping(false);
        }, 400);
        scrollChatToBottom();
    }
    
    const disconnect = () => {
        if (stompClient !== null) {
            stompClient.disconnect();
        }
        console.log("Disconnected");
    }

    const disconnectNotifications = () => {
        if (stompClientNotifications !== null) {
            stompClientNotifications.disconnect();
        }
        console.log("Disconnected");
    }

    const handleSend = () => {
        const messageText = document.getElementById("tekstPoruke").value;
        if (messageText.trim() != "") {
            sendMessage(messageText);
            let newMessage = new Message({
                id: 0,
                message: messageText,
            });
            localStorage.setItem("messages", JSON.stringify([...messages, newMessage]));
            setMessages([...messages, newMessage]);
        }
        setCurrentMessage('');
        scrollChatToBottom();
    };

    function sendMessage(messageText) {
        const newMessage = {
            senderId: localStorage.getItem("id"),
            recipientId: idDoktoraSagovornika.toString(),
            content: messageText,
            timestamp: new Date()
        };
        stompClient.send("/app/chat", {}, JSON.stringify(newMessage));
        const newMessages = [...messages];
        newMessages.push(newMessage);
        setMessages(newMessages);

        sendChatNotification(messageText);
    }

    function sendChatNotification(tekst) {
        const newNotification = {
            senderId: Number(localStorage.getItem("id")),
            recipientId: Number(idDoktoraSagovornika),
            naslov: localStorage.getItem("ime") + " " + localStorage.getItem("prezime"),
            tekst: tekst,
            datum: "datum",
            vrijeme: "00:00"
        };
        stompClientNotifications.send("/app/chat-notifikacije", {}, JSON.stringify(newNotification));
        console.log(newNotification);
    }

    return (
        <PacijentSideBar>
            <HeaderNaslovna stranica={imePrezimeSagovornika} zaobidjiChatNotifikacije={true} />
            <div className="chat">
                {loading ? <><br /><br /><br /><Loader /></> :
                    <>
                        <div id="scrollable-chat-div" className="chat-prozor">
                            <ChatFeed id="chat-prozor"
                                messages={messages} // Array: list of message objects
                                isTyping={isTyping} // Boolean: is the recipient typing
                                hasInputField={false} // Boolean: use our input, or use your own
                                showSenderName // show the name of the user who sent the message
                                bubblesCentered={false} //Boolean should the bubbles be centered in the feed?
                                // JSON: Custom bubble styles
                                bubbleStyles={
                                    {
                                        text: {
                                            fontSize: 16
                                        },
                                        chatbubble: {
                                            borderRadius: 12,
                                            padding: 5,
                                            backgroundColor: "#8c8c8c"
                                        },
                                        userBubble: {
                                            backgroundColor: "#0084FF"
                                        }
                                    }
                                }
                            />
                            <div id="za-scroll" ref={divRef}>&nbsp;</div>
                        </div>
                        <div>
                            <Input id="tekstPoruke" placeholder="Poruka" value={currentMessage} onChange={handleMessageInput}className="tipkanje" onPressEnter={handleSend} onKeyPress={handleTyping} />
                            <Button type="submit" className="sendButton" onClick={handleSend}>
                                <SendOutlined />
                            </Button>
                        </div>
                    </>}
            </div>
        </PacijentSideBar>
    );
}

export default ChatUIPacijent;