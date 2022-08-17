import {notification} from 'antd';
import "antd/dist/antd.css";


export class NotificationService{
  static _instance:NotificationService;
  message:any;
  description:any;
  private setNotificationService(message:any,description:any):void{
    this.message=message;
    this.description=description
  }
  showNotification():void {
    notification.info({
      message: this.message,
      description:this.description,
    });
  }
  private constructor(message:any,description:any) {
    this.message=message;
    this.description=description
  }
  static getInstance(message:any,description:any): void {
    if(!NotificationService._instance) {
      NotificationService._instance = new NotificationService(message,description);
    }
    NotificationService._instance.setNotificationService(message,description)
    return NotificationService._instance.showNotification();
  }
}