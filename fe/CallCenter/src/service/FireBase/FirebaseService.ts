// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getMessaging, getToken } from "firebase/messaging";
import {  getFirestore } from "firebase/firestore";
import firebase from "firebase/compat";
import {collection, getDocs,addDoc} from 'firebase/firestore'
import {recentPhoneNumber, timestamp} from "../../@types/bookingcar";
import axios from "axios";
import { sendRegister } from "src/@types/fcm";
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

const POST_REGISTER_FCM="http://localhost:8082/api/v1/fcm/register";
export const message = firebase.messaging();
export const registerNotification=()=> {
  Notification.requestPermission().then((permisson) => {
    if(permisson==='granted') {
      message.getToken({vapidKey: vapidKey}).then(async token => {
        console.log(token)
        return token
      })
    }
    else{
      console.log("error")
    }
  })
};



















const config={
    apiKey: "AIzaSyCL7Loiq6LmpS-VA2q0USjcaqRgeFLjFxQ",
    authDomain: "grabclone-19ktpm.firebaseapp.com",
    projectId: "grabclone-19ktpm",
    storageBucket: "grabclone-19ktpm.appspot.com",
    messagingSenderId: "1062432939073",
    appId: "1:1062432939073:web:b0e6f5cdca7dc69a40bac0",
    measurementId: "G-25ETJ96ZRC"
}
export
const app=initializeApp(config);
export const databaseFireBase= getFirestore(app);

export const addPhoneRecent= async (collection:any,recentPhoneNumber:recentPhoneNumber)=>{
  return await addDoc(collection,recentPhoneNumber);
}


export const getPhoneNumber=(arr:object[])=>{
  let temp=(new Set(arr.map((index:recentPhoneNumber)=>index.phonenumber)));
  const arrTemp:recentPhoneNumber[]=[];
  temp.forEach(u=>{
    const object:recentPhoneNumber={
      phonenumber:u,
    }
    arrTemp.push(object);
  });
  if(arrTemp.length>=5){
    return arrTemp.splice(5)
  }
  else{
    return arrTemp;
  }
}
export const convertDateFireBase=(time:timestamp)=>{
  const fireBaseTime = new Date(
    time.seconds * 1000 + time.nanoseconds / 1000000,
  );
  const date = fireBaseTime.toDateString();
  const atTime = fireBaseTime.toLocaleTimeString();
  return (date + atTime)
}







