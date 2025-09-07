import {createSlice, type PayloadAction} from "@reduxjs/toolkit";
import type {IAuthUser} from "../api/authApi.ts";

interface IAuthSlice {
    user: IAuthUser | null;
    isLoading: boolean;
    isAuthenticated: boolean;
}

export const initialValue: IAuthSlice = {
    user: null,
    isLoading: true,
    isAuthenticated: false
}

export const authSlice = createSlice({
    name: "authSlice",
    initialState: initialValue,
    reducers: {
        setUser(state, action: PayloadAction<IAuthUser>) {
            state.user = action.payload;
            state.isAuthenticated = true;
            state.isLoading = false;
        },
        setLoadingStatus(state, action: PayloadAction<{ isLoading: boolean }>) {
          state.isLoading = action.payload.isLoading;
        },
        clearUser(state) {
            state.user = initialValue.user;
            state.isLoading = initialValue.isLoading;
            state.isAuthenticated = initialValue.isAuthenticated;
        }
    }
})

export const { setUser, clearUser, setLoadingStatus } = authSlice.actions;
export default authSlice.reducer;