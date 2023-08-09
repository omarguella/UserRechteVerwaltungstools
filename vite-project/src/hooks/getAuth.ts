import { store } from "../redux/store";

const UseGetAuth = async () => {
  const state = await store.getState();
  const { tokens } = state.auth;
  return { tokens };
};

export default UseGetAuth;
