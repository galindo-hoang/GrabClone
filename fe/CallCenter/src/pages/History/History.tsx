import React from "react"
import MainLayout from "src/layouts/MainLayout"
import {TableContainer} from "../Product/ProductList/ProductList.styles";
import {handlePrice} from "../../helpers/string";
import {Link} from "react-router-dom";
import {PATH} from "../../constants/paths";
export default function History() {
  return (
    <MainLayout>
      <h2>Lịch sử đặt xe</h2>
      <TableContainer>
        <table className="table table-striped">
          <thead>
          <tr>
            <th>#</th>
            <th>Thời điểm đặt</th>
            <th>Khách hàng</th>
            <th>Điểm đón</th>
            <th>Trạng thái</th>
            <th>Tài xế</th>
            <th>Số điện thoại</th>
          </tr>
          </thead>
          <tbody>
          </tbody>
        </table>
      </TableContainer>
    </MainLayout>
  )
}
