import { Spin } from "antd";
import styled from "styled-components";

function Loading() {
  return (
    <SpinnerContainer>
      <Spin spinning={true} />
    </SpinnerContainer>
  );
}

const SpinnerContainer = styled.div`
  height: 100%;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
`;

export default Loading;
