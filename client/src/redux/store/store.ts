import {configureStore} from "@reduxjs/toolkit";
import {usersApi} from "../api/usersApi.ts";
import authReducer from '../slices/authSlice.ts';
import prevPathReducer from '../slices/pathSlice.ts';
import healthReducer from '../slices/healthSlice.ts';
import {authApi} from "../api/authApi.ts";
import {healthApi} from "../api/healthApi.ts";

export const store = configureStore({
    reducer: {
        auth: authReducer,
        navigation: prevPathReducer,
        health: healthReducer,
        [healthApi.reducerPath]: healthApi.reducer,
        [usersApi.reducerPath]: usersApi.reducer,
        [authApi.reducerPath]: authApi.reducer,
    },
    middleware: (getDefaultMiddleware) =>
        getDefaultMiddleware()
            .concat(healthApi.middleware)
            .concat(authApi.middleware)
            .concat(usersApi.middleware)
})

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;