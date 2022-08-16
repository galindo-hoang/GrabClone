import React, {useEffect, useState} from "react"
import MainLayout from "src/layouts/MainLayout"
import {Title} from "../BookingCar/BookingCar.styles";
import {PATH} from "../../constants/paths";
import {useHistory} from "react-router-dom";
import {MessageWarningService} from "src/service/Message/MessageService";
import "antd/dist/antd.css";
import {AutoComplete, Button, Modal, Table} from 'antd';
import {connect, ConnectedProps} from "react-redux"
import {bookingCar} from "./BookingCar.thunks";
import {featuresLocation, info2Location, timestamp} from "../../@types/bookingcar";
import BookingService from "../../service/BookingCar/BookingService";
import {coordinate} from "../../@types/map";
import {recentPhoneNumber} from "../../@types/bookingcar";
import {Col, Row} from "react-bootstrap";
import type {ColumnsType} from 'antd/es/table';
import {useDebounce} from "../../hooks/useDebounce";
import {
  addPhoneRecent,
  convertDateFireBase,
  databaseFireBase,
  notification
} from "../../service/FireBase/FirebaseService";
import {collection, getDocs,addDoc} from 'firebase/firestore'
import firebase from "firebase/compat";

const accessToken = "pk.eyJ1IjoicGhhbXRpZW5xdWFuIiwiYSI6ImNsNXFvb2h3ejB3NGMza28zYWx2enoyem4ifQ.v-O4lWtgCXbhJbPt5nPFIQ";
const mapStateToProps = state => ({})

const mapDispatchToProps = {
  bookingCar
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
  const {bookingCar} = props
  const [fullName, setFullName] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [carType, setCarType] = useState("");
  const [departure, setDeparture] = useState<featuresLocation>({
    value: undefined,
    coordinate: undefined,
  });
  const userCollection = collection(databaseFireBase , "HistoryPhoneNumber");
  const [recentPhoneNumber, setRecentPhoneNumber] = useState<object[]>([]);
  useEffect(() => {
    const getData = async () => {
      const data = await getDocs(userCollection);
      const array=data.docs.map((doc)=>{
        const arr:recentPhoneNumber={
          date: convertDateFireBase(doc.data()?.date),
          phonenumber:doc.data()?.phonenumber
        }
        return arr
      });
      setRecentPhoneNumber(array)
    }
    getData()
  }, []);
  console.log(recentPhoneNumber)
  const [departureAutocomplete, setDepartureAutocomplete] = useState<{ value: string, coordinate: coordinate }[]>([]);

  const [destination, setDestination] = useState<featuresLocation>({
    value: undefined,
    coordinate: undefined,
  });
  const [destinationAutocomplete, setDestinationAutocomplete] = useState<{ value: string, coordinate: coordinate }[]>([]);
  const [visibleDesination, setVisibleDesination] = useState(false)
  const [visibleDeparture, setVisibleDeparture] = useState(false)
  const [note, setNote] = useState("");
  const history = useHistory();
  const debounceDestination = useDebounce(destination.value, 500)
  const debounceDeparture = useDebounce(departure.value, 500)
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

  useEffect(() => {
    notification();
  }, [])
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


  const onChangeFullName = (event) => {
    setFullName(event?.target?.value)
  }
  const onChangePhoneNumber = (event) => {
    setPhoneNumber(event?.target?.value)
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
  const onChangeNote = (event) => {
    setNote(event?.target?.value)
  }
  const onChangeCarType = (event) => {
    setCarType(event?.target?.value)
  }
  const submit = event => {
  }

  return (
    <MainLayout>
      <div className="container">
        <div className="min-vh-30 row">
          <div className="col-md-6 m-auto">
            <div className="p-5 rounded-sm shadow text-center info-background">
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
              <label className="float-left mb-1">Loại xe</label>
              <input
                type="text"
                placeholder="Điền loại xe"
                className="form-control form-control-lg mb-3"
                onChange={onChangeCarType}
              />
              <Row>
                <Col xs lg md sm="12">
                  <label className="float-left mb-1">Địa chỉ đón</label>
                </Col>
              </Row>
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
                  <button className="btn  btn-info btn-md" onClick={() => setVisibleDeparture(true)}>
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
                  <button className="btn  btn-info btn-md" onClick={() => setVisibleDesination(true)}>
                    Lịch sử đến
                  </button>
                </Col>
              </Row>
              <label className="float-left mb-1">Ghi chú</label>
              <input
                type="text"
                placeholder="Điền ghi chú"
                className="form-control form-control-lg mb-3"
                onChange={onChangeNote}
              />
              <button type="submit" className="btn btn-block btn-info btn-lg" onClick={() => {
                if (isBlank(destination.value as string) === false && isBlank(departure.value as string) === false) {
                  const position: info2Location = {
                    departure: departure,
                    destination: destination
                  }
                  bookingCar(position)
                  // const recentPhoneNumber:recentPhoneNumber={
                  //   date:convertDateFireBase({seconds:new Date().getTime() / 1000,nanoseconds:} as timestamp),
                  //   phonenumber:phoneNumber
                  // }
                  // addPhoneRecent(collection(databaseFireBase , "HistoryPhoneNumber"),recentPhoneNumber)
                  history.push(PATH.MAP);
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
        <p>some contents...</p>
        <p>some contents...</p>
        <p>some contents...</p>
      </Modal>
      <Modal
        title="Lịch sử đến"
        centered
        visible={visibleDesination}
        onOk={() => setVisibleDesination(false)}
        onCancel={() => setVisibleDesination(false)}
        width={1000}
      >
        <HistoryItem/>
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

const data: DataType[] = [];
for (let i = 0; i < 5; i++) {
  data.push({
    key: i,
    phoneNumber: `0935955314`,
    address: "Bà Hom",
    dateTime: `17-01-2022`,
  });
}

const HistoryItem = () => {
  const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
  const [loading, setLoading] = useState(false);

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
