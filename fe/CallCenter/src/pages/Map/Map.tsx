import React, {useEffect, useRef, useState} from "react"
import MainLayout from "../../layouts/MainLayout";
import {Title} from "./Map.styles"
import ReactMapGL, {GeolocateControl, Layer, Marker, NavigationControl, Popup, Source, useControl} from 'react-map-gl';
import 'mapbox-gl/dist/mapbox-gl.css'
import MapService from "src/service/Map/MapService";
import {coordinate, initialCreateBooking} from "src/@types/map";
import 'antd/dist/antd.css';
import {MessageLoadMapService} from "src/service/Message/MessageService";
import {CarOutlined, EnvironmentOutlined} from '@ant-design/icons'
import {COLOR} from "src/constants/styles";
import {useSpring,animated} from 'react-spring'
import MapBoxGeocoder from '@mapbox/mapbox-gl-geocoder';
import {connect, ConnectedProps, useSelector} from "react-redux"
import InfiniteScroll from 'react-infinite-scroll-component';
import {
  location,
  info2Location,
  featuresLocation,
  responseFinishedRide,
  responseAcceptedRider
} from "src/@types/bookingcar";
import {Badge, Card, Descriptions, Divider, Drawer, List, Row, Skeleton} from "antd";
import bookingCar from "../BookingCar/BookingCar";
import {BODYSTATES} from "../../constants/states";
import {objectTraps} from "immer/dist/core/proxy";
import {handlePrice} from "../../helpers/string";
import {clearBookingCar} from "../BookingCar/BookingCar.thunks";
import {clearFCM} from "../../App/App.thunk";
import Col from "antd/es/grid/col";
import { Scrollbars } from 'react-custom-scrollbars';
import {TYPECAR} from "../../constants/typecar";
const accessToken = "pk.eyJ1IjoicGhhbXRpZW5xdWFuIiwiYSI6ImNsNXFvb2h3ejB3NGMza28zYWx2enoyem4ifQ.v-O4lWtgCXbhJbPt5nPFIQ";



const ProcessMove = (props:any) => {
  const {location,drawer,processMove}=props;
  const [toggle, setToggle] = useState(false);
  const fade=useSpring({
    opacity:toggle?0:1
  })
  return (
    <div
      style={{position: "absolute",left:'5px',top:'5px',maxWidth: "320px"}}
    >
      <button className="btn  btn-info btn-sm" style={{position: "relative",width:'120px'}} onClick={() => setToggle(!toggle)}>{toggle?'Hiện thông tin':'Ẩn thông tin'}</button>
      <button className="btn  btn-info btn-sm ml-2" onClick={drawer}>Hiện trạng thái đặt xe</button>
      <animated.form style={fade} className="p-5 rounded-sm shadow text-center info-background" hidden={toggle}>
        <Row gutter={{ xs: 8, sm: 16, md: 24, lg: 32 }}>
          <Col className="gutter-row" span={24}>
        <Title>Hướng dẫn di chuyển</Title>
            <hr/>
          </Col>
          <Scrollbars
            style={{ width: 500, height: 300 }}
          >
            {processMove.map(index=>
              <Col className="gutter-row" span={24}>{index}</Col>
            )}
          </Scrollbars>
        </Row>
      </animated.form>
    </div>
  );
}
const mapStateToProps = state => ({
  closeSideNav: state.app.closeSideNav,
  payloadFCM:state.app.payloadFCM||null,
  bookingCarForm:state.bookingCar,
})

const mapDispatchToProps = {
  clearBookingCar,
  clearFCM
}

const connector = connect(mapStateToProps, mapDispatchToProps);
interface Props extends ConnectedProps<typeof connector> {}


const StateBooking={
  CREATED:"Tạo booking thành công",
  ACCEPTED:"Tài xế đã chấp nhận cuốc xe",
  UPDATE:"Tài xế đang đón",
  FINISH:"Người đặt đã đến nơi",
  DRIVERWELCOMEGUESTS:"Tài xế đã đón khách",
  CANCELLEDBYDRIVER:"Tài xế đã hủy chuyến"
}
const Map = (props:Props) => {
  const {closeSideNav,payloadFCM,bookingCarForm,clearBookingCar,clearFCM} = props;
  const [payloadFCMValue,setPayloadFCMValue]=useState<Object>(payloadFCM);
  const [viewCoordinate,setViewCoordinate]=useState<coordinate>({
    longitude:bookingCarForm?.departure.coordinate?.longitude as number,
    latitude:bookingCarForm?.departure.coordinate?.latitude as number,
  });
  const [visibleState, setVisibleState] = useState<boolean>(false);
  const [state,setState]=useState(0)
  const [zoom, setZoom] = useState(18)
  const [loadMap, setLoadMap] = useState(false)
  const [polyLine, setPolyLine] = useState([] as coordinate);
  const [showPopupDestination, setShowPopupDestination] = useState(false);
  const [showPopupDeparture, setShowPopupDeparture] = useState(false);
  const [processMove,setProcessMove]=useState([]);
  const showDrawerState = () => {
    setVisibleState(true);
  };
  const onCloseDrawer = () => {
    setVisibleState(false);
  };
  const [destinationCoordinate,setDestinationCoordinate]=useState<featuresLocation>({
    value:bookingCarForm.destination.value,
    coordinate:bookingCarForm.destination.coordinate as coordinate
  });
  const [departureCoordinate,setDepartureCoordinate]=useState<featuresLocation>({
    value:bookingCarForm.departure.value,
    coordinate:bookingCarForm.departure.coordinate as coordinate
  });
  const locationBooking:location={
    destination:bookingCarForm.destination.value,
    departure:bookingCarForm.departure.value
  }
  const [driverCoordinate,setDriverCoordinate]=useState<coordinate>({
    longitude:undefined,
    latitude:undefined
  });
  const [stateBooking,setStateBooking]=useState<any>(StateBooking.CREATED);
  const [finishSuccess,setFinishSuccess]=useState<responseFinishedRide>({
    endTime:undefined,
    rideId:undefined,
    startTime:undefined
  });
  const [driverAccepted,setDriverAccepted]=useState<responseAcceptedRider>({
    bookingId:undefined
  });

  useEffect(()=>{
    console.log(payloadFCM)
     /* if (bookingCarForm.bookingForm.id === JSON.parse(payloadFCM.booking).bookingId) {*/
        //DRIVER ACCEPTED
    if(payloadFCM!==null) {
      /*if (bookingCarForm.bookingForm.id === JSON.parse(JSON.parse(payloadFCM.booking).bookingId)) */{
        //DRIVER ACCEPTED
        if (payloadFCM.body.toString().includes(BODYSTATES.DRIVER_ACCEPTED)) {
          setDriverAccepted(JSON.parse(JSON.parse(payloadFCM.booking).bookingId) as responseAcceptedRider);
          setStateBooking(StateBooking.ACCEPTED)
        }
        //DRIVER UPDATE LOCATION
        else if (payloadFCM.body.toString().includes(BODYSTATES.DRIVER_UPDATE_LOCATION)) {
          const driverCoordinate=JSON.parse(JSON.parse(payloadFCM.ride).driverLocation) as coordinate;
          setDriverCoordinate(driverCoordinate);
          //DRIVER WELCOME GUEST
          if(driverCoordinate.longitude===departureCoordinate.coordinate?.longitude && driverCoordinate.latitude===departureCoordinate.coordinate?.latitude){
            setStateBooking(StateBooking.DRIVERWELCOMEGUESTS);
            const setLineUpFromDriverToDestination = async () => {
              await MapService.getDistanceCarMethod(driverCoordinate, destinationCoordinate.coordinate as coordinate).then((res) => {
                setProcessMove(res.data.routes[0].legs[0].steps.map(index => index.maneuver.instruction))
                setPolyLine(res.data.routes[0].geometry.coordinates);
              })
            }
            setLineUpFromDriverToDestination()
          }
          else{
            setStateBooking(StateBooking.UPDATE);
            const setLineUpFromDriverToUser = async () => {
              if (bookingCarForm.bookingForm.typeCar.includes(TYPECAR.car)) {
                await MapService.getDistanceCarMethod(departureCoordinate.coordinate as coordinate, driverCoordinate).then((res) => {
                  setProcessMove(res.data.routes[0].legs[0].steps.map(index => index.maneuver.instruction))
                  setPolyLine(res.data.routes[0].geometry.coordinates);
                })
              }
              else{
                await MapService.getDistanceMotoMethod(departureCoordinate.coordinate as coordinate, driverCoordinate).then((res) => {
                  setProcessMove(res.data.routes[0].legs[0].steps.map(index => index.maneuver.instruction))
                  setPolyLine(res.data.routes[0].geometry.coordinates);
                })
              }
            };
            setLineUpFromDriverToUser()
          }
        }
        //FINISH
        else if (payloadFCM.body.toString().includes(BODYSTATES.FINISH_SUCCESS)) {
          setDriverCoordinate({longitude: undefined, latitude: undefined} as coordinate);
          setFinishSuccess(JSON.parse(payloadFCM.ride) as responseFinishedRide);
          setStateBooking(StateBooking.FINISH);
        }
        //DRIVER CANCEL
        else if (payloadFCM.body.toString().includes(BODYSTATES.CANCEL_DRIVER)) {
          setDriverCoordinate({longitude: undefined, latitude: undefined} as coordinate);
          setStateBooking(StateBooking.CANCELLEDBYDRIVER)
        }
        setPayloadFCMValue(payloadFCM);
      }
    }
    /*  }*/
  },[payloadFCM])

  useEffect(() => {
    const setLineUp = async () => {
      if (bookingCarForm.bookingForm.typeCar.includes(TYPECAR.car)) {
        await MapService.getDistanceCarMethod(departureCoordinate.coordinate as coordinate, destinationCoordinate.coordinate as coordinate).then((res) => {
          setProcessMove(res.data.routes[0].legs[0].steps.map(index => index.maneuver.instruction))
          setPolyLine(res.data.routes[0].geometry.coordinates);
        });
        await MapService.getDistanceMoto(departureCoordinate.coordinate as coordinate, destinationCoordinate.coordinate as coordinate).then(res =>console.log(res))
      }
      else{
        await MapService.getDistanceMotoMethod(departureCoordinate.coordinate as coordinate, destinationCoordinate.coordinate as coordinate).then((res) => {
          setProcessMove(res.data.routes[0].legs[0].steps.map(index => index.maneuver.instruction))
          setPolyLine(res.data.routes[0].geometry.coordinates);
        })
      }
    }
    setLineUp();
  }, []);

  useEffect(() => {
    let isMessage=true;
    if(isMessage===true) {
      const notification= MessageLoadMapService.getInstance({loading: 'đang tải map', loaded: 'Tải map thành công!'},loadMap);
    }
    return ()=>{
      isMessage=false;
    }
  }, [loadMap])
  const [viewState, setViewState] = useState({
    longitude: viewCoordinate?.longitude,
    latitude: viewCoordinate?.latitude,
    zoom: zoom,
  });
  const geoJsonPolyLine: GeoJSON.FeatureCollection<any> = {
    type: 'FeatureCollection',
    features: [
      {
        type: 'Feature',
        geometry: {
          type: 'LineString',
          coordinates: polyLine
        },
        properties: {}
      }
    ]
  };
  const mapRef = useRef(null);
  return <MainLayout>
    <ReactMapGL id="map"
                mapboxAccessToken={accessToken}
                mapStyle="mapbox://styles/mapbox/streets-v11"
                style={{
                  width: "80%",
                  height: "600px",
                  borderRadius: "15px",
                  border: `2px solid ${COLOR.MAIN}`,
                  position:'absolute'
                }}
                {...viewState}
                ref={mapRef}

                onClick={(evt) => {

                }}

                onLoad={(map) => {
                  setLoadMap(true)
                }}


                onZoom={evt => {
                }}
                onMove={evt => {
                  setViewState(evt.viewState)
                }}>


      <Source type='geojson' id='source-geojson' data={geoJsonPolyLine}>
        <Layer
          id="lineLayer1"
          type="line"
          source="route"
          layout={{
            "line-join": "round",
            "line-cap": "round"
          }}
          paint={{
            "line-color": "red",
            "line-width": 3
          }}
        />
      </Source>

      {showPopupDestination && (
        <Popup
          latitude={destinationCoordinate?.coordinate?.latitude as number}
          longitude={destinationCoordinate?.coordinate?.longitude as number}
          closeButton={true}
          closeOnClick={true}
          onClose={() => setShowPopupDestination(false)}
          anchor="top-right"
        >
          <div>Điểm đến</div>
        </Popup>
      )}

      {showPopupDeparture && (
        <Popup
          latitude={departureCoordinate?.coordinate?.latitude as number}
          longitude={departureCoordinate?.coordinate?.longitude as number}
          closeButton={true}
          closeOnClick={true}
          onClose={() => setShowPopupDeparture(false)}
          anchor="top-right"
        >
          <div>Điểm xuất phát</div>
        </Popup>
      )}

      {/*MARKER DRIVER*/}
      {
        (driverCoordinate?.latitude!==undefined&&driverCoordinate?.longitude!==undefined)?
        <Marker
          latitude={driverCoordinate?.latitude||undefined}
          longitude={driverCoordinate?.longitude||undefined}>
          <img
            onClick={() => setShowPopupDestination(true)}
            style={{height: 50, width: 50}}
            src="https://library.kissclipart.com/20180925/rpe/kissclipart-map-car-icon-clipart-car-google-maps-navigation-c81a6a2d0ecb7a15.png"
          />
        </Marker>:""
      }

      {/*MARKER DESTINATION*/}
      <Marker
        latitude={destinationCoordinate?.coordinate?.latitude}
        longitude={destinationCoordinate?.coordinate?.longitude}>
        <img
          onClick={() => setShowPopupDestination(true)}
          style={{height: 50, width: 50}}
          src="https://icon-library.com/images/marker-icon/marker-icon-26.jpg"
        />
      </Marker>

      {/*MARKER USER*/}
      {
        stateBooking.includes(StateBooking.DRIVERWELCOMEGUESTS)?
          ""
          :
          <Marker
            latitude={departureCoordinate?.coordinate?.latitude}
            longitude={departureCoordinate?.coordinate?.longitude}>
            <img
              onClick={() => setShowPopupDeparture(true)}
              style={{height: 50, width: 50}}
              src="https://icon-library.com/images/map-marker-icon/map-marker-icon-17.jpg"
            />
          </Marker>

      }
      <GeolocateControl position='top-right'/>
      <NavigationControl position='top-right'/>
      <ProcessMove location={locationBooking} drawer={()=>setVisibleState(prevState => !prevState)} processMove={processMove}/>
    </ReactMapGL>

    <Drawer
      placement='bottom'
      closable={false}
      onClose={onCloseDrawer}
      visible={visibleState}
    >

      <Descriptions title="Trạng thái đặt xe"  bordered>
        <Descriptions.Item label="Số điện thoại">{bookingCarForm.bookingForm.phonenumber}</Descriptions.Item>
        <Descriptions.Item label="Loại xe">{bookingCarForm.bookingForm.typeCar}</Descriptions.Item>
        <Descriptions.Item label="Id Chuyến xe">{bookingCarForm.bookingForm.id}</Descriptions.Item>
        <Descriptions.Item label="Thời gian đặt xe">{bookingCarForm.bookingForm.createdAt}</Descriptions.Item>
        <Descriptions.Item label="Thời gian tài xe nhận" >
          2019-04-24 18:00:00
        </Descriptions.Item>
        <Descriptions.Item label="Thời gian kết thúc chuyến đi" >
          {finishSuccess.endTime}
        </Descriptions.Item>
        <Descriptions.Item label="Địa chỉ đón khách">{bookingCarForm.departure.value}</Descriptions.Item>
        <Descriptions.Item label="Địa chỉ khách đến" span={2}>{bookingCarForm.destination.value}</Descriptions.Item>
        <Descriptions.Item label="Trạng thái" span={3}>
          {StateBooking.FINISH.toString().includes(stateBooking)?
            <Badge status="success" text={stateBooking} />:
            StateBooking.CANCELLEDBYDRIVER.toString().includes(stateBooking)?
              <Badge status="error" text={stateBooking} />
              :
              <Badge status="processing" text={stateBooking} />
          }
        </Descriptions.Item>
        <Descriptions.Item label="Phương thức thanh toán">{bookingCarForm.bookingForm.paymentMethod}</Descriptions.Item>
        <Descriptions.Item label="Giá tiền">{handlePrice(bookingCarForm.bookingForm.price)}</Descriptions.Item>
      </Descriptions>
    </Drawer>

  </MainLayout>
}


export default connector(Map)