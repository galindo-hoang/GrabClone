import React, {useEffect, useRef, useState} from "react"
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
import {featuresLocation, info2Location, location} from "../../@types/bookingcar";
import { Geocoder } from "src/service/BookingCar/GeoCoder";
import { AutoComplete } from 'antd';
import BookingService from "../../service/BookingCar/BookingService";
import {coordinate} from "../../@types/map";
const accessToken = "pk.eyJ1IjoicGhhbXRpZW5xdWFuIiwiYSI6ImNsNXFvb2h3ejB3NGMza28zYWx2enoyem4ifQ.v-O4lWtgCXbhJbPt5nPFIQ";
const mapStateToProps = state => ({

})

const mapDispatchToProps = {
  bookingCar
}
const connector = connect(mapStateToProps, mapDispatchToProps)
interface Props extends ConnectedProps<typeof connector> {}

const BookingCar =(props:Props)=>{
  const {bookingCar}=props
  const [fullName,setFullName]=useState("");
  const [phoneNumber,setPhoneNumber]=useState("");

  const [departure,setDeparture]=useState<featuresLocation>({
    value:undefined,
    coordinate:undefined,
  });
  const [departureAutocomplete,setDepartureAutocomplete]=useState<{ value: string, coordinate:coordinate}[]>([]);

  const [destination,setDestination]=useState<featuresLocation>({
    value:undefined,
    coordinate:undefined,
  });
  const [destinationAutocomplete,setDestinationAutocomplete]=useState<{ value: string,coordinate:coordinate }[]>([]);



  const [note,setNote]=useState("");
  const history = useHistory();
  useEffect(()=>{
    const getAutoCompleteDeparture= async ()=> {
      await BookingService.autoComplete(departure.value as string).then(res => {
        const data=(res.data.features.map((index)=> {
          const data:featuresLocation={
            coordinate:{
              longitude:index.geometry.coordinates[0],
              latitude:index.geometry.coordinates[1],
            },
            value:index.properties.formatted
          }
          return data;
        }))
        setDepartureAutocomplete(data)
      })
    }
    getAutoCompleteDeparture()
  },[departure.value])

  useEffect(()=>{
    const getAutoCompleteDestination=async ()=> {
      await BookingService.autoComplete(destination.value as string).then(res => {
       const data=(res.data.features.map((index)=> {
         const data:featuresLocation={
           coordinate:{
             longitude:index.geometry.coordinates[0],
             latitude:index.geometry.coordinates[1],
           },
           value:index.properties.formatted
         }
         return data;
       }));
       setDestinationAutocomplete(data)
      })
    }
    getAutoCompleteDestination()
  },[destination.value]);

  const onSelectedDeparture=(address)=>{
    const temp:{ value: string; coordinate: coordinate }[] | undefined= departureAutocomplete?.filter(index =>(
      index.value===address
    ));
    setDeparture({
      value:temp[0].value,
      coordinate:temp[0].coordinate
    })
  }
  const onSelectedDestination=(address)=>{
    const temp:{ value: string; coordinate: coordinate }[] | undefined=destinationAutocomplete?.filter(index =>(
      index.value===address
    ));
    setDestination({
      value:temp[0].value,
      coordinate:temp[0].coordinate
    })
  }


  const onChangeFullName=(event)=>{
    setFullName(event?.target?.value)
  }
  const onChangePhoneNumber=(event)=>{
    setPhoneNumber(event?.target?.value)
  }
  const onChangeDeparture=(data)=>{
    setDeparture({
      value:data
    })
  }
  const onChangeDestination=(data)=>{
    setDestination({
      value:data
    });
  }
  const onChangeNote=(event)=>{
    setNote(event?.target?.value)
  }
  const submit=event=> {
  }

  return (
    <MainLayout>
      <div className="container">
        <div className="min-vh-30 row">
          <div className="col-md-6 m-auto">
            <div className="p-5 rounded-sm shadow text-center info-background" >
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
                value={departure.value}
                onChange={onChangeDeparture}
                onSelect={onSelectedDeparture}
                placeholder="Điền địa chỉ đón"
              />
              <label className="float-left mb-1">Địa chỉ đến</label>
              <AutoComplete
                className="form-control form-control-lg mb-3"
                options={destinationAutocomplete}
                value={destination.value}
                onChange={onChangeDestination}
                onSelect={onSelectedDestination}
                placeholder="Điền địa chỉ đến"
              />
              <label className="float-left mb-1">Ghi chú</label>
              <input
                type="text"
                placeholder="Điền ghi chú"
                className="form-control form-control-lg mb-3"
                onChange={onChangeNote}
              />
              <button type="submit" className="btn btn-block btn-info btn-lg" onClick={()=>{
                if (destination.value !== undefined && departure.value !== undefined) {
                  console.log(destination, departure)
                  const position: info2Location = {
                    departure: departure,
                    destination: destination
                  }
                  bookingCar(position)
                  history.push(PATH.MAP);
                }
                else{
                  MessageService.warning("Vui lòng nhập đầy đủ thông tin")
                }
              }}>
                  Xác nhận
              </button>
            </div>
          </div>
        </div>
      </div>
    </MainLayout>
  )
}

export default connector(BookingCar)
