import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react";
import type {IUser} from "../../models/IUser.ts";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';
const username = 'hedgerock@gmail.com';
const password = '12345';
const basicAuth = btoa(`${username}:${password}`);
const AUTHORIZATION_HEADER = "Authorization";

export const usersApi = createApi({
    reducerPath: 'userApi',
    baseQuery: fetchBaseQuery({
        baseUrl: baseApiUrl,
        prepareHeaders: headers => {
            headers.set(AUTHORIZATION_HEADER, `Basic ${basicAuth}`)
            return headers;
        }
    }),
    endpoints: (builder) => ({
        getUserById: builder.query<IUser, number>({
            query: (id) => `api/v1/users/${ id }`
        })
    })
})

export const { useGetUserByIdQuery } = usersApi