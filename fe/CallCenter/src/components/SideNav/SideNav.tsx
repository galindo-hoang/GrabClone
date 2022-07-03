import React from "react"
import { NavLink } from "react-router-dom"
import { Footer, Logo, Menu, Nav } from "./SideNav.styles"
import { PATH } from "src/constants/paths"
import home from "src/assets/images/home.svg"
import list from "src/assets/images/list.svg"
import call from "src/assets/images/call.svg"
import { connect, ConnectedProps } from "react-redux"

const mapStateToProps = state => ({
  closeSideNav: state.app.closeSideNav
})

const mapDispatchToProps = {}

const connector = connect(mapStateToProps, mapDispatchToProps)

interface Props extends ConnectedProps<typeof connector> {}

function SideNav(props: Props) {
  const {closeSideNav} = props
  return (
    <Nav className={closeSideNav ? "close" : ""}>
      <NavLink exact to={PATH.HOME}>
      </NavLink>
      <Menu className="list-unstyled mb-5 mt-3">
        <li>
          <NavLink exact to={PATH.HOME}>
            <img src={home} alt=""/>
            <span>Trang chủ</span>
          </NavLink>
        </li>
        <li>
          <NavLink to={PATH.PRODUCT}>
            <img src={list} alt=""/>
            <span>Sản phẩm</span>
          </NavLink>
        </li>
        <li>
          <NavLink to={PATH.BOOKINGCAR}>
            <img src={call} alt=""/>
            <span>Đặt xe</span>
          </NavLink>
        </li>
        <li>
          <NavLink to={PATH.HISTORY}>
            <img src={list} alt=""/>
            <span>Lịch sử đặt xe</span>
          </NavLink>
        </li>
      </Menu>
    </Nav>
  )
}

export default connector(SideNav)
