import { createAsyncThunk } from "@reduxjs/toolkit";
import { _DELETE, _POST, _PUT } from "../../api/config";
import { Permissions } from "../../types/permissions";

export type PayloadPermission = Pick<
  Permissions,
  "permissionKey" | "roleName" | "type"
>;

/**
 * Update Permission
 */
export const UpdatePermissionAction = createAsyncThunk(
  "roles/permission/update",
  async ({ data }: { data: PayloadPermission }, { rejectWithValue }) => {
    try {
      const response = await _PUT("/permissionRole", data);
      return response.data;
    } catch (error) {
      return rejectWithValue("Something wrong try again");
    }
  }
);

/**
 * Create Permission
 */

export const CreatePermissionAction = createAsyncThunk(
  "roles/permission/create",
  async ({ data }: { data: PayloadPermission }, { rejectWithValue }) => {
    try {
      const response = await _POST("/permissionRole", data);
      return response.data as unknown;
    } catch (error) {
      return rejectWithValue("Something wrong try again");
    }
  }
);

/**
 * Delete Permission
 */

export const DeletePermissionAction = createAsyncThunk(
  "roles/permission/delete",
  async ({ data }: { data: PayloadPermission }, { rejectWithValue }) => {
    try {
      const response = await _DELETE(
        `/permissionRole/${data.roleName}?permissionKey=${data.permissionKey}`
      );
      return response as unknown;
    } catch (error) {
      return rejectWithValue("Something wrong try again");
    }
  }
);

/**
 * Delete Permission by key
 */

export const DeletePermissionByKeyAction = createAsyncThunk(
  "permissions/deletekey",
  async ({ key }: { key: string }, { rejectWithValue }) => {
    try {
      const response = await _DELETE(`/permissions/key/${key}`);
      return response as unknown;
    } catch (error) {
      return rejectWithValue("Something wrong try again");
    }
  }
);

/**
 * Delete Permission by key
 */

export const CreatePermissionByMethodAction = createAsyncThunk(
  "permissions/create",
  async ({ data }: { data: any }, { rejectWithValue }) => {
    try {
      const response = await _POST(`/permissions`, data);
      return response as unknown;
    } catch (error) {
      return rejectWithValue("Something wrong try again");
    }
  }
);
