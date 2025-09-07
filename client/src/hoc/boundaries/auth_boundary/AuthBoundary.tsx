import {type FC, type PropsWithChildren} from "react";
import {useAuth} from "../../../hooks/useAuth.ts";
import {Navigate} from "react-router-dom";

export const AuthBoundary:FC<PropsWithChildren> = ({ children }) => {
    const { isAuthenticated } = useAuth();

    if (!isAuthenticated) {
        return <Navigate to={"/login"} />
    }

    return <>{ children }</>
}