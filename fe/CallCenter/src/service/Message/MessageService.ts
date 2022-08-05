import {message} from 'antd';
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
  }
}


export default new MessageService()
