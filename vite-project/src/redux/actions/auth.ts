import { createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";
import { TLogin, TRegistration } from "../../types/auth";

// Login
export const LoginAction = createAsyncThunk(
  "auth/login",
  async (data: TLogin, { rejectWithValue }) => {
    try {
      const response = await axios.post("http://localhost:9000/auth/login", data);
      return response.data;
    } catch (error) {
      return rejectWithValue("Opps error ocurred try again");
    }
  }
);

// Registration
export const RegisterAction = createAsyncThunk(
  "auth/registration",
  async (data: TRegistration, { rejectWithValue }) => {
    const { role, ...rest } = data;
    try {
      const response = await axios.post(`http://localhost:9000/auth/registration/${role}`, rest);
      return response.data;
    } catch (error) {
      return rejectWithValue("Opps error ocurred try again");
    }
  }
);
