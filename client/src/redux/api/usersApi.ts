import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react";
import type {IUser} from "../../models/IUser.ts";
import {baseApiUrl} from "../constants";

export const usersApi = createApi({
    reducerPath: 'userApi',
    baseQuery: fetchBaseQuery({
        baseUrl: baseApiUrl,
        credentials: "include"
    }),
    endpoints: (builder) => ({
        getUserById: builder.query<IUser, number>({
            query: (id) => `api/v1/users/${ id }`
        })
    })
})

export const { useGetUserByIdQuery } = usersApi