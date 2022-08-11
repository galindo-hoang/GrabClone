import React, {useRef, useState} from "react"
import MainLayout from "src/layouts/MainLayout"
import {Title} from "../BookingCar/BookingCar.styles";
import {PATH} from "../../constants/paths";
import {useHistory} from "react-router-dom";
import MessageService from "src/service/Message/MessageService";
import "antd/dist/antd.css";


export default function BookingCar() {
  const [fullname,setFullName]=useState("");
  const [phoneNumber,setPhoneNumber]=useState("");
  const [pickUpAdress,setPickUpAdress]=useState("");
  const [destination,setDestination]=useState("");
  const [note,setNote]=useState("");
  const history = useHistory();
  const onChangeFullName=(event)=>{
    setFullName(event?.target?.value)
  }
  const onChangePhoneNumber=(event)=>{
    setPhoneNumber(event?.target?.value)
  }
  const onChangePickUpAddress=(event)=>{
    setPickUpAdress(event?.target?.value)
  }
  const onChangeDestination=(event)=>{
    setDestination(event?.target?.value)
  }
  const onChangeNote=(event)=>{
    setNote(event?.target?.value)
  }
  const submit=event=>{
    event.preventDefault();
    history.push(PATH.MAP);
  }
  return (
    <MainLayout>
      <div className="container">
        <div className="min-vh-30 row">
          <div className="col-md-6 m-auto">
            <form className="p-5 rounded-sm shadow text-center" onSubmit={submit}>
              <Title>Đặt xe</Title>
              <p className="text-muted">Vui lòng điền đầy đủ thông tin </p>
              <label className="float-left mb-1">Họ và tên</label>
              <input
                type="text"
                placeholder="Điền đầy đủ họ tên"
                className="form-control form-control-lg mb-3"
                onChange={onChangeFullName}
              />
              <label className="float-left mb-1">Số điện thoại</label>
              <input
                type="text"
                placeholder="Điền số điện thoại"
                className="form-control form-control-lg mb-3"
                onChange={onChangePhoneNumber}
              />
              <label className="float-left mb-1">Địa chỉ đón</label>
              <input
                type="text"
                placeholder="Điền địa chỉ đi"
                className="form-control form-control-lg mb-3"
                onChange={onChangePickUpAddress}
              />
              <label className="float-left mb-1">Địa chỉ đến</label>
              <input
                type="text"
                placeholder="Điền địa chỉ đến"
                className="form-control form-control-lg mb-3"
                onChange={onChangeDestination}
              />
              <label className="float-left mb-1">Ghi chú</label>
              <input
                type="text"
                placeholder="Điền ghi chú"
                className="form-control form-control-lg mb-3"
                onChange={onChangeNote}
              />
              <button type="submit" className="btn btn-block btn-info btn-lg">
                Đặt xe
              </button>
            </form>
          </div>
        </div>
      </div>
    </MainLayout>
  )
}
