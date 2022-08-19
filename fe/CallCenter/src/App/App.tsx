import React, {useCallback, useEffect} from "react"
import Routes from "src/routes/routes"
import {message, registerNotification} from '../service/FireBase/FirebaseService'
import { useState } from "react";
import  MuiAlert, { AlertProps } from '@material-ui/lab/Alert';
import Notification, {sendRegister} from "../@types/fcm"
import  Snackbar, {SnackbarOrigin} from '@material-ui/core/Snackbar';
import axios from "axios";
import {MessageLoadMapService} from "../service/Message/MessageService";
import {NotificationService} from "../service/Notification/NotificationService";
import {connect, ConnectedProps} from "react-redux";
import {receivedPayload,clearFCM} from "./App.thunk";


const mapStateToProps = state => ({
})

const mapDispatchToProps = {
  receivedPayload,
  clearFCM
}
const connector = connect(mapStateToProps, mapDispatchToProps)
interface Props extends ConnectedProps<typeof connector> {
}
const App=(props: Props) =>{
  const {receivedPayload,clearFCM} = props;
  useEffect(()=>{
    registerNotification();
    console.log("register")
  },[]);
  useEffect(()=>{
    message.onMessage((payload) => {
      console.log(payload)
      const notification= NotificationService.getInstance(payload.notification.title,payload.notification.body);
      receivedPayload(payload.data);
      // if (!payload?.notification) {
      //   setStateData({...stateData, open: true, severity: 'error'})
      //   return;
      // }
      // const {notification} = payload
      // setStateData({...stateData, open: true, severity: 'success'})
      // setDataNotify({title: notification.title, body: notification.body} as Notification)

    });
  },[])

  return (
    <Routes/>
  )
}

export default connector(App)
