import React from "react"
import { NavLink } from "react-router-dom"
import { Footer, Logo, Menu, Nav } from "./SideNav.styles"
import { PATH } from "src/constants/paths"
import home from "src/assets/images/home.svg"
import list from "src/assets/images/list.svg"
import call from "src/assets/images/call.svg"
import { connect, ConnectedProps } from "react-redux"
import AuthenticatedGuard from "src/guards/AuthenticatedGuard"
import {clearFCM} from "../../App/App.thunk";

const mapStateToProps = state => ({
  closeSideNav: state.app.closeSideNav
})

const mapDispatchToProps = {
  clearFCM
}

const connector = connect(mapStateToProps, mapDispatchToProps)

interface Props extends ConnectedProps<typeof connector> {}

const getAccessToken=()=>{
  return localStorage.getItem("accessToken");
}
function SideNav(props: Props) {
  const {closeSideNav,clearFCM} = props
  return (
    <Nav className={closeSideNav ? "close" : ""}>
      <NavLink  exact to={getAccessToken()?PATH.HOME:PATH.LOGIN}>
      </NavLink>
      <Menu className="list-unstyled mb-5 mt-3 ">
        <li>
          <NavLink exact to={getAccessToken()?PATH.HOME:PATH.LOGIN}>
            <img src={home} alt=""/>
            <span>Trang chủ</span>
          </NavLink>
        </li>
        <li>
          <NavLink exact to={getAccessToken()?PATH.PRODUCT:PATH.LOGIN}>
            <img src={list} alt=""/>
            <span>Sản phẩm</span>
          </NavLink>
        </li>
        <li>
          <NavLink exact to={getAccessToken()?PATH.BOOKINGCAR:PATH.LOGIN} onClick={()=>clearFCM()}>
            <img src={call} alt=""/>
            <span>Đặt xe</span>
          </NavLink>
        </li>
        <li>
          <NavLink exact to={getAccessToken()?PATH.USERINFO:PATH.LOGIN}>
            <img src={list} alt=""/>
            <span>Thông tin tài xế</span>
          </NavLink>
        </li>
      </Menu>
    </Nav>
  )
}

export default connector(SideNav)
