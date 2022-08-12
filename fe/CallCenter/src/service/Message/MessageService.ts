import {message,Modal, Space} from 'antd';
import "antd/dist/antd.css";

const key = 'updatable'

class MessageService {
  openMessage = (content:any,flag:boolean=false): void => {
    message.loading({content: content.loading, key})
    if (flag === true) {
      message.success({content: content.loaded, key, duration: 2})
      setTimeout(() => {
      }, 1000)
    }
  };
  warning = (title:string) => {
    Modal.warning({
      title: 'Thông báo',
      content: `${title}`,
    });
  };
}


export default new MessageService()
