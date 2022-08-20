
importScripts('https://www.gstatic.com/firebasejs/8.9.0/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/8.9.0/firebase-analytics.js');
importScripts('https://www.gstatic.com/firebasejs/8.9.0/firebase-messaging.js')
importScripts('https://www.gstatic.com/firebasejs/9.0.0/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/9.0.0/firebase-messaging-compat.js');

if ('serviceWorker' in navigator) {
  navigator.serviceWorker.register('../public/firebase-messaging-sw.js')
    .then(function(registration) {
      console.log('Registration successful, scope is:', registration.scope);
    }).catch(function(err) {
    console.log('Service worker registration failed, error:', err);
  });
}

firebase.initializeApp({
  apiKey: "AIzaSyCL7Loiq6LmpS-VA2q0USjcaqRgeFLjFxQ",
  authDomain: "grabclone-19ktpm.firebaseapp.com",
  projectId: "grabclone-19ktpm",
  storageBucket: "grabclone-19ktpm.appspot.com",
  messagingSenderId: "1062432939073",
  appId: "1:1062432939073:web:b0e6f5cdca7dc69a40bac0",
  measurementId: "G-25ETJ96ZRC"
})

const message =firebase.messaging();

