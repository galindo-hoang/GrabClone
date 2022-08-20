import {message,Modal, Space} from 'antd';
import "antd/dist/antd.css";

const key = 'updatable'

export class MessageLoadMapService {
  static _instance:MessageLoadMapService;
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
    if(!MessageLoadMapService._instance) {
      MessageLoadMapService._instance = new MessageLoadMapService(_content,_flag);
    }
    MessageLoadMapService._instance.setFlag(_flag);
    return MessageLoadMapService._instance.showMessage();
  }
}
export class MessageWarningService{
  static _instance:MessageWarningService;
  content:any;
  showMessage():void{
        Modal.warning({
      title: 'Thông báo',
      content: `${this.content}`,
    });
  }
  private setContent(_content:any):void{
    this.content=_content
  }
  private constructor(_content:any) {
    this.content=_content
  }
  static getInstance(_content:any):void{
    if(!MessageWarningService._instance){
      MessageWarningService._instance=new MessageWarningService(_content);
    }
    MessageWarningService._instance.setContent(_content);
    return MessageWarningService._instance.showMessage();
  }
}




