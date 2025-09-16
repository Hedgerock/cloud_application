import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react";
import {baseApiUrl, HttpMethods} from "../constants";

export interface IAuthUser {
    username: string;
    authorities: string[];
}

export type ILoginRegisterDefaultCredentials = {
    email: string,
    password: string;
}

export type ILoginCredentials = ILoginRegisterDefaultCredentials
export type IRegisterCredentials = ILoginRegisterDefaultCredentials;

export const authApi = createApi({
    reducerPath: "authApi",
    baseQuery: fetchBaseQuery({
        baseUrl: baseApiUrl,
        credentials: "include"
    }),
    endpoints: (builder) => ({
        login: builder.mutation<void, ILoginCredentials>({
            query: (body) => ({
                url: "/api/auth/login",
                method: HttpMethods.POST,
                body
            })
        }),
        register: builder.mutation<void, IRegisterCredentials>({
           query: (body) => ({
               url: "/api/auth/register",
               method: HttpMethods.POST,
               body
           }) 
        }),
        restorePassword: builder.mutation<void, { email: string }>({
           query: (body) => ({
               url: "/api/auth/restore_password",
               method: HttpMethods.POST,
               body
           })
        }),
        confirmPassword: builder.mutation<void, { token: string, password: string }>({
            query: (body) => ({
                url: "/api/auth/confirm_new_password",
                method: HttpMethods.POST,
                body
            })
        }),
        confirm: builder.mutation<void, { token: string }>({
           query: (body) => ({
               url: "/api/auth/confirm_email",
               method: HttpMethods.POST,
               body
           })
        }),
        logout: builder.mutation<void, void>({
           query: () => ({
               url: "/api/auth/logout",
               method: HttpMethods.POST
           })
        }),
        getCurrentUser: builder.query<IAuthUser, void>({
            query: () => "/api/auth/me"
        })
    })
})

export const {
    useLazyGetCurrentUserQuery,
    useLoginMutation,
    useLogoutMutation,
    useRegisterMutation,
    useConfirmMutation,
    useRestorePasswordMutation,
    useConfirmPasswordMutation
} = authApi;