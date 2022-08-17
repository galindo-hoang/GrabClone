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
  useEffect(()=>{
    registerNotification();
  },[]);
    message.onMessage((payload) => {

      console.log( payload.notification);
      const notification= NotificationService.getInstance(payload.notification.title,payload.notification.body);
      // if (!payload?.notification) {
      //   setStateData({...stateData, open: true, severity: 'error'})
      //   return;
      // }
      // const {notification} = payload
      // setStateData({...stateData, open: true, severity: 'success'})
      // setDataNotify({title: notification.title, body: notification.body} as Notification)
    });
  const handleClose = useCallback((event?: React.SyntheticEvent, reason?: string) => {
    setStateData({ ...stateData, open: false });
  }, [stateData]);

  return (
    <div>
    {/*<Alert onClose={handleClose} severity={stateData.severity}>*/}
    {/*  {dataNotify.title}*/}
    {/*</Alert>*/}
    <Routes/>
    </div>
  )
}

export default App
