import React from "react"
import MainLayout from "src/layouts/MainLayout"
import {TableContainer} from "../Product/ProductList/ProductList.styles";
import {handlePrice} from "../../helpers/string";
import {Link} from "react-router-dom";
import {PATH} from "../../constants/paths";
import {Space, Table, Tag } from "antd";
import type {ColumnsType} from 'antd/es/table';
import Search from "antd/lib/input/Search";

interface DataType {
  key: string;
  name: string;
  age: number;
  address: string;
}

const columns: ColumnsType<DataType> = [
  {
    title: 'Name',
    dataIndex: 'name',
    key: 'name',
    render: text => <a>{text}</a>,
  },
  {
    title: 'Age',
    dataIndex: 'age',
    key: 'age',
  },
  {
    title: 'Address',
    dataIndex: 'address',
    key: 'address',
  },
];

const data: DataType[] = [
  {
    key: '1',
    name: 'John Brown',
    age: 32,
    address: 'New York No. 1 Lake Park',
  },
  {
    key: '2',
    name: 'Jim Green',
    age: 42,
    address: 'London No. 1 Lake Park',
  },
  {
    key: '3',
    name: 'Joe Black',
    age: 32,
    address: 'Sidney No. 1 Lake Park',
  },
];

export default function UserInfo() {
  const onSearch=()=>{

  }
  return (
    <MainLayout>
      <Search placeholder="input search text" enterButton="Search" size="large" loading onSearch={onSearch} />
      <Table columns={columns} dataSource={data} />
    </MainLayout>
  )
}
