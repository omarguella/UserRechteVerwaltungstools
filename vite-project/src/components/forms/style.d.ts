import styled from "styled-components";
import * as Antd from "antd";
export const Input = styled(Antd.Input)`
  border-radius: 5px;
  height: 50px;
  &:focus {
    border-width: 1px;
  }
`;

export const Password = styled(Antd.Input.Password)`
  border-radius: 5px;
  height: 50px;
  &:focus {
    border-width: 1px;
  }
`;

export const Select = styled(Antd.Select)`
  .ant-select-selector {
    height: 50px !important;
  }
  span.ant-select-selection-item {
    display: flex;
    align-items: center;
  }
  &:focus {
    border-width: 1px;
  }
`;

export const Button = styled(Antd.Button)`
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: fit-content;
`;
