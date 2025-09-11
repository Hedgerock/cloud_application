import type {FC, PropsWithChildren} from "react";
import {useAuth} from "../../../hooks/auth/useAuth.ts";
import {Navigate} from "react-router-dom";
import {useSelector} from "react-redux";
import type {RootState} from "../../../redux/store/store.ts";

export const NonAuthBoundary: FC<PropsWithChildren> = ({ children }) => {
    const { isAuthenticated } = useAuth();
    const prevPath = useSelector((state: RootState) => state.navigation.prevPath);

    if (isAuthenticated) {
        return <Navigate to={prevPath} />
    }

    return <>{ children }</>
}