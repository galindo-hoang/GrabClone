import firebase from "firebase/compat";
import '@firebase/messaging'


const vapidKey ="BGJAvRpdq0x88UOpB12JeMFx8GbZsttxJXxMuT2Glad3lneOAtTsAS4R1XyvngzWmLBmfo9f8A1Hz5gaITQbofU"
const firebaseConfig ={
  apiKey: "AIzaSyCL7Loiq6LmpS-VA2q0USjcaqRgeFLjFxQ",
  authDomain: "grabclone-19ktpm.firebaseapp.com",
  projectId: "grabclone-19ktpm",
  storageBucket: "grabclone-19ktpm.appspot.com",
  messagingSenderId: "1062432939073",
  appId: "1:1062432939073:web:b0e6f5cdca7dc69a40bac0",
  measurementId: "G-25ETJ96ZRC"
};
firebase.initializeApp(firebaseConfig)
export const message = firebase.messaging()
export default firebase

