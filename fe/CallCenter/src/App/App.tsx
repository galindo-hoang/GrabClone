import React, {useEffect} from "react"
import Routes from "src/routes/routes"
import {message, registerNotification} from '../service/FireBase/FirebaseService'
import { useState } from "react";
import  MuiAlert, { AlertProps } from '@material-ui/lab/Alert';
import Notification, {sendRegister} from "../@types/fcm"
import  Snackbar, {SnackbarOrigin} from '@material-ui/core/Snackbar';
import axios from "axios";
function Alert(props: AlertProps) {
  return <MuiAlert  variant="filled" {...props} />;
}

export interface State extends SnackbarOrigin {
  open: boolean;
  severity: "error" | "success" | "info" | "warning" | undefined
}

function App() {
  const [stateData, setStateData] = useState<State>({
    open: false,
    vertical: 'bottom',
    horizontal: 'left',
    severity: 'success'
  });
  const [dataNotify, setDataNotify] = useState<Notification>({title: '', body: ''}as Notification);
  message.onMessage((payload) => {
    if (!payload?.notification) {
      setStateData({...stateData, open: true, severity: 'error'})
      return;
    }
    const {notification} = payload
    console.log(notification)
    setStateData({...stateData, open: true, severity: 'success'})
    setDataNotify({title: notification.title, body: notification.body} as Notification)
  });
  useEffect(()=>{
    console.log(registerNotification());
  },[])
  return <Routes/>
}

export default App
