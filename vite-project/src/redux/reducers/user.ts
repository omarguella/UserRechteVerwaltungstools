import { createSlice } from "@reduxjs/toolkit";
import { User } from "../../types/user";
import { GetUserAction } from "../actions/user";

interface InitialState {
  update: {
    data: User | null;
    loading: boolean;
    modal: boolean;
  };
  create: {
    modal: boolean;
    loading: boolean;
  };
}
const initialState: InitialState = {
  update: {
    data: null,
    loading: false,
    modal: false,
  },
  create: {
    loading: false,
    modal: false,
  },
};
export const UsersSlice = createSlice({
  name: "users",
  initialState,
  reducers: {
    OpenUpdateModal: state => {
      state.update.modal = true;
    },
    CloseUpdateModal: state => {
      state.update.modal = false;
    },
    OpenCreateModal: state => {
      state.create.modal = true;
    },
    CloseCreateModal: state => {
      state.create.modal = false;
    },
  },
  extraReducers(builder) {
    builder
      .addCase(GetUserAction.pending, (state: InitialState) => {
        state.update.loading = true;
      })
      .addCase(
        GetUserAction.fulfilled,
        (state: InitialState, action: { payload: User }) => {
          state.update.loading = false;
          state.update.data = action.payload;
          state.update.modal = true;
        }
      )
      .addCase(GetUserAction.rejected, (state: InitialState) => {
        state.update.loading = false;
      });
  },
});

export const UsersReducer = UsersSlice.reducer;
