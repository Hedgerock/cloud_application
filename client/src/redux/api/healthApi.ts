import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react";
import {baseApiUrl} from "../constants";

export const healthApi = createApi({
    reducerPath: 'healthApi',
    baseQuery: fetchBaseQuery({
        baseUrl: baseApiUrl,
    }),
    endpoints: (builder) => ({
        checkConnection: builder.query<{ message: string }, void>({
            query: () => `api/v1/health/ping`
        })
    })
})

export const { useLazyCheckConnectionQuery } = healthApi