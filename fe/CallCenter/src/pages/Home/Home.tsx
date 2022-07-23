import React from "react"
import MainLayout from "src/layouts/MainLayout"
import { connect, ConnectedProps } from "react-redux"
import LoginService from "src/service/Login/LoginService";

const mapStateToProps = state => ({
  loading:state.productItem.loading,
  info:state.login.clientInformation,
  loding2:state.home.loading
})

const mapDispatchToProps = {}


const connector = connect(mapStateToProps, mapDispatchToProps)
interface Props extends ConnectedProps<typeof connector> {}
const Home = (props: Props) => {
  const {loading,loding2,info}= props

  const testToken= () =>{
    LoginService.getListUser(localStorage.getItem("accessToken")||"");
  }
  return (
    <MainLayout>
      <h2 className="mb-4">Home</h2>
      <button onClick={testToken}>Test Token</button>
    </MainLayout>
  )
}



export default connector(Home)