import {useDispatch, useSelector} from "react-redux";
import type {AppDispatch, RootState} from "../../../redux/store/store.ts";
import {useLazyCheckConnectionQuery} from "../../../redux/api/healthApi.ts";
import {useCallback, useEffect} from "react";
import {changeState} from "../../../redux/slices/healthSlice.ts";

export const useCheckConnection = () => {
    const dispatch = useDispatch<AppDispatch>();
    const health = useSelector((state:RootState) => state.health);
    const [checkConnection, { isLoading }] = useLazyCheckConnectionQuery();

    const initConnection = useCallback( async () => {
        try {
            await checkConnection().unwrap();
            if (health.isOnline !== true || health.isOnline === undefined) {
                dispatch(changeState({isOnline: true}));
            }
        } catch (e) {
            if (health.isOnline) {
                dispatch(changeState({isOnline: false}));
            }
        }
    }, [checkConnection, dispatch, health.isOnline])

    useEffect(() => {
        (async() => await initConnection())();
        const interval = setInterval(initConnection, 5000);
        return () => clearInterval(interval);
    }, [checkConnection, initConnection]);

    return { health, isLoading }
}