import { createSlice } from "@reduxjs/toolkit";
import { Role } from "../../types/roles";

interface InitialState {
  update: {
    data: Role | null;
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
export const RolesSlice = createSlice({
  name: "roles",
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
  extraReducers: builder => {
    builder
      .addCase(
        "roles/find/pending" || "roles/update/pending",
        (state: InitialState) => {
          state.update.loading = true;
        }
      )
      .addCase("roles/find/fulfilled", (state: InitialState) => {
        state.update.loading = false;
        state.update.modal = true;
      })
      .addCase("roles/update/fulfilled", (state: InitialState) => {
        state.update.loading = false;
        state.update.modal = false;
      })
      .addCase(
        "roles/find/rejected" || "roles/update/pending",
        (state: InitialState) => {
          state.update.loading = false;
        }
      )
      .addCase("roles/create/pending", (state: InitialState) => {
        state.create.loading = true;
      })
      .addCase("roles/create/fulfilled", (state: InitialState) => {
        state.create.loading = false;
        state.create.modal = false;
      })
      .addCase("roles/create/rejected", (state: InitialState) => {
        state.create.loading = false;
      });
  },
});

export const RolesReducer = RolesSlice.reducer;
