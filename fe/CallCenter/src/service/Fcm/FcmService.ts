// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getMessaging, getToken } from "firebase/messaging";
import firebase from "firebase/compat";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries
const vapidKey ="BGJAvRpdq0x88UOpB12JeMFx8GbZsttxJXxMuT2Glad3lneOAtTsAS4R1XyvngzWmLBmfo9f8A1Hz5gaITQbofU"
firebase.initializeApp( {
  apiKey: "AIzaSyCL7Loiq6LmpS-VA2q0USjcaqRgeFLjFxQ",
  authDomain: "grabclone-19ktpm.firebaseapp.com",
  projectId: "grabclone-19ktpm",
  storageBucket: "grabclone-19ktpm.appspot.com",
  messagingSenderId: "1062432939073",
  appId: "1:1062432939073:web:b0e6f5cdca7dc69a40bac0",
  measurementId: "G-25ETJ96ZRC"
});
const messaging = firebase.messaging();
export const notification=()=> {
  Notification.requestPermission().then((token) => {
    return messaging.getToken({vapidKey:vapidKey})
  }).then(token => {
    console.log(token)
    return token
  })
}



