import firebase from "firebase/compat";

importScripts('https://www.gstatic.com/firebasejs/8.9.0/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/8.9.0/firebase-analytics.js');
importScripts('https://www.gstatic.com/firebasejs/8.9.0/firebase-messaging.js')

if ('serviceWorker' in navigator) {
  navigator.serviceWorker.register('../firebase-messaging-sw.js')
    .then(function(registration) {
      console.log('Registration successful, scope is:', registration.scope);
    }).catch(function(err) {
    console.log('Service worker registration failed, error:', err);
  });
}

firebase.initializeApp({
  messagingSenderId: "1062432939073",
  appId:"1:1062432939073:web:b0e6f5cdca7dc69a40bac0"
})

const message =firebase.messaging();

