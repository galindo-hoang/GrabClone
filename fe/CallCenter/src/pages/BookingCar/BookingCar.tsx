import React from "react"
import MainLayout from "src/layouts/MainLayout"
import {Title} from "../BookingCar/BookingCar.styles";
export default function BookingCar() {
  return (
    <MainLayout>
      <div className="container">
        <div className="min-vh-30 row">
          <div className="col-md-6 m-auto">
            <form className="p-5 rounded-sm shadow text-center">
              <Title>Đặt xe</Title>
              <p className="text-muted">Vui lòng điền đầy đủ thông tin </p>
              <label className="float-left mb-1">Họ và tên</label>
              <input
                type="text"
                placeholder="Điền đầy đủ họ tên"
                className="form-control form-control-lg mb-3"
              />
              <label className="float-left mb-1">Số điện thoại</label>
              <input
                type="text"
                placeholder="Điền số điện thoại"
                className="form-control form-control-lg mb-3"
              />
              <label className="float-left mb-1">Địa chỉ</label>
              <input
                type="text"
                placeholder="Điền địa chỉ"
                className="form-control form-control-lg mb-3"
              />
              <label className="float-left mb-1">Ghi chú</label>
              <input
                type="text"
                placeholder="Điền ghi chú"
                className="form-control form-control-lg mb-3"
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
