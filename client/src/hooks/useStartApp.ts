import {useDispatch, useSelector} from "react-redux";
import type {AppDispatch, RootState} from "../redux/store/store.ts";
import {useEffect, useRef} from "react";
import {useCheckConnection} from "../components/helpers/connection_tracker/useCheckConnection.ts";
import {fetchCurrentUser} from "../redux/thunks/fetchCurrentUser.ts";

export const useStartApp = () => {
    const dispatch = useDispatch<AppDispatch>();
    const isOnline = useSelector((root:RootState) => root.health.isOnline);
    const isFirstRender = useRef<boolean>(true);
    const { health, isLoading } = useCheckConnection();

    useEffect(() => {
        dispatch(fetchCurrentUser(isOnline));

        if (isFirstRender.current) {
            isFirstRender.current = false;
        }
    }, [dispatch, isOnline])

    return { health, isLoading, isFirstRender }
}