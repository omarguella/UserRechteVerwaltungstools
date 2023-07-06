import React, { FC } from "react";
import * as CustomForm from "./style.d";

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {}

const Input: FC<InputProps> = props => {
  return <CustomForm.Input {...props} />;
};

export default Input;
