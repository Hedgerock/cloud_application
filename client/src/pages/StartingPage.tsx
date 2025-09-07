import {AppStructure} from "../hoc/template/AppStructure.tsx";
import {useGetUserByIdQuery} from "../redux/api/usersApi.ts";
import {LoadingProgress} from "../components/loading/LoadingProgress.tsx";
import {type FC} from "react";
import {Loading} from "../hoc/Loading.tsx";
import {ErrorPage} from "./ErrorPage.tsx";
import type {ISuccessPageProps} from "../models/ISuccessPageProps.tsx";
import {CurrentUser} from "../components/current_user/CurrentUser.tsx";
import {useAuth} from "../hooks/useAuth.ts";

export const StartingPage = () => {
    const { data, isError, isFetching, currentData } = useGetUserByIdQuery(1);
    const { isAuthenticated } = useAuth();

    return (
        <>
            <LoadingProgress isFetching={isFetching} />
            <AppStructure>
                { isError || !isAuthenticated
                    ? <ErrorPage message={"User not found"} />
                    : <SuccessPage currentData={ currentData } data={ data }/>
                }
            </AppStructure>
        </>
    )
}

const SuccessPage: FC<ISuccessPageProps> = ({ currentData, data }) => {

    return (
        <Loading isLoading={ !currentData }>
            { data && <CurrentUser entity={ data } />}
        </Loading>
    )
}