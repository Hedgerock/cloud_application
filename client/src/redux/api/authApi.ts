import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react";
import {baseApiUrl} from "../constants";

export interface IAuthUser {
    username: string;
    authorities: string[];
}

export const authApi = createApi({
    reducerPath: "authApi",
    baseQuery: fetchBaseQuery({
        baseUrl: baseApiUrl,
        credentials: "include"
    }),
    endpoints: (builder) => ({
        login: builder.mutation<void, { email:string, password: string }>({
            query: (body) => ({
                url: "/api/auth/login",
                method: "POST",
                body
            })
        }),
        logout: builder.mutation<void, void>({
           query: () => ({
               url: "/api/auth/logout",
               method: "POST"
           })
        }),
        getCurrentUser: builder.query<IAuthUser, void>({
            query: () => "/api/auth/me"
        })
    })
})

export const { useLazyGetCurrentUserQuery, useLoginMutation, useLogoutMutation } = authApi;