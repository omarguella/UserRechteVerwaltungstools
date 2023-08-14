import { combineReducers } from "redux";
import { AuthReducer } from "./auth";
import { UsersReducer } from "./user";
import { RolesReducer } from "./roles";
import { PermissionsReducer } from "./permissions";

const reducers = combineReducers({
  auth: AuthReducer,
  users: UsersReducer,
  roles: RolesReducer,
  permissions: PermissionsReducer,
});
export type RootState = ReturnType<typeof reducers>;
export default reducers;
