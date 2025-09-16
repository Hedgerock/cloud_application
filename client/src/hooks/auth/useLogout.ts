import {useLogoutMutation} from "../../redux/api/authApi.ts";
import {useDispatch, useSelector} from "react-redux";
import type {AppDispatch, RootState} from "../../redux/store/store.ts";
import {clearUser, setLoadingStatus} from "../../redux/slices/authSlice.ts";
import {useAuth} from "./useAuth.ts";
import {useCallback} from "react";

export const useLogout = () => {
    const [logout] = useLogoutMutation();
    const { isAuthenticated } = useAuth();
    const dispatch = useDispatch<AppDispatch>();
    const health = useSelector((root:RootState) => root.health);

    const handleLogout = useCallback(async() => {
        if (!health.isOnline) return;
        if (!isAuthenticated) return;

        try {
            await logout();
            dispatch(clearUser());
            dispatch(setLoadingStatus({ isLoading: false }))
        } catch (e) {
            console.error("Failed to logout", e);
        }
    }, [dispatch, health.isOnline, isAuthenticated, logout])

    return { logout: handleLogout };
}