import React, {useRef, useState} from "react"
import MainLayout from "src/layouts/MainLayout"
import {Title} from "../BookingCar/BookingCar.styles";
import {PATH} from "../../constants/paths";
import {useHistory} from "react-router-dom";
import MessageService from "src/service/Message/MessageService";
import "antd/dist/antd.css";
import { Button, Checkbox, Form, Input } from 'antd';
import { NavLink } from "react-router-dom"
import {connect, ConnectedProps, useSelector} from "react-redux"
import { bookingCar } from "./BookingCar.thunks";
import {localtion} from "../../@types/bookingcar";
import { Geocoder } from "src/service/BookingCar/GeoCoder";
import { AutoComplete } from 'antd';
const accessToken = "pk.eyJ1IjoicGhhbXRpZW5xdWFuIiwiYSI6ImNsNXFvb2h3ejB3NGMza28zYWx2enoyem4ifQ.v-O4lWtgCXbhJbPt5nPFIQ";
const mapStateToProps = state => ({

})

const mapDispatchToProps = {
  bookingCar
}
const setOptionDestination = (str: string, repeat = 1) => ({
  value: str.repeat(repeat),
});

const setOptionDeparture = (str: string, repeat = 1) => ({
  value: str.repeat(repeat),
});

const connector = connect(mapStateToProps, mapDispatchToProps)
interface Props extends ConnectedProps<typeof connector> {}

const BookingCar =(props:Props)=>{
  const {bookingCar}=props
  const [fullname,setFullName]=useState("");
  const [phoneNumber,setPhoneNumber]=useState("");

  const [departure,setDeparture]=useState('');
  const [departureAutocomplete,setDepartureAutocomplete]=useState<{ value: string }[]>([]);

  const [destination,setDestination]=useState('');
  const [destinationAutocomplete,setDestinationAutocomplete]=useState<{ value: string }[]>([]);

  const [note,setNote]=useState("");
  const history = useHistory();
  const onChangeFullName=(event)=>{
    setFullName(event?.target?.value)
  }
  const onChangePhoneNumber=(event)=>{
    setPhoneNumber(event?.target?.value)
  }
  const onChangeDeparture=(data)=>{
    setDeparture(data)
  }
  const onChangeDestination=(data)=>{
    setDestination(data);
  }
  const onSearchDestination=(searchText)=>{
    setDestinationAutocomplete(
      !searchText ? [] : [setOptionDestination(searchText), setOptionDestination(searchText, 2), setOptionDestination(searchText, 3)])
  }
  const onSearchDeparture=(searchText)=>{
    setDepartureAutocomplete(
      !searchText ? [] : [setOptionDeparture(searchText), setOptionDeparture(searchText, 2), setOptionDeparture(searchText, 3)]
    )
  }
  const onChangeNote=(event)=>{
    setNote(event?.target?.value)
  }
  const submit=event=>{
    const location:localtion={};
    location.destination=destination;
    location.departure=departure;
    bookingCar(location)
    event.preventDefault();
    history.push(PATH.MAP);
  }

  return (
    <MainLayout>
      <div className="container">
        <div className="min-vh-30 row">
          <div className="col-md-6 m-auto">
            <form className="p-5 rounded-sm shadow text-center info-background" onSubmit={submit}>
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
              <AutoComplete
                className="form-control form-control-lg mb-3"
                options={departureAutocomplete}
                value={departure}
                onSearch={onSearchDeparture}
                onChange={onChangeDeparture}
                placeholder="Điền địa chỉ đón"
              />
              <label className="float-left mb-1">Địa chỉ đến</label>
              <AutoComplete
                className="form-control form-control-lg mb-3"
                options={destinationAutocomplete}
                value={destination}
                onSearch={onSearchDestination}
                onChange={onChangeDestination}
                placeholder="Điền địa chỉ đến"
              />
              <label className="float-left mb-1">Ghi chú</label>
              <input
                type="text"
                placeholder="Điền ghi chú"
                className="form-control form-control-lg mb-3"
                onChange={onChangeNote}
              />
              <button type="submit" className="btn btn-block btn-info btn-lg">
                  Xác nhận
              </button>
            </form>
          </div>
        </div>
      </div>
    </MainLayout>
  )
}

export default connector(BookingCar)
