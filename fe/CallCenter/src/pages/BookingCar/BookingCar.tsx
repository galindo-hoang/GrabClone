import React, {useEffect, useState} from "react"
import MainLayout from "src/layouts/MainLayout"
import {Title} from "../BookingCar/BookingCar.styles";
import {PATH} from "../../constants/paths";
import {useHistory} from "react-router-dom";
import {MessageWarningService} from "src/service/Message/MessageService";
import "antd/dist/antd.css";
import {AutoComplete, Button, Dropdown, Menu, MenuProps, Modal, Space, Table} from 'antd';
import {connect, ConnectedProps} from "react-redux"
import {createBookingCar, saveAddressBooking} from "./BookingCar.thunks";
import {bookingCarForm, createBooking, featuresLocation, info2Location, timestamp} from "../../@types/bookingcar";
import BookingService from "../../service/BookingCar/BookingService";
import {coordinate} from "../../@types/map";
import {recentPhoneNumber} from "../../@types/bookingcar";
import {Col, Row} from "react-bootstrap";
import type {ColumnsType} from 'antd/es/table';
import {useDebounce} from "../../hooks/useDebounce";
import {
  addPhoneRecent,
  convertDateFireBase,
  databaseFireBase, getPhoneNumber,
  registerNotification
} from "../../service/FireBase/FirebaseService";
import {collection, getDocs,addDoc} from 'firebase/firestore'
import firebase from "firebase/compat";
import DownOutlined from "@ant-design/icons/lib/icons/DownOutlined";
import ProcessBookingService from "../../service/BookingCar/ProcessBookingService";
import { coordinateExample } from "src/apis/product.api";

const accessToken = "pk.eyJ1IjoicGhhbXRpZW5xdWFuIiwiYSI6ImNsNXFvb2h3ejB3NGMza28zYWx2enoyem4ifQ.v-O4lWtgCXbhJbPt5nPFIQ";
const mapStateToProps = state => ({})

const mapDispatchToProps = {
  saveAddressBooking,
  createBookingCar
}

const isBlank = (index: string) => {
  if (index === undefined) {
    return true;
  }
  if (index === '') {
    return true;
  }
  return false;
}
const connector = connect(mapStateToProps, mapDispatchToProps)
interface Props extends ConnectedProps<typeof connector> {
}



const BookingCar = (props: Props) => {
  const {saveAddressBooking,createBookingCar} = props
  const [carType, setCarType] = useState("Chọn loại xe");
  const userCollection = collection(databaseFireBase , "HistoryPhoneNumber");
  const [recentPhoneNumber, setRecentPhoneNumber] = useState<object[]>([]);
  const [phoneNumber, setPhoneNumber] = useState<string>("");
  useEffect(() => {
      const getData = async () => {
          const data = await getDocs(userCollection);
          const array = data.docs.map((doc) => {
            const arr: recentPhoneNumber = {
              date: convertDateFireBase(doc.data()?.date),
              phonenumber: doc.data()?.phonenumber
            }
            return arr
          });
          setRecentPhoneNumber(getPhoneNumber(array));
        }
      getData();
    return (()=>{
    })
  },[]);

  const [departure, setDeparture] = useState<featuresLocation>({
    value: undefined,
    coordinate: undefined,
  });
  const [departureAutocomplete, setDepartureAutocomplete] = useState<{ value: string, coordinate: coordinate }[]>([]);
  const [destination, setDestination] = useState<featuresLocation>({
    value: undefined,
    coordinate: undefined,
  });
  const [destinationAutocomplete, setDestinationAutocomplete] = useState<{ value: string, coordinate: coordinate }[]>([]);
  const [visibleDesination, setVisibleDesination] = useState(false)
  const [visibleDeparture, setVisibleDeparture] = useState(false)
  const [dropdownTypeCar,setDropdownTypeCar]=useState<any>();
  const history = useHistory();
  const debounceDestination = useDebounce(destination.value, 500)
  const debounceDeparture = useDebounce(departure.value, 500);
  const [dataDesinationMost,setDataDestinationMost]=useState<featuresLocation[]>([]);
  const [dataDepartureMost,setDataDepartureMost]=useState<featuresLocation[]>([]);
  const deboundTop5AddressMost = useDebounce(phoneNumber, 500)

  useEffect(() => {
    let isApi = true;
    const getAutoCompleteDeparture = async () => {
      if (debounceDeparture) {
        await BookingService.autoComplete(departure.value as string).then(res => {
          const data = (res.data.features.map((index) => {
            const data: featuresLocation = {
              coordinate: {
                longitude: index.geometry.coordinates[0],
                latitude: index.geometry.coordinates[1],
              },
              value: index.properties.formatted
            }
            return data;
          }))
          setDepartureAutocomplete(data)
        })
      } else {
        setDepartureAutocomplete([])
      }
    }
    getAutoCompleteDeparture()
    return (() => {
      isApi = false;
    })
  }, [departure.value])

  useEffect(() => {
    let isApi = true;
    const getAutoCompleteDestination = async () => {
      if (debounceDestination) {
        await BookingService.autoComplete(destination.value as string).then(res => {
          const data = (res.data.features.map((index) => {
            const data: featuresLocation = {
              coordinate: {
                longitude: index.geometry.coordinates[0],
                latitude: index.geometry.coordinates[1],
              },
              value: index.properties.formatted
            }
            return data;
          }));
          setDestinationAutocomplete(data)
        })
      } else {
        setDestinationAutocomplete([])
      }
    }
    getAutoCompleteDestination()
    return (() => {
      isApi = false;
    })
  }, [destination.value]);
  useEffect(()=>{
    const menu = (
      <Menu
        onClick={onClick}
        items={[
          {
            key: 'MOTORCYCLE',
            label: (
              <a target="_blank"  >
                MOTORCYCLE
              </a>
            ),
          },
          {
            key: 'CAR',
            label: (
              <a target="_blank" >
                CAR
              </a>
            ),
          },
        ]}
      />
    );
    setDropdownTypeCar(menu)
  },[]);
  const onClick: MenuProps['onClick'] = ({ key }) => {
    setCarType(key);
  };
  const onSelectedPhoneNumber=(phoneNumber)=>{
    const temp=recentPhoneNumber.filter((index:recentPhoneNumber)=> {
      if(index.phonenumber === phoneNumber){
        return index.phonenumber as string;
      }
    }) as recentPhoneNumber
    setPhoneNumber(temp[0]?.phonenumber)
  }
  useEffect(()=>{
    console.log(phoneNumber)
    const listAddressDestination:featuresLocation[]=[];
    const listAddressDeparture:featuresLocation[]=[];
    if(deboundTop5AddressMost) {
      const get5AddressMost = async () => {
        coordinateExample.forEach(async index=>{
          await BookingService.convertCoordinateToAddress(index).then(response => {
            listAddressDeparture.push({value:response.data.features[0].properties.formatted,coordinate:{
                longitude:response.data.features[0].geometry.coordinates[1],
                latitude:response.data.features[0].geometry.coordinates[0]
              }});
            listAddressDestination.push({value:response.data.features[0].properties.formatted,coordinate:{
                longitude:response.data.features[0].geometry.coordinates[1],
                latitude:response.data.features[0].geometry.coordinates[0]
              }})
          })
        })
      }
      get5AddressMost();
      setDataDepartureMost(listAddressDeparture);
      setDataDestinationMost(listAddressDestination)
    }
  },[deboundTop5AddressMost])
  const onSelectedDeparture = (address) => {
    const temp: { value: string; coordinate: coordinate }[] | undefined = departureAutocomplete?.filter(index => (
      index.value === address
    ));
    setDeparture({
      value: temp[0].value,
      coordinate: temp[0].coordinate
    })
  }
  const onSelectedDestination = (address) => {
    const temp: { value: string; coordinate: coordinate }[] | undefined = destinationAutocomplete?.filter(index => (
      index.value === address
    ));
    setDestination({
      value: temp[0].value,
      coordinate: temp[0].coordinate
    })
  }

  const onChangePhoneNumber = (data) => {

    setPhoneNumber(data)
  }
  const onChangeDeparture = (data) => {
    setDeparture({
      value: data
    })
  }
  const onChangeDestination = (data) => {
    setDestination({
      value: data
    });
  }

  return (
    <MainLayout>
      <div className="container">
        <div className="min-vh-30 row">
          <div className="col-md-6 m-auto">
            <div className="p-5 rounded-sm shadow text-center info-background">
              <Title>Đặt xe</Title>
              <p className="text-muted">Vui lòng điền đầy đủ thông tin </p>
              <label className="float-left mb-1">Số điện thoại</label>
              <AutoComplete
                className="form-control form-control-lg mb-3"
                options={recentPhoneNumber.map((index:recentPhoneNumber)=>{const object= {
                  value:index.phonenumber
                }
                return object
                })
                }
                value={phoneNumber}
                onChange={onChangePhoneNumber}
                onSelect={onSelectedPhoneNumber}
                placeholder="Điền số điện thoại"
              />
              <Row>
                <Col xs lg md sm="12">
              <label className="float-left mb-1">Loại xe</label>
                </Col>
              </Row>
              <Row style={{position: "relative", width: "500px"}}>
                <Col xs lg md sm="12">
                  <Dropdown.Button
                    className="float-left mb-1 "
                    icon={<DownOutlined />}
                    overlay={dropdownTypeCar}
                  >
                    {carType}
                  </Dropdown.Button>
                </Col>
              </Row>
              <Row>
                <Col xs lg md sm="12">
                  <label className="float-left mb-1">Địa chỉ đón</label>
                </Col>
              </Row>
             {/* <Row style={{position: "relative"}} gutter={{ xs: 8, sm: 16, md: 24, lg: 32 }}>
                <Col className="gutter-row" span={16}>
                  <AutoComplete
                    className="form-control form-control-lg mb-3"
                    options={departureAutocomplete}
                    value={departure.value}
                    onChange={onChangeDeparture}
                    onSelect={onSelectedDeparture}
                    placeholder="Điền địa chỉ đón"
                  />
                </Col>
                <Col className="gutter-row" span={8}>
                  <button className="btn  btn-info btn-md" onClick={() => setVisibleDeparture(true)}>
                    Lịch sử đón
                  </button>
                </Col>
              </Row>*/}
              <Row style={{position: "relative", width: "500px"}}>
                <Col xs lg md sm="8">
                  <AutoComplete
                    className="form-control form-control-lg mb-3"
                    options={departureAutocomplete}
                    value={departure.value}
                    onChange={onChangeDeparture}
                    onSelect={onSelectedDeparture}
                    placeholder="Điền địa chỉ đón"
                    style={{
                      width: "345px"
                    }}
                  />
                </Col>
                <Col xs lg md sm="4" className="">
                  <button className="btn  btn-info btn-md" onClick={() =>{
                    if(dataDepartureMost.length===5)
                    {
                      setVisibleDeparture(true)
                    }
                    else{
                      MessageWarningService.getInstance("Đang load")
                    }
                  }}>
                    Lịch sử đón
                  </button>
                </Col>
              </Row>
              <Row>
                <Col xs lg md sm="12">
                  <label className="float-left mb-1">Địa chỉ đến</label>
                </Col>
              </Row>
              <Row style={{position: "relative", width: "500px"}}>
                <Col xs lg md sm="8">
                  <AutoComplete
                    className="form-control form-control-lg mb-3"
                    options={destinationAutocomplete}
                    value={destination.value}
                    onChange={onChangeDestination}
                    onSelect={onSelectedDestination}
                    placeholder="Điền địa chỉ đến"
                    style={{
                      width: "345px"
                    }}
                  />
                </Col>
                <Col xs lg md sm="4" className="">
                  <button className="btn  btn-info btn-md" onClick={() => {
                    if(dataDesinationMost.length===5)
                    {
                      setVisibleDesination(true)
                    }
                    else{
                      MessageWarningService.getInstance("Đang load")
                    }

                  }}>
                    Lịch sử đến
                  </button>
                </Col>
              </Row>
              <button type="submit" className="btn btn-block btn-info btn-lg" onClick={() => {
                if (isBlank(destination.value as string) === false && isBlank(departure.value as string) === false
                  && isBlank(phoneNumber as string) === false && isBlank(carType as string) === false) {
                  const position: info2Location = {
                    departure: departure,
                    destination: destination
                  }
                  const bookingCarForm:bookingCarForm={
                    address:position,
                    typeCar:carType,
                    phoneNumber:phoneNumber,
                  }
                  const createBooking:createBooking={
                    phonenumber:phoneNumber,
                    username:localStorage.getItem("userName") as string,
                    typeCar:carType,
                    dropoffLocation:{
                      longitude:position.destination?.coordinate?.longitude,
                      latitude:position.destination?.coordinate?.latitude
                    },
                    pickupLocation:{
                      longitude:position.departure?.coordinate?.longitude,
                      latitude:position.departure?.coordinate?.latitude
                    },
                    paymentMethod:"CREDIT_CARD",
                    price:100000
                  }
                  saveAddressBooking(bookingCarForm as bookingCarForm);
                  createBookingCar(createBooking as createBooking)
                  const recentPhoneNumber:recentPhoneNumber={
                    date:firebase.firestore.Timestamp.fromDate(new Date()),
                    phonenumber:phoneNumber
                  }
                  addPhoneRecent(collection(databaseFireBase , "HistoryPhoneNumber"),recentPhoneNumber);
                  history.push(PATH.MAP)
                } else {
                  const message = MessageWarningService.getInstance("Vui lòng điền đầy đủ thông tin")
                }
              }}>
                Xác nhận
              </button>
            </div>
          </div>
        </div>
      </div>
      <Modal
        title="Lịch sử đón"
        centered
        visible={visibleDeparture}
        onOk={() => setVisibleDeparture(false)}
        onCancel={() => setVisibleDeparture(false)}
        width={1000}
      >
        <HistoryItem dataDeparture={dataDepartureMost} setDeparture={(index)=>setDeparture(index)}/>
      </Modal>
      <Modal
        title="Lịch sử đến"
        centered
        visible={visibleDesination}
        onOk={() => setVisibleDesination(false)}
        onCancel={() => setVisibleDesination(false)}
        width={1000}
      >
        <HistoryItem dataDestination={dataDesinationMost} setDestination={(index)=>setDestination(index)}/>
      </Modal>
    </MainLayout>
  )
}


interface DataType {
  key: React.Key;
  phoneNumber: string;
  address: string;
  dateTime: string;
}



const HistoryItem = (props:any) => {
  const {dataDeparture ,dataDestination,setDeparture,setDestination}=props;
  const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
  const [loading, setLoading] = useState(false);
  const data: DataType[] = [];
  if(dataDestination===undefined) {
    dataDeparture.forEach((index) => {
      data.push({
        key: 1,
        phoneNumber: `0935955314`,
        address: index.value.toString(),
        dateTime: `17-01-2022`,
      });
    })
  }
  else
  {
    dataDestination.forEach((index) => {
      data.push({
        key: 2,
        phoneNumber: `0935955314`,
        address: index.value.toString(),
        dateTime: `17-01-2022`,
      });
    })
  }
  const columns: ColumnsType<DataType> = [
    {
      title: 'Số điện thoại',
      dataIndex: 'phoneNumber',
      render: text => <a>{text}</a>,
    },
    {
      title: 'Địa chỉ',
      dataIndex: 'address',
      render: text => <a style={{color: 'blue', textDecorationLine: "underline"}}>{text}</a>,
    },
    {
      title: 'Thời gian đặt',
      dataIndex: 'dateTime',
      render: text => <a>{text}</a>,
    },
  ];

  const start = () => {
    setLoading(true);
    setTimeout(() => {
      setSelectedRowKeys([]);
      setLoading(false);
    }, 1000);
  };

  const onSelectChange = (newSelectedRowKeys: React.Key[]) => {
    setSelectedRowKeys(newSelectedRowKeys);
  };


  const rowSelection = {
    selectedRowKeys,
    onChange: onSelectChange,
  };
  const hasSelected = selectedRowKeys.length > 0;

  return (
    <div>
      <div style={{marginBottom: 16}}>
        <Button type="primary" onClick={start} disabled={!hasSelected} loading={loading}>
          Reload
        </Button>
        <span style={{marginLeft: 8}}>
          {hasSelected ? `Selected ${selectedRowKeys.length} items` : ''}
        </span>
      </div>
      <Table rowSelection={rowSelection} columns={columns} dataSource={data}/>
    </div>
  );
};

export default connector(BookingCar)
