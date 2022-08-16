import { COLOR } from "src/constants/styles"
import styled from "styled-components"

export const LogoutIcon = styled.span`
  &.active{
    background: ${COLOR.INFO};
    color:black
  }
  &.hover {
    background: ${COLOR.INFO};
    color:black
  }
`

export const button=styled.button`
  &.active {
        background: ${COLOR.MAIN};
    }
`


