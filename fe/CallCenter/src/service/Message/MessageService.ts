import {message,Modal, Space} from 'antd';
import "antd/dist/antd.css";

const key = 'updatable'

// class MessageService {
//   openMessage = (content:any,flag:boolean=false): void => {
//     message.loading({content: content.loading, key})
//     if (flag === true) {
//       message.success({content: content.loaded, key, duration: 2})
//       setTimeout(() => {
//       }, 1000)
//     }
//   };
//   warning = (title:string) => {
//     Modal.warning({
//       title: 'Thông báo',
//       content: `${title}`,
//     });
//   };
// }

class MessageService {
  static _instance:MessageService;
   flag:boolean;
   content:any;
   showMessage():void{
    message.loading({content: this.content.loading, key});
    if (this.flag === true) {
      message.success({content: this.content.loaded, key, duration: 2})
      setTimeout(() => {
      }, 1000)
    }
  }
  private setFlag(_flag:boolean):void{
     this.flag=_flag;
  }
  private constructor(_content: any,_flag:boolean) {
    this.content=_content;
    this.flag=_flag;
  }
  static getInstance(_content:any,_flag:boolean): void {
    if(!MessageService._instance) {
      MessageService._instance = new MessageService(_content,_flag);
    }
    MessageService._instance.setFlag(_flag);
    return MessageService._instance.showMessage();
  }
}



export default MessageService
