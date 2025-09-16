import {createAsyncThunk} from "@reduxjs/toolkit";
import type {IAuthUser} from "../api/authApi.ts";
import {clearUser, setLoadingStatus, setUser} from "../slices/authSlice.ts";
import {baseApiUrl} from "../constants";

export const fetchCurrentUser = createAsyncThunk(
    "auth/fetchCurrentUser",
    async (isOnline: boolean, thunkAPI) => {
        const { dispatch } = thunkAPI;
        if (!isOnline) {
            dispatch(setLoadingStatus({ isLoading: false }));
            return;
        }

        try {
            const response = await fetch(`${baseApiUrl}/api/auth/me`, {
                credentials: "include"
            });

            if (!response.ok) return;

            const user: IAuthUser = await response.json();
            dispatch(setUser(user));
        } catch (e) {
            console.error("Something went wrong", e);
            dispatch(clearUser());
        } finally {
            dispatch(setLoadingStatus({ isLoading: false }))
        }
    }
);
