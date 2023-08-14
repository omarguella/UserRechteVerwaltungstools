import { createSlice } from "@reduxjs/toolkit";

interface InitialState {
  create: {
    modal: boolean;
    loading: boolean;
  };
}
const initialState: InitialState = {
  create: {
    loading: false,
    modal: false,
  },
};
export const PermissionsSlice = createSlice({
  name: "permissions",
  initialState,
  reducers: {
    OpenCreateModal: state => {
      state.create.modal = true;
    },
    CloseCreateModal: state => {
      state.create.modal = false;
    },
  },
  extraReducers: builder => {
    builder
      .addCase("permissions/create/pending", (state: InitialState) => {
        state.create.loading = true;
      })
      .addCase("permissions/create/fulfilled", (state: InitialState) => {
        state.create.loading = false;
        state.create.modal = false;
      })
      .addCase("permissions/create/rejected", (state: InitialState) => {
        state.create.loading = false;
      });
  },
});

export const PermissionsReducer = PermissionsSlice.reducer;
