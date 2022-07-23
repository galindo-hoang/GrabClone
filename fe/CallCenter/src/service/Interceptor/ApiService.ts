import axios from 'axios';

export const instance =axios.create({
  headers: {
    'Content-Type': 'multipart/form-data',
  },
})

instance.interceptors.response.use(response =>{
  
 if(response.status===200){
   return response;
 }
 if(response.status===401){
   console.log(401)
 }
 if(response.status===403){
   console.log(403)
 }
})




instance.interceptors.request.use(request=>{

  return request;
})