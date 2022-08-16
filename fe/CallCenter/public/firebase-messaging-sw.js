import firebase from "firebase/compat";

importScripts('https://www.gstatic.com/firebasejs/7.13.2/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/7.13.2/firebase-messaging.js');

firebase.initializeApp({
  messagingSenderId: "1062432939073",
})
const initMessaging=firebase.messaging();