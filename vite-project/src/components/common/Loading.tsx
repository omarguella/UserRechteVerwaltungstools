import { Spin } from "antd";
import styled from "styled-components";

function Loading() {
  return (
    <SpinnerContainer>
      <svg
        xmlns="http://www.w3.org/2000/svg"
        width="50px"
        height="50px"
        viewBox="0 0 100 100"
        overflow="visible"
        fill="#343434"
        stroke="none"
      >
        <defs>
          <rect
            id="loader"
            x="46.5"
            y="40"
            width="7"
            height="20"
            rx="2"
            ry="2"
            transform="translate(0 -30)"
          />
        </defs>
        <use xlinkHref="#loader" transform="rotate(45 50 50)">
          <animate
            attributeName="opacity"
            values="0;1;0"
            dur="1s"
            begin="0.13s"
            repeatCount="indefinite"
          ></animate>
        </use>
        <use xlinkHref="#loader" transform="rotate(90 50 50)">
          <animate
            attributeName="opacity"
            values="0;1;0"
            dur="1s"
            begin="0.25s"
            repeatCount="indefinite"
          ></animate>
        </use>
        <use xlinkHref="#loader" transform="rotate(135 50 50)">
          <animate
            attributeName="opacity"
            values="0;1;0"
            dur="1s"
            begin="0.38s"
            repeatCount="indefinite"
          ></animate>
        </use>
        <use xlinkHref="#loader" transform="rotate(180 50 50)">
          <animate
            attributeName="opacity"
            values="0;1;0"
            dur="1s"
            begin="0.50s"
            repeatCount="indefinite"
          ></animate>
        </use>
        <use xlinkHref="#loader" transform="rotate(225 50 50)">
          <animate
            attributeName="opacity"
            values="0;1;0"
            dur="1s"
            begin="0.63s"
            repeatCount="indefinite"
          ></animate>
        </use>
        <use xlinkHref="#loader" transform="rotate(270 50 50)">
          <animate
            attributeName="opacity"
            values="0;1;0"
            dur="1s"
            begin="0.75s"
            repeatCount="indefinite"
          ></animate>
        </use>
        <use xlinkHref="#loader" transform="rotate(315 50 50)">
          <animate
            attributeName="opacity"
            values="0;1;0"
            dur="1s"
            begin="0.88s"
            repeatCount="indefinite"
          ></animate>
        </use>
        <use xlinkHref="#loader" transform="rotate(360 50 50)">
          <animate
            attributeName="opacity"
            values="0;1;0"
            dur="1s"
            begin="1.00s"
            repeatCount="indefinite"
          ></animate>
        </use>
      </svg>
    </SpinnerContainer>
  );
}

const SpinnerContainer = styled.div`
  height: 100vh;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
`;

export default Loading;
